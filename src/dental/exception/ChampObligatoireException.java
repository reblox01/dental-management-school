package dental.exception;

/**
 * Exception spécifique levée lorsqu'un champ obligatoire est laissé vide ou null.
 * Hérite de ValidationException.
 */
public class ChampObligatoireException extends ValidationException {

    public ChampObligatoireException(String nomChamp) {
        super(nomChamp, "Le champ '" + nomChamp + "' est obligatoire et ne peut pas être vide.");
    }
}
