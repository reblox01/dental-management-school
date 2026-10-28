package dental.ui;

import dental.exception.ChampObligatoireException;
import dental.exception.MotDePasseInvalideException;
import dental.exception.UtilisateurIntrouvableException;
import dental.model.User;
import dental.service.AuthService;
import dental.util.InputSanitizer;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre de connexion principale en Java Swing pur (sans framework).
 * Étape 1 : Connectée à AuthService pour valider l'héritage (User -> Dentiste / Assistant).
 */
public class LoginFrame extends JFrame {

    private final AuthService authService;

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JButton btnQuitter;

    public LoginFrame() {
        super("Cabinet Dentaire - Connexion");
        this.authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 440);
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

        JLabel lblLogin = new JLabel("Identifiant (ex: dentiste ou assistant) :");
        lblLogin.setFont(UIUtils.FONT_BODY_BOLD);
        lblLogin.setForeground(UIUtils.COLOR_TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formCard.add(lblLogin, gbc);

        txtLogin = UIUtils.createStyledTextField(20);
        txtLogin.setText("dentiste");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formCard.add(txtLogin, gbc);

        JLabel lblPass = new JLabel("Mot de passe (ex: admin123 ou pass123) :");
        lblPass.setFont(UIUtils.FONT_BODY_BOLD);
        lblPass.setForeground(UIUtils.COLOR_TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formCard.add(lblPass, gbc);

        txtPassword = UIUtils.createStyledPasswordField(20);
        txtPassword.setText("admin123");
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
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.add(formCard);
        add(centerWrapper, BorderLayout.CENTER);

        // Pied de page
        JLabel lblFooter = new JLabel("Projet Académique - Étape 8 : Sécurité (InputSanitizer, SHA-256, PreparedStatement)", SwingConstants.CENTER);
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblFooter.setBorder(new EmptyBorder(5, 10, 12, 10));
        add(lblFooter, BorderLayout.SOUTH);

        // Actions
        btnQuitter.addActionListener(e -> System.exit(0));
        btnConnexion.addActionListener(e -> onConnexion());

        // Touche Entrée pour valider
        getRootPane().setDefaultButton(btnConnexion);
    }

    private void onConnexion() {
        // Step 8 — Sanitisation des entrées (@securly)
        String login = InputSanitizer.nettoyer(txtLogin.getText(), 50);
        String pass = new String(txtPassword.getPassword());

        // Détection préventive d'injection (défense en profondeur)
        if (InputSanitizer.contientInjection(login)) {
            JOptionPane.showMessageDialog(this,
                    "Caractères suspects détectés dans l'identifiant.\nSaisie rejetée pour des raisons de sécurité.",
                    "Alerte de Sécurité",
                    JOptionPane.ERROR_MESSAGE);
            txtLogin.setText("");
            txtLogin.requestFocus();
            return;
        }

        // Désactiver le bouton pendant le traitement pour éviter les doubles clics
        btnConnexion.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Démonstration du bloc TRY : appel d'une méthode déclarant des exceptions contrôlées
            User utilisateur = authService.authentifier(login, pass);

            JOptionPane.showMessageDialog(this,
                    "Connexion réussie !\n\n"
                    + "Bienvenue " + utilisateur.getNomComplet() + "\n"
                    + "Rôle : " + utilisateur.getRole() + "\n\n"
                    + "Description du rôle (méthode abstraite decrireRole()) :\n"
                    + utilisateur.decrireRole(),
                    "Authentification Réussie",
                    JOptionPane.INFORMATION_MESSAGE);

            // Ouvrir le tableau de bord principal
            this.dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(utilisateur).setVisible(true));

        } catch (ChampObligatoireException e) {
            // CATCH 1 : Gestion spécifique d'un champ requis manquant
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Champs Requis",
                    JOptionPane.WARNING_MESSAGE);
            if (login.isEmpty()) {
                txtLogin.requestFocus();
            } else {
                txtPassword.requestFocus();
            }

        } catch (UtilisateurIntrouvableException e) {
            // CATCH 2 : Gestion spécifique d'un identifiant inexistant
            JOptionPane.showMessageDialog(this,
                    e.getMessage() + "\n\nVérifiez l'orthographe de votre identifiant.",
                    "Utilisateur Inconnu",
                    JOptionPane.ERROR_MESSAGE);
            txtLogin.requestFocus();
            txtLogin.selectAll();

        } catch (MotDePasseInvalideException e) {
            // CATCH 3 : Gestion spécifique d'un mot de passe incorrect
            JOptionPane.showMessageDialog(this,
                    e.getMessage() + "\n\nVeuillez ressaisir votre mot de passe.",
                    "Erreur d'Authentification",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();

        } catch (Exception e) {
            // CATCH 4 : Capture générique en dernier recours (bonne pratique)
            JOptionPane.showMessageDialog(this,
                    "Une erreur inattendue est survenue : " + e.getMessage(),
                    "Erreur Système",
                    JOptionPane.ERROR_MESSAGE);

        } finally {
            // FINALLY : Toujours exécuté, avec ou sans exception (nettoyage des ressources & état)
            btnConnexion.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
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
