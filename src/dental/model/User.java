package dental.model;

/**
 * Classe de base représentant un utilisateur du cabinet dentaire.
 * Illustre les principes fondamentaux de la POO :
 * - Encapsulation (attributs privés, accesseurs publics)
 * - Base pour l'héritage (classe mère de Dentiste et Assistant)
 */
public class User {

    private int id;
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String login;
    private String motDePasse;

    public User() {
    }

    public User(int id, String cin, String nom, String prenom, String email, String telephone, String login, String motDePasse) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.login = login;
        this.motDePasse = motDePasse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * Méthode métier retournant le nom complet de l'utilisateur.
     */
    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom.toUpperCase() : "");
    }

    /**
     * Retourne le rôle de l'utilisateur.
     * Cette méthode sera redéfinie (override) par les sous-classes.
     */
    public String getRole() {
        return "UTILISATEUR";
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", cin=" + cin + ", nomComplet=" + getNomComplet()
                + ", email=" + email + ", role=" + getRole() + "]";
    }
}
