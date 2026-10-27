package dental.exception;

/**
 * Exception de base (checked) pour toutes les erreurs métier du cabinet dentaire.
 * Hérite de java.lang.Exception.
 */
public class DentalException extends Exception {

    public DentalException(String message) {
        super(message);
    }

    public DentalException(String message, Throwable cause) {
        super(message, cause);
    }
}
