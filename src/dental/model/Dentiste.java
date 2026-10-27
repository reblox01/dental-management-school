package dental.model;

/**
 * Sous-classe concrète de la classe abstraite User.
 * Implémente obligatoirement les méthodes abstraites déclarées dans User :
 * - decrireRole()
 * - aPrivilege(String)
 * - getRole()
 */
public class Dentiste extends User {

    private String specialite;
    private String numeroOrdre;

    public Dentiste() {
        super();
    }

    public Dentiste(int id, String cin, String nom, String prenom, String email, String telephone,
                    String login, String motDePasse, String specialite, String numeroOrdre) {
        super(id, cin, nom, prenom, email, telephone, login, motDePasse);
        this.specialite = specialite;
        this.numeroOrdre = numeroOrdre;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    @Override
    public String getRole() {
        return "DENTISTE";
    }

    /**
     * Implémentation concrète de la méthode abstraite decrireRole().
     */
    @Override
    public String decrireRole() {
        return "Chirurgien-Dentiste (Spécialité : " + (specialite != null ? specialite : "Généraliste")
                + ", N° Ordre : " + (numeroOrdre != null ? numeroOrdre : "N/A")
                + ") - Responsable des diagnostics, actes chirurgicaux et prescriptions.";
    }

    /**
     * Implémentation concrète de la méthode abstraite aPrivilege(String).
     * Le praticien dentiste possède tous les droits d'administration médicale.
     */
    @Override
    public boolean aPrivilege(String privilege) {
        if (privilege == null) {
            return false;
        }
        // Le dentiste a un accès complet à toutes les fonctionnalités
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " - Dentiste [specialite=" + specialite + ", numeroOrdre=" + numeroOrdre + "]";
    }
}
