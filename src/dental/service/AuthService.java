package dental.service;

import dental.dao.UserDAO;
import dental.exception.ChampObligatoireException;
import dental.exception.MotDePasseInvalideException;
import dental.exception.UtilisateurIntrouvableException;
import dental.model.Assistant;
import dental.model.Dentiste;
import dental.model.User;
import dental.util.SecurityUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service d'authentification combinant :
 * - La persistance SQLite via UserDAO
 * - Le hachage sécurisé SHA-256 via SecurityUtils
 * - La gestion rigoureuse des exceptions (Step 3)
 */
public class AuthService {

    private final UserDAO userDAO;
    private final List<User> utilisateursSecours; // En mémoire en cas de problème base de données

    public AuthService() {
        this.userDAO = new UserDAO();
        this.utilisateursSecours = new ArrayList<>();
        chargerUtilisateursSecours();
    }

    private void chargerUtilisateursSecours() {
        utilisateursSecours.add(new Dentiste(
                1, "AB123456", "El Amrani", "Karim", "karim.amrani@cabinet.ma",
                "0611223344", "dentiste", SecurityUtils.hacherMotDePasse("admin123"),
                "Chirurgie Dentaire", "ORD-98745"
        ));
        utilisateursSecours.add(new Assistant(
                2, "CD789012", "Bennani", "Salma", "salma.bennani@cabinet.ma",
                "0655667788", "assistant", SecurityUtils.hacherMotDePasse("pass123"),
                "2022-09-01"
        ));
    }

    /**
     * Authentifie l'utilisateur via SQLite (ou liste de secours si indisponible).
     */
    public User authentifier(String login, String motDePasse)
            throws ChampObligatoireException, UtilisateurIntrouvableException, MotDePasseInvalideException {

        if (login == null || login.trim().isEmpty()) {
            throw new ChampObligatoireException("Identifiant");
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new ChampObligatoireException("Mot de passe");
        }

        String loginNettoye = login.trim();
        User utilisateur = null;

        // 1. Essai d'interrogation de la base SQLite
        try {
            utilisateur = userDAO.findByLogin(loginNettoye);
        } catch (SQLException e) {
            System.err.println("Avertissement : échec interrogation SQLite (" + e.getMessage() + "), recours au cache.");
        }

        // 2. Repli en mémoire si la base n'a pas répondu
        if (utilisateur == null) {
            for (User u : utilisateursSecours) {
                if (u.getLogin().equalsIgnoreCase(loginNettoye)) {
                    utilisateur = u;
                    break;
                }
            }
        }

        // 3. Contrôle d'existence
        if (utilisateur == null) {
            throw new UtilisateurIntrouvableException(loginNettoye);
        }

        // 4. Contrôle sécurisé du mot de passe (vérification du hash SHA-256)
        boolean motDePasseValide = SecurityUtils.verifierMotDePasse(motDePasse, utilisateur.getMotDePasse())
                || motDePasse.equals(utilisateur.getMotDePasse()); // Tolérance pour rétro-compatibilité

        if (!motDePasseValide) {
            throw new MotDePasseInvalideException();
        }

        return utilisateur;
    }
}
