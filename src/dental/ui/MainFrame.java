package dental.ui;

import dental.model.User;
import dental.service.PatientService;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre principale de l'application Dental Management.
 * Intègre :
 * - Le bandeau utilisateur avec méthodes abstraites (Step 2)
 * - Le panneau de gestion des dossiers patients (Step 5 - Interface Fonctionnelle)
 */
public class MainFrame extends JFrame {

    private final User currentUser;
    private final PatientService patientService;
    private PatientsPanel patientsPanel;

    public MainFrame(User currentUser) {
        super("Cabinet Dentaire - Tableau de Bord (" + (currentUser != null ? currentUser.getRole() : "") + ")");
        this.currentUser = currentUser;
        this.patientService = new PatientService();
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // Barre supérieure (Header)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_PRIMARY);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JPanel userIntro = new JPanel(new GridLayout(2, 1, 0, 3));
        userIntro.setOpaque(false);

        JLabel lblName = new JLabel("Connecté : " + (currentUser != null ? currentUser.getNomComplet() : "Invité"));
        lblName.setFont(UIUtils.FONT_SUBTITLE);
        lblName.setForeground(Color.WHITE);

        // Appel polymorphique à la méthode abstraite decrireRole()
        JLabel lblRoleDesc = new JLabel(currentUser != null ? currentUser.decrireRole() : "");
        lblRoleDesc.setFont(UIUtils.FONT_SMALL);
        lblRoleDesc.setForeground(new Color(220, 235, 250));

        userIntro.add(lblName);
        userIntro.add(lblRoleDesc);
        topBar.add(userIntro, BorderLayout.CENTER);

        JButton btnDeconnexion = UIUtils.createStyledButton("Déconnexion", UIUtils.COLOR_DANGER, Color.WHITE);
        btnDeconnexion.addActionListener(e -> onDeconnexion());
        topBar.add(btnDeconnexion, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Panneau central : Gestion des patients
        patientsPanel = new PatientsPanel(currentUser, patientService);
        add(patientsPanel, BorderLayout.CENTER);

        // Pied de page
        JLabel lblFooter = new JLabel("Dental Management - Étape 5 : Interface Fonctionnelle (Validateur<T> & andThen())", SwingConstants.CENTER);
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblFooter.setBorder(new EmptyBorder(8, 10, 10, 10));
        add(lblFooter, BorderLayout.SOUTH);
    }

    private void onDeconnexion() {
        int reponse = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Confirmation de déconnexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (reponse == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public PatientsPanel getPatientsPanel() {
        return patientsPanel;
    }
}
