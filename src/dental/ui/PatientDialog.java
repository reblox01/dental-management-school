package dental.ui;

import dental.exception.ValidationException;
import dental.model.Patient;
import dental.service.PatientService;
import dental.util.InputSanitizer;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Boîte de dialogue modale pour l'ajout ou la modification d'un patient.
 * Exécute la validation fonctionnelle (Step 5) lors de la validation du formulaire.
 */
public class PatientDialog extends JDialog {

    private final PatientService patientService;
    private final Patient patientExistant; // null en mode création, non-null en mode édition
    private boolean sauvegardeReussie = false;

    private JTextField txtCin;
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JComboBox<String> cbSexe;
    private JSpinner spAge;
    private JTextField txtTelephone;
    private JTextField txtEmail;
    private JTextField txtAdresse;
    private JComboBox<String> cbMutuelle;
    private JTextField txtAntecedents;

    public PatientDialog(Frame parent, PatientService patientService, Patient patientExistant) {
        super(parent, patientExistant == null ? "Nouveau Patient" : "Modifier Dossier Patient", true);
        this.patientService = patientService;
        this.patientExistant = patientExistant;
        initUI();
        if (patientExistant != null) {
            remplirChamps(patientExistant);
        }
    }

    private void initUI() {
        setSize(520, 560);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // En-tête
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(UIUtils.COLOR_PRIMARY);
        JLabel lblTitre = new JLabel(patientExistant == null ? "Création d'un dossier patient" : "Mise à jour du dossier patient");
        lblTitre.setFont(UIUtils.FONT_SUBTITLE);
        lblTitre.setForeground(Color.WHITE);
        header.add(lblTitre);
        add(header, BorderLayout.NORTH);

        // Formulaire
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 25, 15, 25));
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCin = UIUtils.createStyledTextField(15);
        txtNom = UIUtils.createStyledTextField(15);
        txtPrenom = UIUtils.createStyledTextField(15);
        cbSexe = new JComboBox<>(new String[]{"M", "F"});
        cbSexe.setFont(UIUtils.FONT_BODY);

        spAge = new JSpinner(new SpinnerNumberModel(30, 0, 125, 1));
        spAge.setFont(UIUtils.FONT_BODY);

        txtTelephone = UIUtils.createStyledTextField(15);
        txtEmail = UIUtils.createStyledTextField(15);
        txtAdresse = UIUtils.createStyledTextField(15);

        cbMutuelle = new JComboBox<>(new String[]{"CNSS", "CNOPS", "AXA", "RMA Watanya", "Saham", "Aucune"});
        cbMutuelle.setFont(UIUtils.FONT_BODY);

        txtAntecedents = UIUtils.createStyledTextField(15);
        txtAntecedents.setToolTipText("Ex: Diabète, Allergies, Hypertension...");

        int y = 0;
        ajouterLigneFormulaire(form, gbc, y++, "CIN (Ex: AB123456) * :", txtCin);
        ajouterLigneFormulaire(form, gbc, y++, "Nom * :", txtNom);
        ajouterLigneFormulaire(form, gbc, y++, "Prénom * :", txtPrenom);
        ajouterLigneFormulaire(form, gbc, y++, "Sexe :", cbSexe);
        ajouterLigneFormulaire(form, gbc, y++, "Âge :", spAge);
        ajouterLigneFormulaire(form, gbc, y++, "Téléphone (Ex: 0612345678) :", txtTelephone);
        ajouterLigneFormulaire(form, gbc, y++, "Email :", txtEmail);
        ajouterLigneFormulaire(form, gbc, y++, "Adresse :", txtAdresse);
        ajouterLigneFormulaire(form, gbc, y++, "Mutuelle :", cbMutuelle);
        ajouterLigneFormulaire(form, gbc, y++, "Antécédents médicaux :", txtAntecedents);

        add(form, BorderLayout.CENTER);

        // Boutons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtils.COLOR_BORDER));

        JButton btnAnnuler = UIUtils.createStyledButton("Annuler", UIUtils.COLOR_TEXT_MUTED, Color.WHITE);
        JButton btnEnregistrer = UIUtils.createStyledButton("Enregistrer", UIUtils.COLOR_ACCENT, Color.WHITE);

        btnAnnuler.addActionListener(e -> dispose());
        btnEnregistrer.addActionListener(e -> onEnregistrer());

        footer.add(btnAnnuler);
        footer.add(btnEnregistrer);
        add(footer, BorderLayout.SOUTH);
    }

    private void ajouterLigneFormulaire(JPanel form, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UIUtils.FONT_BODY_BOLD);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        form.add(component, gbc);
    }

    private void remplirChamps(Patient p) {
        txtCin.setText(p.getCin());
        txtNom.setText(p.getNom());
        txtPrenom.setText(p.getPrenom());
        cbSexe.setSelectedItem(p.getSexe());
        spAge.setValue(p.getAge());
        txtTelephone.setText(p.getTelephone() != null ? p.getTelephone() : "");
        txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
        txtAdresse.setText(p.getAdresse() != null ? p.getAdresse() : "");
        cbMutuelle.setSelectedItem(p.getMutuelle());
        txtAntecedents.setText(p.getAntecedents() != null ? p.getAntecedents() : "");
    }

    private void onEnregistrer() {
        // Step 8 — Sanitisation des entrées (@securly) avant toute validation métier
        String cin = InputSanitizer.nettoyer(txtCin.getText(), 20);
        String nom = InputSanitizer.nettoyer(txtNom.getText(), 50);
        String prenom = InputSanitizer.nettoyer(txtPrenom.getText(), 50);
        String sexe = (String) cbSexe.getSelectedItem(); // liste déroulante : valeur contrôlée
        int age = (Integer) spAge.getValue();
        String tel = InputSanitizer.nettoyer(txtTelephone.getText(), 20);
        String email = InputSanitizer.nettoyer(txtEmail.getText(), 100);
        String adresse = InputSanitizer.nettoyer(txtAdresse.getText(), 200);
        String mutuelle = (String) cbMutuelle.getSelectedItem(); // liste déroulante : valeur contrôlée
        String antecedents = InputSanitizer.nettoyer(txtAntecedents.getText(), 300);

        // Détection préventive d'injection (défense en profondeur, même logique que LoginFrame)
        String[] champsSensibles = {cin, nom, prenom, tel, email, adresse, antecedents};
        for (String champ : champsSensibles) {
            if (InputSanitizer.contientInjection(champ)) {
                JOptionPane.showMessageDialog(this,
                        "Caractères suspects détectés dans le formulaire.\nSaisie rejetée pour des raisons de sécurité.",
                        "Alerte de Sécurité",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Patient p = (patientExistant == null)
                ? new Patient(cin, nom, prenom, sexe, age, tel, email, adresse, mutuelle, antecedents)
                : patientExistant;

        if (patientExistant != null) {
            p.setCin(cin);
            p.setNom(nom);
            p.setPrenom(prenom);
            p.setSexe(sexe);
            p.setAge(age);
            p.setTelephone(tel);
            p.setEmail(email);
            p.setAdresse(adresse);
            p.setMutuelle(mutuelle);
            p.setAntecedents(antecedents);
        }

        try {
            if (patientExistant == null) {
                patientService.ajouterPatient(p);
                JOptionPane.showMessageDialog(this,
                        "Patient enregistré avec succès (Validations fonctionnelles réussies) !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                patientService.modifierPatient(p);
                JOptionPane.showMessageDialog(this,
                        "Dossier patient mis à jour avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            sauvegardeReussie = true;
            dispose();

        } catch (ValidationException e) {
            // Capture spécifique de l'exception levée par les interfaces fonctionnelles Validateur<T>
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Erreur de Validation Fonctionnelle",
                    JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + e.getMessage(),
                    "Erreur Système",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSauvegardeReussie() {
        return sauvegardeReussie;
    }
}
