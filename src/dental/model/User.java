package dental.model;

/**
 * Classe abstraite représentant un utilisateur du cabinet dentaire.
 * Illustre :
 * - Le mot-clé 'abstract' sur la classe (impossible d'instancier directement avec 'new User')
 * - La déclaration de méthodes abstraites (sans corps) imposant un contrat aux sous-classes
 * - Le polymorphisme d'appel sur les méthodes abstraites
 */
public abstract class User {

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
     * Méthode concrète commune : retourne le nom complet.
     */
    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom.toUpperCase() : "");
    }

    /**
     * Méthode abstraite 1 : Oblige chaque sous-classe à fournir
     * une description détaillée de son rôle au sein du cabinet.
     */
    public abstract String decrireRole();

    /**
     * Méthode abstraite 2 : Oblige chaque sous-classe à définir
     * son système de permissions et privilèges applicatifs.
     *
     * @param privilege le code du privilège à vérifier (ex: "SUPPRIMER_PATIENT")
     * @return true si l'utilisateur possède ce privilège, false sinon
     */
    public abstract boolean aPrivilege(String privilege);

    /**
     * Méthode abstraite 3 : Retourne le libellé court du rôle.
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return "User [id=" + id + ", cin=" + cin + ", nomComplet=" + getNomComplet()
                + ", email=" + email + ", role=" + getRole()
                + ", description=" + decrireRole() + "]";
    }
}
