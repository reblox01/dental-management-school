package dental.exception;

/**
 * Exception levée lors d'une erreur de validation des données d'un formulaire.
 * Hérite de DentalException.
 */
public class ValidationException extends DentalException {

    private final String nomChamp;

    public ValidationException(String nomChamp, String message) {
        super("Erreur sur le champ '" + nomChamp + "' : " + message);
        this.nomChamp = nomChamp;
    }

    public ValidationException(String message) {
        super(message);
        this.nomChamp = "Inconnu";
    }

    public String getNomChamp() {
        return nomChamp;
    }
}
