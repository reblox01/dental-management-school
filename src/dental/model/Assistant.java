package dental.model;

/**
 * Sous-classe Assistant qui hérite de la classe User.
 * Illustre :
 * - Le mot-clé 'extends'
 * - L'appel au constructeur parent via 'super(...)'
 * - L'attribut spécifique ('dateEmbauche')
 * - La redéfinition polymorphique (@Override getRole() et toString())
 */
public class Assistant extends User {

    private String dateEmbauche;

    public Assistant() {
        super();
    }

    public Assistant(int id, String cin, String nom, String prenom, String email, String telephone,
                     String login, String motDePasse, String dateEmbauche) {
        // Appel au constructeur de la classe mère User
        super(id, cin, nom, prenom, email, telephone, login, motDePasse);
        this.dateEmbauche = dateEmbauche;
    }

    public String getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    /**
     * Redéfinition de la méthode getRole() héritée de User.
     */
    @Override
    public String getRole() {
        return "ASSISTANT";
    }

    @Override
    public String toString() {
        return super.toString() + " - Assistant [dateEmbauche=" + dateEmbauche + "]";
    }
}
