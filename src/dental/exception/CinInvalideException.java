package dental.exception;

/**
 * Exception spécifique levée lorsqu'un format de CIN (Carte d'Identité Nationale) est invalide.
 * Hérite de ValidationException.
 */
public class CinInvalideException extends ValidationException {

    public CinInvalideException(String cinSaisi) {
        super("CIN", "Le format de la CIN '" + cinSaisi + "' est invalide (Attendu : 1 ou 2 lettres majuscules suivies de 4 à 7 chiffres, ex: AB123456).");
    }
}
