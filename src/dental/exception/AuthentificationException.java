package dental.exception;

/**
 * Exception levée lors d'un problème lié au processus d'authentification.
 * Hérite de DentalException.
 */
public class AuthentificationException extends DentalException {

    public AuthentificationException(String message) {
        super(message);
    }

    public AuthentificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
