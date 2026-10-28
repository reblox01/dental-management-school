package dental.util;

/**
 * Utilitaire de nettoyage des entrées utilisateur (sanitisation).
 * 
 * Audit de sécurité (@securly) — Step 8 :
 * Toute donnée provenant de l'interface graphique doit être nettoyée
 * AVANT d'être transmise au service métier ou à la couche DAO.
 * 
 * Protections incluses :
 * 1. Suppression des espaces superflus (trim)
 * 2. Suppression des caractères de contrôle (null bytes, tabulations, etc.)
 * 3. Limitation de longueur maximale (évite les attaques par déni de service)
 * 4. Aucune concaténation SQL directe (garanti par PreparedStatement en DAO)
 */
public final class InputSanitizer {

    /** Longueur maximale par défaut autorisée pour un champ textuel */
    private static final int LONGUEUR_MAX_DEFAUT = 255;

    private InputSanitizer() {
        // Constructeur privé — classe utilitaire
    }

    /**
     * Nettoie une chaîne de caractères en supprimant les espaces et les caractères de contrôle.
     *
     * @param valeur la valeur brute provenant de l'interface
     * @return la valeur nettoyée, ou une chaîne vide si null
     */
    public static String nettoyer(String valeur) {
        return nettoyer(valeur, LONGUEUR_MAX_DEFAUT);
    }

    /**
     * Nettoie une chaîne avec limitation de longueur personnalisée.
     *
     * @param valeur      la valeur brute
     * @param longueurMax la longueur maximale autorisée
     * @return la valeur nettoyée et tronquée si nécessaire
     */
    public static String nettoyer(String valeur, int longueurMax) {
        if (valeur == null) {
            return "";
        }

        // 1. Trim des espaces en début et fin
        String resultat = valeur.trim();

        // 2. Suppression des caractères de contrôle (ASCII 0-31, retour à la ligne conservé)
        StringBuilder sb = new StringBuilder(resultat.length());
        for (int i = 0; i < resultat.length(); i++) {
            char c = resultat.charAt(i);
            if (c >= 32 || c == '\n' || c == '\r') {
                sb.append(c);
            }
        }
        resultat = sb.toString();

        // 3. Tronquer à la longueur maximale
        if (resultat.length() > longueurMax) {
            resultat = resultat.substring(0, longueurMax);
        }

        return resultat;
    }

    /**
     * Vérifie qu'une chaîne ne contient pas de tentatives d'injection évidentes.
     * Note : Ceci est une couche de défense en profondeur complémentaire au PreparedStatement.
     *
     * @param valeur la valeur à contrôler
     * @return true si la valeur semble suspecte
     */
    public static boolean contientInjection(String valeur) {
        if (valeur == null || valeur.isEmpty()) {
            return false;
        }

        String lower = valeur.toLowerCase();
        // Détection de motifs SQL courants (défense en profondeur)
        return lower.contains("' or ") || lower.contains("'; drop")
                || lower.contains("\" or ") || lower.contains("1=1")
                || lower.contains("--") && lower.contains("'");
    }
}
