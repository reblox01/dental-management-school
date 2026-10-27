package dental.exception;

/**
 * Exception spécifique levée lorsque l'identifiant (login) n'existe pas dans le système.
 * Hérite de AuthentificationException.
 */
public class UtilisateurIntrouvableException extends AuthentificationException {

    private final String loginSaisi;

    public UtilisateurIntrouvableException(String loginSaisi) {
        super("Aucun utilisateur trouvé avec l'identifiant : '" + loginSaisi + "'");
        this.loginSaisi = loginSaisi;
    }

    public String getLoginSaisi() {
        return loginSaisi;
    }
}
