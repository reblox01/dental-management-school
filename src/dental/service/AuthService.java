package dental.service;

import dental.model.Assistant;
import dental.model.Dentiste;
import dental.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Service d'authentification démontrant l'héritage et le polymorphisme.
 * Une liste hétérogène List<User> contient à la fois des instances de Dentiste et d'Assistant.
 */
public class AuthService {

    // Liste polymorphique contenant des objets de type User (Dentiste et Assistant)
    private final List<User> utilisateursEnMemoire;

    public AuthService() {
        this.utilisateursEnMemoire = new ArrayList<>();
        chargerUtilisateursInitiaux();
    }

    /**
     * Initialise des utilisateurs de test illustrant l'héritage.
     */
    private void chargerUtilisateursInitiaux() {
        // Ajout d'une instance de Dentiste (sous-classe de User)
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

        // Ajout d'une instance d'Assistant (sous-classe de User)
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
     * Authentifie un utilisateur à partir de son login et de son mot de passe.
     * Démonstration du polymorphisme : la méthode manipule la référence générique 'User'.
     *
     * @param login       l'identifiant saisi
     * @param motDePasse  le mot de passe saisi
     * @return l'instance de User (Dentiste ou Assistant), ou null si échec
     */
    public User authentifier(String login, String motDePasse) {
        if (login == null || motDePasse == null) {
            return null;
        }

        for (User u : utilisateursEnMemoire) {
            if (u.getLogin().equalsIgnoreCase(login.trim()) && u.getMotDePasse().equals(motDePasse)) {
                return u; // Polymorphisme : l'objet retourné est concrètement un Dentiste ou un Assistant
            }
        }
        return null;
    }

    public List<User> getUtilisateursEnMemoire() {
        return utilisateursEnMemoire;
    }
}
