package dental.model;

/**
 * Sous-classe Dentiste qui hérite de la classe User.
 * Illustre :
 * - Le mot-clé 'extends'
 * - L'appel au constructeur parent via 'super(...)'
 * - L'ajout d'attributs spécifiques ('specialite', 'numeroOrdre')
 * - La redéfinition de méthode (@Override getRole() et toString())
 */
public class Dentiste extends User {

    private String specialite;
    private String numeroOrdre;

    public Dentiste() {
        super();
    }

    public Dentiste(int id, String cin, String nom, String prenom, String email, String telephone,
                    String login, String motDePasse, String specialite, String numeroOrdre) {
        // Appel explicite au constructeur de la classe mère User
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

    /**
     * Redéfinition (Override) de la méthode getRole() de la classe mère User.
     */
    @Override
    public String getRole() {
        return "DENTISTE";
    }

    @Override
    public String toString() {
        // Réutilisation de la méthode toString() du parent via 'super.toString()'
        return super.toString() + " - Dentiste [specialite=" + specialite + ", numeroOrdre=" + numeroOrdre + "]";
    }
}
