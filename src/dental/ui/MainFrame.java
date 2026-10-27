package dental.ui;

import dental.model.User;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre principale de l'application Dental Management.
 * Illustre l'utilisation concrète des méthodes abstraites :
 * - decrireRole() polymorphique
 * - aPrivilege(String) pour contrôler les actions disponibles selon le rôle
 */
public class MainFrame extends JFrame {

    private final User currentUser;

    public MainFrame(User currentUser) {
        super("Cabinet Dentaire - Tableau de Bord (" + (currentUser != null ? currentUser.getRole() : "") + ")");
        this.currentUser = currentUser;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
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

        // Panneau central de démonstration
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        centerPanel.setOpaque(false);

        // Carte d'information du rôle et privilèges
        JPanel cardInfo = UIUtils.createCardPanel();
        cardInfo.setLayout(new BorderLayout(15, 15));

        JLabel lblCardTitle = new JLabel("Gestion des Droits & Privilèges (Classe Abstraite 'User')");
        lblCardTitle.setFont(UIUtils.FONT_SUBTITLE);
        lblCardTitle.setForeground(UIUtils.COLOR_PRIMARY);
        cardInfo.add(lblCardTitle, BorderLayout.NORTH);

        JPanel privPanel = new JPanel(new GridLayout(4, 2, 12, 10));
        privPanel.setOpaque(false);

        ajouterLignePrivilege(privPanel, "Consulter les dossiers patients", "CONSULTER_PATIENTS");
        ajouterLignePrivilege(privPanel, "Ajouter un nouveau patient", "AJOUTER_PATIENT");
        ajouterLignePrivilege(privPanel, "Modifier un dossier patient", "MODIFIER_PATIENT");
        ajouterLignePrivilege(privPanel, "Supprimer un patient (Droit critique)", "SUPPRIMER_PATIENT");

        cardInfo.add(privPanel, BorderLayout.CENTER);
        centerPanel.add(cardInfo);

        add(centerPanel, BorderLayout.CENTER);

        // Pied de page
        JLabel lblFooter = new JLabel("Dental Management - Étape 2 : Classe Abstraite (Méthodes abstraites polymorphiques)", SwingConstants.CENTER);
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblFooter.setBorder(new EmptyBorder(8, 10, 10, 10));
        add(lblFooter, BorderLayout.SOUTH);
    }

    private void ajouterLignePrivilege(JPanel panel, String libelle, String codePrivilege) {
        JLabel lbl = new JLabel(libelle);
        lbl.setFont(UIUtils.FONT_BODY);

        boolean autorise = currentUser != null && currentUser.aPrivilege(codePrivilege);
        JLabel badge = new JLabel(autorise ? " [ AUTORISÉ ] " : " [ REFUSÉ ] ");
        badge.setFont(UIUtils.FONT_BODY_BOLD);
        badge.setForeground(autorise ? UIUtils.COLOR_ACCENT : UIUtils.COLOR_DANGER);

        panel.add(lbl);
        panel.add(badge);
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
}
