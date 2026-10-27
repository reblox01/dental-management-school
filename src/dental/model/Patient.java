package dental.model;

/**
 * Entité représentant un dossier patient au cabinet dentaire.
 * Modélisation objet standard avec encapsulation complète.
 */
public class Patient {

    private int id;
    private String cin;
    private String nom;
    private String prenom;
    private String sexe;        // "M" ou "F"
    private int age;
    private String telephone;
    private String email;
    private String adresse;
    private String mutuelle;    // Ex: "CNSS", "CNOPS", "AXA", "Aucune"
    private String antecedents; // Ex: "Diabète type 2", "Allergie pénicilline", "Aucun"

    public Patient() {
    }

    public Patient(int id, String cin, String nom, String prenom, String sexe, int age,
                   String telephone, String email, String adresse, String mutuelle, String antecedents) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.mutuelle = mutuelle;
        this.antecedents = antecedents;
    }

    public Patient(String cin, String nom, String prenom, String sexe, int age,
                   String telephone, String email, String adresse, String mutuelle, String antecedents) {
        this(0, cin, nom, prenom, sexe, age, telephone, email, adresse, mutuelle, antecedents);
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom.toUpperCase() : "");
    }

    @Override
    public String toString() {
        return "Patient [id=" + id + ", cin=" + cin + ", nomComplet=" + getNomComplet()
                + ", age=" + age + ", sexe=" + sexe + ", telephone=" + telephone + ", mutuelle=" + mutuelle + "]";
    }
}
