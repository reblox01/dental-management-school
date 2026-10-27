package dental.database;

import dental.util.SecurityUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestionnaire de base de données relationnelle SQLite.
 * Implémente :
 * - Le chargement du pilote JDBC SQLite
 * - La création automatique des tables si elles n'existent pas
 * - L'initialisation des utilisateurs et patients de test (Seed)
 * - L'utilisation systématique de requêtes préparées (sécurité contre injection SQL)
 */
public final class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:dental.db";

    static {
        // Enregistrement explicite du pilote JDBC SQLite
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR : Pilote org.sqlite.JDBC introuvable dans le classpath !");
            e.printStackTrace();
        }
    }

    private DatabaseManager() {
    }

    /**
     * Ouvre et retourne une nouvelle connexion vers la base de données SQLite.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initialise la structure de la base de données (Tables et données initiales).
     */
    public static void initialiserBaseDeDonnees() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Table des utilisateurs (héritage stocké en table unique avec colonne 'role')
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "cin TEXT NOT NULL UNIQUE, "
                    + "nom TEXT NOT NULL, "
                    + "prenom TEXT NOT NULL, "
                    + "email TEXT, "
                    + "telephone TEXT, "
                    + "login TEXT NOT NULL UNIQUE, "
                    + "mot_de_passe TEXT NOT NULL, "
                    + "role TEXT NOT NULL, "
                    + "specialite TEXT, "
                    + "numero_ordre TEXT, "
                    + "date_embauche TEXT"
                    + ");");

            // Table des patients
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "cin TEXT NOT NULL UNIQUE, "
                    + "nom TEXT NOT NULL, "
                    + "prenom TEXT NOT NULL, "
                    + "sexe TEXT NOT NULL, "
                    + "age INTEGER NOT NULL, "
                    + "telephone TEXT, "
                    + "email TEXT, "
                    + "adresse TEXT, "
                    + "mutuelle TEXT, "
                    + "antecedents TEXT"
                    + ");");

            // Données initiales pour les utilisateurs si la table est vide
            seederUtilisateurs(conn);

            // Données initiales pour les patients si la table est vide
            seederPatients(conn);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base SQLite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seederUtilisateurs(Connection conn) throws SQLException {
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) AS total FROM users")) {
            if (rs.next() && rs.getInt("total") == 0) {
                String sql = "INSERT INTO users (cin, nom, prenom, email, telephone, login, mot_de_passe, role, specialite, numero_ordre, date_embauche) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    // 1. Compte Dentiste (mot de passe initial : 'admin123', hashé SHA-256)
                    ps.setString(1, "AB123456");
                    ps.setString(2, "El Amrani");
                    ps.setString(3, "Karim");
                    ps.setString(4, "karim.amrani@cabinet.ma");
                    ps.setString(5, "0611223344");
                    ps.setString(6, "dentiste");
                    ps.setString(7, SecurityUtils.hacherMotDePasse("admin123"));
                    ps.setString(8, "DENTISTE");
                    ps.setString(9, "Chirurgie Dentaire & Implantologie");
                    ps.setString(10, "ORD-98745");
                    ps.setString(11, null);
                    ps.executeUpdate();

                    // 2. Compte Assistant (mot de passe initial : 'pass123', hashé SHA-256)
                    ps.setString(1, "CD789012");
                    ps.setString(2, "Bennani");
                    ps.setString(3, "Salma");
                    ps.setString(4, "salma.bennani@cabinet.ma");
                    ps.setString(5, "0655667788");
                    ps.setString(6, "assistant");
                    ps.setString(7, SecurityUtils.hacherMotDePasse("pass123"));
                    ps.setString(8, "ASSISTANT");
                    ps.setString(9, null);
                    ps.setString(10, null);
                    ps.setString(11, "2022-09-01");
                    ps.executeUpdate();
                }
            }
        }
    }

    private static void seederPatients(Connection conn) throws SQLException {
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) AS total FROM patients")) {
            if (rs.next() && rs.getInt("total") == 0) {
                String sql = "INSERT INTO patients (cin, nom, prenom, sexe, age, telephone, email, adresse, mutuelle, antecedents) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    Object[][] patients = {
                        {"BK654321", "Alami", "Yassine", "M", 34, "0612345678", "yassine.alami@email.com", "Rabat Agdal", "CNSS", "Aucun"},
                        {"BJ987654", "Chraibi", "Houda", "F", 28, "0678901234", "houda.chraibi@email.com", "Casablanca Maarif", "CNOPS", "Allergie Pénicilline"},
                        {"BE112233", "Tazi", "Omar", "M", 62, "0661122334", "omar.tazi@email.com", "Fès Centre", "AXA", "Diabète type 2, Hypertension"},
                        {"BL445566", "Idrissi", "Khadija", "F", 45, "0622334455", "khadija.idrissi@email.com", "Marrakech Guéliz", "CNSS", "Aucun"}
                    };

                    for (Object[] p : patients) {
                        ps.setString(1, (String) p[0]);
                        ps.setString(2, (String) p[1]);
                        ps.setString(3, (String) p[2]);
                        ps.setString(4, (String) p[3]);
                        ps.setInt(5, (Integer) p[4]);
                        ps.setString(6, (String) p[5]);
                        ps.setString(7, (String) p[6]);
                        ps.setString(8, (String) p[7]);
                        ps.setString(9, (String) p[8]);
                        ps.setString(10, (String) p[9]);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }
}
