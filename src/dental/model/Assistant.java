package dental.model;

/**
 * Sous-classe concrète de la classe abstraite User.
 * Implémente obligatoirement les méthodes abstraites déclarées dans User :
 * - decrireRole()
 * - aPrivilege(String)
 * - getRole()
 */
public class Assistant extends User {

    private String dateEmbauche;

    public Assistant() {
        super();
    }

    public Assistant(int id, String cin, String nom, String prenom, String email, String telephone,
                     String login, String motDePasse, String dateEmbauche) {
        super(id, cin, nom, prenom, email, telephone, login, motDePasse);
        this.dateEmbauche = dateEmbauche;
    }

    public String getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    @Override
    public String getRole() {
        return "ASSISTANT";
    }

    /**
     * Implémentation concrète de la méthode abstraite decrireRole().
     */
    @Override
    public String decrireRole() {
        return "Assistant(e) Dentaire (Embauché(e) le : " + (dateEmbauche != null ? dateEmbauche : "N/A")
                + ") - Responsable de l'accueil, de la prise de rendez-vous et de la saisie des dossiers patients.";
    }

    /**
     * Implémentation concrète de la méthode abstraite aPrivilege(String).
     * L'assistant a des droits restreints : il ne peut pas supprimer un patient, ni prescrire d'actes.
     */
    @Override
    public boolean aPrivilege(String privilege) {
        if (privilege == null) {
            return false;
        }

        switch (privilege.toUpperCase()) {
            case "CONSULTER_PATIENTS":
            case "AJOUTER_PATIENT":
            case "MODIFIER_PATIENT":
            case "VOIR_STATISTIQUES":
                return true;
            case "SUPPRIMER_PATIENT":
            case "PRESCRIRE_ACTE":
            case "ADMINISTRATION":
            default:
                // Privilèges refusés pour le rôle Assistant
                return false;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - Assistant [dateEmbauche=" + dateEmbauche + "]";
    }
}
