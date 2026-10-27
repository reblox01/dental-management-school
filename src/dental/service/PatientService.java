package dental.service;

import dental.dao.PatientDAO;
import dental.exception.DentalException;
import dental.exception.EntiteNonTrouveeException;
import dental.exception.ValidationException;
import dental.functional.Validateur;
import dental.functional.Validateurs;
import dental.model.Patient;
import java.sql.SQLException;
import java.util.List;

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
}
