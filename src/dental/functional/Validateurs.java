package dental.functional;

import dental.exception.ChampObligatoireException;
import dental.exception.CinInvalideException;
import dental.exception.ValidationException;
import java.util.regex.Pattern;

/**
 * Fabrique de validateurs fonctionnels.
 * Illustre comment instancier une interface fonctionnelle via des expressions lambda.
 */
public final class Validateurs {

    // Expressions régulières pour validation
    private static final Pattern PATTERN_CIN = Pattern.compile("^[A-Za-z]{1,2}[0-9]{4,7}$");
    private static final Pattern PATTERN_TEL = Pattern.compile("^(?:\\+212|0)[5-7][0-9]{8}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validateurs() {
    }

    /**
     * Valide qu'une chaîne n'est ni nulle ni vide.
     */
    public static Validateur<String> nonVide(String nomChamp) {
        return valeur -> {
            if (valeur == null || valeur.trim().isEmpty()) {
                throw new ChampObligatoireException(nomChamp);
            }
        };
    }

    /**
     * Valide la longueur minimale d'une chaîne.
     */
    public static Validateur<String> longueurMin(String nomChamp, int min) {
        return valeur -> {
            if (valeur != null && valeur.trim().length() < min) {
                throw new ValidationException(nomChamp, "Doit comporter au moins " + min + " caractères.");
            }
        };
    }

    /**
     * Valide le format de la CIN marocaine (ex: AB123456 ou B12345).
     */
    public static Validateur<String> formatCin() {
        return cin -> {
            if (cin == null || !PATTERN_CIN.matcher(cin.trim()).matches()) {
                throw new CinInvalideException(cin != null ? cin : "");
            }
        };
    }

    /**
     * Valide le format d'un numéro de téléphone.
     */
    public static Validateur<String> formatTelephone() {
        return tel -> {
            if (tel != null && !tel.trim().isEmpty()) {
                String propre = tel.trim().replaceAll("\\s+", "");
                if (!PATTERN_TEL.matcher(propre).matches()) {
                    throw new ValidationException("Téléphone", "Format invalide (Ex: 0612345678 ou +212612345678).");
                }
            }
        };
    }

    /**
     * Valide le format d'une adresse email (si renseignée).
     */
    public static Validateur<String> formatEmail() {
        return email -> {
            if (email != null && !email.trim().isEmpty()) {
                if (!PATTERN_EMAIL.matcher(email.trim()).matches()) {
                    throw new ValidationException("Email", "Format d'adresse email invalide (Ex: patient@domaine.com).");
                }
            }
        };
    }

    /**
     * Valide que l'âge est cohérent pour un patient.
     */
    public static Validateur<Integer> ageValide(int min, int max) {
        return age -> {
            if (age == null || age < min || age > max) {
                throw new ValidationException("Âge", "L'âge doit être compris entre " + min + " et " + max + " ans.");
            }
        };
    }
}
