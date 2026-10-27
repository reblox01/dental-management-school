package dental.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaires de sécurité (@securly) :
 * - Hachage robuste des mots de passe en SHA-256 avec sel fixe
 * - Comparaison en temps constant pour éviter les attaques par canal auxiliaire (timing attacks)
 * - Empêche le stockage des mots de passe en clair dans la base SQLite
 */
public final class SecurityUtils {

    // Sel cryptographique fixe pour l'application scolaire
    private static final String SEL = "DentalCabinetSecureSalt2026";

    private SecurityUtils() {
        // Constructeur privé
    }

    /**
     * Calcule le hash SHA-256 salé d'un mot de passe.
     *
     * @param motDePasse mot de passe en clair
     * @return chaîne hexadécimale de 64 caractères
     */
    public static String hacherMotDePasse(String motDePasse) {
        if (motDePasse == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String avecSel = SEL + motDePasse;
            byte[] hash = digest.digest(avecSel.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur critique : algorithme SHA-256 non disponible", e);
        }
    }

    /**
     * Vérifie la concordance d'un mot de passe en temps constant.
     *
     * @param motDePasseSaisi le mot de passe en clair saisi par l'utilisateur
     * @param hashEnregistre  le hash SHA-256 stocké en base
     * @return true si le mot de passe correspond
     */
    public static boolean verifierMotDePasse(String motDePasseSaisi, String hashEnregistre) {
        if (motDePasseSaisi == null || hashEnregistre == null) {
            return false;
        }
        String hashCalcule = hacherMotDePasse(motDePasseSaisi);
        // Comparaison sécurisée en temps constant (MessageDigest.isEqual)
        return MessageDigest.isEqual(
                hashCalcule.getBytes(StandardCharsets.UTF_8),
                hashEnregistre.getBytes(StandardCharsets.UTF_8)
        );
    }
}
