package dental.exception;

/**
 * Exception levée lorsqu'une entité recherchée (ex: Patient introuvable par son CIN) n'existe pas.
 * Hérite de DentalException.
 */
public class EntiteNonTrouveeException extends DentalException {

    private final String typeEntite;
    private final Object identifiant;

    public EntiteNonTrouveeException(String typeEntite, Object identifiant) {
        super(typeEntite + " introuvable avec l'identifiant : " + identifiant);
        this.typeEntite = typeEntite;
        this.identifiant = identifiant;
    }

    public String getTypeEntite() {
        return typeEntite;
    }

    public Object getIdentifiant() {
        return identifiant;
    }
}
