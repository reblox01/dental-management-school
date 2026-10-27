package dental.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre de connexion principale en Java Swing pur (sans framework).
 */
public class LoginFrame extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JButton btnQuitter;

    public LoginFrame() {
        super("Cabinet Dentaire - Connexion");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // En-tête
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIUtils.COLOR_PRIMARY);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Cabinet Dentaire");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Système de Gestion des Soins & Patients");
        lblSub.setFont(UIUtils.FONT_SMALL);
        lblSub.setForeground(new Color(200, 220, 240));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSub);
        add(headerPanel, BorderLayout.NORTH);

        // Corps du formulaire (Carte)
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogin = new JLabel("Identifiant / Nom d'utilisateur :");
        lblLogin.setFont(UIUtils.FONT_BODY_BOLD);
        lblLogin.setForeground(UIUtils.COLOR_TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formCard.add(lblLogin, gbc);

        txtLogin = UIUtils.createStyledTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formCard.add(txtLogin, gbc);

        JLabel lblPass = new JLabel("Mot de passe :");
        lblPass.setFont(UIUtils.FONT_BODY_BOLD);
        lblPass.setForeground(UIUtils.COLOR_TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formCard.add(lblPass, gbc);

        txtPassword = UIUtils.createStyledPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formCard.add(txtPassword, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        btnQuitter = UIUtils.createStyledButton("Quitter", UIUtils.COLOR_TEXT_MUTED, Color.WHITE);
        btnConnexion = UIUtils.createStyledButton("Se connecter", UIUtils.COLOR_PRIMARY, Color.WHITE);

        buttonPanel.add(btnQuitter);
        buttonPanel.add(btnConnexion);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 8, 8, 8);
        formCard.add(buttonPanel, gbc);

        // Conteneur centré pour la carte
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerWrapper.setOpaque(false);
        centerWrapper.add(formCard);
        add(centerWrapper, BorderLayout.CENTER);

        // Pied de page
        JLabel lblFooter = new JLabel("Projet Académique - Java Standard & Swing", SwingConstants.CENTER);
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblFooter.setBorder(new EmptyBorder(10, 10, 15, 10));
        add(lblFooter, BorderLayout.SOUTH);

        // Actions
        btnQuitter.addActionListener(e -> System.exit(0));
        btnConnexion.addActionListener(e -> onConnexion());

        // Touche Entrée pour valider
        getRootPane().setDefaultButton(btnConnexion);
    }

    private void onConnexion() {
        String login = txtLogin.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (login.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez renseigner tous les champs.",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simule l'authentification de base (sera connectée aux étapes suivantes)
        JOptionPane.showMessageDialog(this,
                "Bienvenue " + login + " !\n(Authentification de base initialisée)",
                "Connexion réussie",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public JTextField getTxtLogin() {
        return txtLogin;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnConnexion() {
        return btnConnexion;
    }
}
