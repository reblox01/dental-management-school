package dental.dao;

import dental.database.DatabaseManager;
import dental.model.Assistant;
import dental.model.Dentiste;
import dental.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour l'entité User et ses sous-classes.
 * Toutes les requêtes SQL utilisent exclusivement PreparedStatement pour garantir la sécurité.
 */
public class UserDAO {

    /**
     * Recherche un utilisateur par son login.
     * Reconstitue polymorphiquement un objet Dentiste ou Assistant selon la colonne 'role'.
     */
    public User findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM users WHERE LOWER(login) = LOWER(?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraireUtilisateur(rs);
                }
            }
        }
        return null;
    }

    /**
     * Récupère la liste complète des utilisateurs.
     */
    public List<User> findAll() throws SQLException {
        List<User> liste = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY nom, prenom";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(extraireUtilisateur(rs));
            }
        }
        return liste;
    }

    /**
     * Reconstitue une instance spécialisée de Dentiste ou Assistant
     * à partir de la ligne courante du ResultSet.
     */
    private User extraireUtilisateur(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String cin = rs.getString("cin");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String tel = rs.getString("telephone");
        String login = rs.getString("login");
        String mdp = rs.getString("mot_de_passe");
        String role = rs.getString("role");

        if ("DENTISTE".equalsIgnoreCase(role)) {
            String specialite = rs.getString("specialite");
            String numeroOrdre = rs.getString("numero_ordre");
            return new Dentiste(id, cin, nom, prenom, email, tel, login, mdp, specialite, numeroOrdre);
        } else {
            String dateEmbauche = rs.getString("date_embauche");
            return new Assistant(id, cin, nom, prenom, email, tel, login, mdp, dateEmbauche);
        }
    }
}
