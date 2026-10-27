package dental.exception;

/**
 * Exception spécifique levée lorsque le mot de passe ne correspond pas au compte utilisateur.
 * Hérite de AuthentificationException.
 */
public class MotDePasseInvalideException extends AuthentificationException {

    public MotDePasseInvalideException() {
        super("Le mot de passe saisi est incorrect.");
    }

    public MotDePasseInvalideException(String message) {
        super(message);
    }
}
