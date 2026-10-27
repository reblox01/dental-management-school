package dental;

import dental.database.DatabaseManager;
import dental.ui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Point d'entrée principal de l'application Dental Management.
 * Projet pédagogique développé en Java Standard pur avec Java Swing (aucun framework).
 */
public class Main {

    public static void main(String[] args) {
        // Initialisation de la base de données relationnelle SQLite
        DatabaseManager.initialiserBaseDeDonnees();

        // Appliquer le look & feel natif du système d'exploitation si disponible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Repli sur le look & feel Swing par défaut
        }

        // Lancement sur l'Event Dispatch Thread (EDT) de Swing
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
