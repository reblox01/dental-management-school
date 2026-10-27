package dental.dao;

import dental.database.DatabaseManager;
import dental.model.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour l'entité Patient.
 * Assure la persistance complète dans SQLite avec des requêtes paramétrées (PreparedStatement).
 */
public class PatientDAO {

    /**
     * Insère un nouveau patient en base de données.
     */
    public Patient create(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (cin, nom, prenom, sexe, age, telephone, email, adresse, mutuelle, antecedents) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            remplirPreparedStatement(ps, patient);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
        }
        return patient;
    }

    /**
     * Met à jour les informations d'un patient existant.
     */
    public boolean update(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET cin = ?, nom = ?, prenom = ?, sexe = ?, age = ?, "
                   + "telephone = ?, email = ?, adresse = ?, mutuelle = ?, antecedents = ? "
                   + "WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            remplirPreparedStatement(ps, patient);
            ps.setInt(11, patient.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Supprime un patient par son identifiant unique.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Recherche un patient par son id.
     */
    public Patient findById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extrairePatient(rs);
                }
            }
        }
        return null;
    }

    /**
     * Recherche un patient par son code CIN unique.
     */
    public Patient findByCin(String cin) throws SQLException {
        String sql = "SELECT * FROM patients WHERE LOWER(cin) = LOWER(?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cin);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extrairePatient(rs);
                }
            }
        }
        return null;
    }

    /**
     * Récupère la liste complète des patients ordonnée par nom et prénom.
     */
    public List<Patient> findAll() throws SQLException {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY nom, prenom";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(extrairePatient(rs));
            }
        }
        return liste;
    }

    /**
     * Recherche textuelle multi-champs (Nom, Prénom, CIN, Téléphone).
     */
    public List<Patient> search(String keyword) throws SQLException {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? "
                   + "OR LOWER(cin) LIKE ? OR telephone LIKE ? ORDER BY nom, prenom";

        String motif = "%" + (keyword != null ? keyword.trim().toLowerCase() : "") + "%";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, motif);
            ps.setString(2, motif);
            ps.setString(3, motif);
            ps.setString(4, motif);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(extrairePatient(rs));
                }
            }
        }
        return liste;
    }

    private void remplirPreparedStatement(PreparedStatement ps, Patient patient) throws SQLException {
        ps.setString(1, patient.getCin());
        ps.setString(2, patient.getNom());
        ps.setString(3, patient.getPrenom());
        ps.setString(4, patient.getSexe());
        ps.setInt(5, patient.getAge());
        ps.setString(6, patient.getTelephone());
        ps.setString(7, patient.getEmail());
        ps.setString(8, patient.getAdresse());
        ps.setString(9, patient.getMutuelle());
        ps.setString(10, patient.getAntecedents());
    }

    private Patient extrairePatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("id"),
                rs.getString("cin"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("sexe"),
                rs.getInt("age"),
                rs.getString("telephone"),
                rs.getString("email"),
                rs.getString("adresse"),
                rs.getString("mutuelle"),
                rs.getString("antecedents")
        );
    }
}
