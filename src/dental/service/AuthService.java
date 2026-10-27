package dental.service;

import dental.exception.ChampObligatoireException;
import dental.exception.MotDePasseInvalideException;
import dental.exception.UtilisateurIntrouvableException;
import dental.model.Assistant;
import dental.model.Dentiste;
import dental.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Service d'authentification démontrant l'utilisation des exceptions :
 * - Déclaration des exceptions avec 'throws'
 * - Levée d'exceptions métier avec 'throw new ...'
 * - Fin des retours silencieux 'null' au profit d'un contrat explicite et robuste
 */
public class AuthService {

    private final List<User> utilisateursEnMemoire;

    public AuthService() {
        this.utilisateursEnMemoire = new ArrayList<>();
        chargerUtilisateursInitiaux();
    }

    private void chargerUtilisateursInitiaux() {
        utilisateursEnMemoire.add(new Dentiste(
                1,
                "AB123456",
                "El Amrani",
                "Karim",
                "karim.amrani@cabinet.ma",
                "0611223344",
                "dentiste",
                "admin123",
                "Chirurgie Dentaire & Implantologie",
                "ORD-98745"
        ));

        utilisateursEnMemoire.add(new Assistant(
                2,
                "CD789012",
                "Bennani",
                "Salma",
                "salma.bennani@cabinet.ma",
                "0655667788",
                "assistant",
                "pass123",
                "2022-09-01"
        ));
    }

    /**
     * Authentifie un utilisateur avec contrôle strict par exceptions.
     *
     * @param login      identifiant
     * @param motDePasse mot de passe
     * @return l'utilisateur authentifié (garanti non-null en cas de succès)
     * @throws ChampObligatoireException      si le login ou le mot de passe est vide
     * @throws UtilisateurIntrouvableException si l'identifiant n'existe pas
     * @throws MotDePasseInvalideException    si le mot de passe ne correspond pas
     */
    public User authentifier(String login, String motDePasse)
            throws ChampObligatoireException, UtilisateurIntrouvableException, MotDePasseInvalideException {

        // 1. Validation des paramètres obligatoires
        if (login == null || login.trim().isEmpty()) {
            throw new ChampObligatoireException("Identifiant");
        }
        if (motDePasse == null || motDePasse.isEmpty()) {
            throw new ChampObligatoireException("Mot de passe");
        }

        String loginNettoye = login.trim();

        // 2. Recherche de l'utilisateur par son identifiant
        User utilisateurTrouve = null;
        for (User u : utilisateursEnMemoire) {
            if (u.getLogin().equalsIgnoreCase(loginNettoye)) {
                utilisateurTrouve = u;
                break;
            }
        }

        if (utilisateurTrouve == null) {
            // Levée explicite : Utilisateur non trouvé
            throw new UtilisateurIntrouvableException(loginNettoye);
        }

        // 3. Vérification du mot de passe
        if (!utilisateurTrouve.getMotDePasse().equals(motDePasse)) {
            // Levée explicite : Mot de passe incorrect
            throw new MotDePasseInvalideException();
        }

        return utilisateurTrouve;
    }

    public List<User> getUtilisateursEnMemoire() {
        return utilisateursEnMemoire;
    }
}
