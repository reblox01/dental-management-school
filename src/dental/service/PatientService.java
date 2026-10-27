package dental.service;

import dental.dao.PatientDAO;
import dental.exception.DentalException;
import dental.exception.EntiteNonTrouveeException;
import dental.exception.ValidationException;
import dental.functional.Validateur;
import dental.functional.Validateurs;
import dental.model.Patient;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Service métier pour la gestion des patients.
 * Utilise l'interface fonctionnelle Validateur<T> et sa méthode andThen() pour valider les données.
 */
public class PatientService {

    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }

    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    /**
     * Valide l'ensemble des données d'un patient à l'aide de l'interface fonctionnelle Validateur<T>.
     * Démonstration du chaînage fonctionnel avec .andThen(...)
     */
    public void validerPatient(Patient patient) throws ValidationException {
        if (patient == null) {
            throw new ValidationException("Patient", "L'objet patient ne peut pas être null.");
        }

        // Chaînage fonctionnel 1 : CIN non vide ET format CIN valide
        Validateur<String> validateurCin = Validateurs.nonVide("CIN")
                                                      .andThen(Validateurs.formatCin());
        validateurCin.valider(patient.getCin());

        // Chaînage fonctionnel 2 : Nom non vide ET longueur minimale de 2 caractères
        Validateur<String> validateurNom = Validateurs.nonVide("Nom")
                                                      .andThen(Validateurs.longueurMin("Nom", 2));
        validateurNom.valider(patient.getNom());

        // Chaînage fonctionnel 3 : Prénom non vide ET longueur minimale de 2 caractères
        Validateur<String> validateurPrenom = Validateurs.nonVide("Prénom")
                                                        .andThen(Validateurs.longueurMin("Prénom", 2));
        validateurPrenom.valider(patient.getPrenom());

        // Validation fonctionnelle 4 : Âge compris entre 0 et 125 ans
        Validateur<Integer> validateurAge = Validateurs.ageValide(0, 125);
        validateurAge.valider(patient.getAge());

        // Validation fonctionnelle 5 : Téléphone (format optionnel mais strict si renseigné)
        Validateurs.formatTelephone().valider(patient.getTelephone());

        // Validation fonctionnelle 6 : Email (format optionnel mais strict si renseigné)
        Validateurs.formatEmail().valider(patient.getEmail());
    }

    /**
     * Enregistre un nouveau patient après validation fonctionnelle.
     */
    public Patient ajouterPatient(Patient patient) throws DentalException {
        validerPatient(patient);

        try {
            // Vérifier l'unicité de la CIN
            Patient existant = patientDAO.findByCin(patient.getCin());
            if (existant != null) {
                throw new ValidationException("CIN", "Un patient avec le CIN '" + patient.getCin() + "' existe déjà.");
            }
            return patientDAO.create(patient);

        } catch (SQLException e) {
            throw new DentalException("Erreur lors de l'enregistrement du patient en base : " + e.getMessage(), e);
        }
    }

    /**
     * Modifie un patient existant après validation fonctionnelle.
     */
    public void modifierPatient(Patient patient) throws DentalException {
        validerPatient(patient);

        try {
            Patient existant = patientDAO.findById(patient.getId());
            if (existant == null) {
                throw new EntiteNonTrouveeException("Patient", patient.getId());
            }

            // Vérifier si la CIN modifiée n'appartient pas à un autre patient
            Patient autrePatient = patientDAO.findByCin(patient.getCin());
            if (autrePatient != null && autrePatient.getId() != patient.getId()) {
                throw new ValidationException("CIN", "Le CIN '" + patient.getCin() + "' est déjà attribué à un autre patient.");
            }

            patientDAO.update(patient);

        } catch (SQLException e) {
            throw new DentalException("Erreur lors de la mise à jour du patient : " + e.getMessage(), e);
        }
    }

    /**
     * Supprime un patient par son identifiant unique.
     */
    public void supprimerPatient(int id) throws DentalException {
        try {
            boolean supprime = patientDAO.delete(id);
            if (!supprime) {
                throw new EntiteNonTrouveeException("Patient", id);
            }
        } catch (SQLException e) {
            throw new DentalException("Erreur lors de la suppression du patient : " + e.getMessage(), e);
        }
    }

    /**
     * Retourne la liste complète des patients.
     */
    public List<Patient> obtenirTousLesPatients() throws DentalException {
        try {
            return patientDAO.findAll();
        } catch (SQLException e) {
            throw new DentalException("Impossible de charger la liste des patients : " + e.getMessage(), e);
        }
    }

    /**
     * Recherche des patients par mot-clé (Nom, Prénom, CIN, Téléphone).
     */
    public List<Patient> rechercherPatients(String motCle) throws DentalException {
        try {
            if (motCle == null || motCle.trim().isEmpty()) {
                return patientDAO.findAll();
            }
            return patientDAO.search(motCle.trim());
        } catch (SQLException e) {
            throw new DentalException("Erreur lors de la recherche des patients : " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // ÉTAPE 6 : INTERFACES FONCTIONNELLES PRÉ-DÉFINIES (java.util.function)
    // =========================================================================

    /**
     * 1. Utilisation de Predicate<T> :
     * Filtre la liste des patients selon un critère booléen passé sous forme d'expression lambda.
     * Démonstration de l'interface fonctionnelle standard java.util.function.Predicate.
     *
     * @param filtre prédicat déterminant si un patient doit être conservé
     * @return la liste filtrée
     */
    public List<Patient> filtrerPatients(Predicate<Patient> filtre) throws DentalException {
        List<Patient> tous = obtenirTousLesPatients();
        List<Patient> resultat = new ArrayList<>();

        if (filtre == null) {
            return tous;
        }

        for (Patient p : tous) {
            // Appel de la méthode SAM de Predicate : boolean test(T t)
            if (filtre.test(p)) {
                resultat.add(p);
            }
        }
        return resultat;
    }

    /**
     * 2. Utilisation de Function<T, R> :
     * Transforme/Mappe chaque patient en un autre type (ex: String de synthèse ou CSV).
     * Démonstration de l'interface standard java.util.function.Function.
     *
     * @param transformateur fonction appliquant la transformation T -> R
     * @param <R>            le type cible
     * @return la liste des résultats transformés
     */
    public <R> List<R> transformerPatients(Function<Patient, R> transformateur) throws DentalException {
        List<Patient> tous = obtenirTousLesPatients();
        List<R> resultat = new ArrayList<>();

        if (transformateur == null) {
            return resultat;
        }

        for (Patient p : tous) {
            // Appel de la méthode SAM de Function : R apply(T t)
            resultat.add(transformateur.apply(p));
        }
        return resultat;
    }

    /**
     * 3. Utilisation de Consumer<T> :
     * Applique une action/effet de bord sur chaque patient (ex: journalisation, notification).
     * Démonstration de l'interface standard java.util.function.Consumer.
     *
     * @param action consommateur effectuant une action sur le patient
     */
    public void appliquerSurPatients(Consumer<Patient> action) throws DentalException {
        if (action == null) {
            return;
        }
        List<Patient> tous = obtenirTousLesPatients();
        for (Patient p : tous) {
            // Appel de la méthode SAM de Consumer : void accept(T t)
            action.accept(p);
        }
    }

    /**
     * 4. Utilisation de Supplier<T> :
     * Fournit une liste de patients à la demande (alimentation paresseuse ou source externe).
     * Démonstration de l'interface standard java.util.function.Supplier.
     *
     * @param fournisseur fournisseur retournant les données
     * @return les données obtenues depuis le Supplier
     */
    public List<Patient> chargerDepuisFournisseur(Supplier<List<Patient>> fournisseur) {
        if (fournisseur == null) {
            return new ArrayList<>();
        }
        // Appel de la méthode SAM de Supplier : T get()
        return fournisseur.get();
    }
}
