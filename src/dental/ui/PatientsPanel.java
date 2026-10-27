package dental.ui;

import dental.exception.DentalException;
import dental.model.Patient;
import dental.model.User;
import dental.service.PatientService;
import java.awt.*;
import java.util.List;
import java.util.function.Predicate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Panneau principal de gestion des patients.
 * Intègre :
 * - Le contrôle d'accès par privilèges (Step 2 - Classe Abstraite)
 * - La validation fonctionnelle lors de l'édition (Step 5 - Interface Fonctionnelle)
 */
public class PatientsPanel extends JPanel {

    private final User currentUser;
    private final PatientService patientService;

    private JTable tablePatients;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnActualiser;
    private JLabel lblCompteur;

    private List<Patient> listeCourante;

    public PatientsPanel(User currentUser, PatientService patientService) {
        this.currentUser = currentUser;
        this.patientService = patientService;
        initUI();
        chargerDonnees();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // Barre d'outils supérieure (Recherche + Actions)
        JPanel topToolbar = new JPanel(new BorderLayout(15, 10));
        topToolbar.setOpaque(false);

        // Zone de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);

        JLabel lblRecherche = new JLabel("Recherche :");
        lblRecherche.setFont(UIUtils.FONT_BODY_BOLD);
        txtRecherche = UIUtils.createStyledTextField(18);
        txtRecherche.setToolTipText("Recherche par Nom, Prénom, CIN ou Téléphone");

        JButton btnRechercher = UIUtils.createStyledButton("Chercher", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnRechercher.addActionListener(e -> onRechercher());

        searchPanel.add(lblRecherche);
        searchPanel.add(txtRecherche);
        searchPanel.add(btnRechercher);
        topToolbar.add(searchPanel, BorderLayout.WEST);

        // Zone des boutons d'actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsPanel.setOpaque(false);

        btnAjouter = UIUtils.createStyledButton("+ Nouveau Patient", UIUtils.COLOR_ACCENT, Color.WHITE);
        btnModifier = UIUtils.createStyledButton("Modifier", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSupprimer = UIUtils.createStyledButton("Supprimer", UIUtils.COLOR_DANGER, Color.WHITE);
        btnActualiser = UIUtils.createStyledButton("Actualiser", UIUtils.COLOR_TEXT_MUTED, Color.WHITE);

        // Contrôle des droits selon les privilèges de l'utilisateur connecté (Step 2)
        boolean droitSuppression = currentUser != null && currentUser.aPrivilege("SUPPRIMER_PATIENT");
        btnSupprimer.setEnabled(droitSuppression);
        if (!droitSuppression) {
            btnSupprimer.setToolTipText("Droit de suppression réservé aux Dentistes.");
        }

        JButton btnStats = UIUtils.createStyledButton("Synthèse & Stats", UIUtils.COLOR_ACCENT, Color.WHITE);
        btnStats.addActionListener(e -> onAfficherStats());

        btnAjouter.addActionListener(e -> onAjouter());
        btnModifier.addActionListener(e -> onModifier());
        btnSupprimer.addActionListener(e -> onSupprimer());
        btnActualiser.addActionListener(e -> chargerDonnees());

        actionsPanel.add(btnStats);
        actionsPanel.add(btnAjouter);
        actionsPanel.add(btnModifier);
        actionsPanel.add(btnSupprimer);
        actionsPanel.add(btnActualiser);
        topToolbar.add(actionsPanel, BorderLayout.EAST);

        // Barre des filtres par Predicate<T> (Step 6)
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.setOpaque(false);
        JLabel lblFiltres = new JLabel("Filtres rapides (Predicate<T>) :");
        lblFiltres.setFont(UIUtils.FONT_SMALL);
        lblFiltres.setForeground(UIUtils.COLOR_TEXT_MUTED);
        filterBar.add(lblFiltres);

        ajouterBoutonFiltre(filterBar, "Tous", p -> true);
        ajouterBoutonFiltre(filterBar, "Seniors (>= 60 ans)", p -> p.getAge() >= 60);
        ajouterBoutonFiltre(filterBar, "Mineurs (< 18 ans)", p -> p.getAge() < 18);
        ajouterBoutonFiltre(filterBar, "Avec Mutuelle", p -> p.getMutuelle() != null && !p.getMutuelle().equalsIgnoreCase("Aucune"));
        ajouterBoutonFiltre(filterBar, "Femmes", p -> "F".equalsIgnoreCase(p.getSexe()));
        ajouterBoutonFiltre(filterBar, "Hommes", p -> "M".equalsIgnoreCase(p.getSexe()));

        // Tri avec Comparator<Patient> (Step 7 - Lambdas & Références de Méthodes)
        JLabel lblTri = new JLabel("  |  Trier (Comparator & ::) :");
        lblTri.setFont(UIUtils.FONT_SMALL);
        lblTri.setForeground(UIUtils.COLOR_TEXT_MUTED);
        filterBar.add(lblTri);

        JComboBox<String> cbTri = new JComboBox<>(new String[]{
            "Par défaut",
            "Nom (A -> Z)",
            "Nom (Z -> A)",
            "Âge (Croissant)",
            "Âge (Décroissant)",
            "CIN"
        });
        cbTri.setFont(UIUtils.FONT_SMALL);
        cbTri.setBackground(Color.WHITE);
        // Lambda pour la gestion de l'événement de tri
        cbTri.addActionListener(e -> onChangerTri((String) cbTri.getSelectedItem()));
        filterBar.add(cbTri);

        // Recherche dynamique en direct via DocumentListener (mise à jour en temps réel)
        txtRecherche.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { onRechercher(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { onRechercher(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { onRechercher(); }
        });

        JPanel northContainer = new JPanel(new BorderLayout(0, 8));
        northContainer.setOpaque(false);
        northContainer.add(topToolbar, BorderLayout.NORTH);
        northContainer.add(filterBar, BorderLayout.SOUTH);

        add(northContainer, BorderLayout.NORTH);

        // Table des patients stylisée
        String[] colonnes = {"ID", "CIN", "Nom", "Prénom", "Sexe", "Âge", "Téléphone", "Email", "Mutuelle"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table en lecture seule directe (édition via boîte de dialogue)
            }
        };

        tablePatients = new JTable(tableModel);
        tablePatients.setFont(UIUtils.FONT_BODY);
        tablePatients.setRowHeight(28);
        tablePatients.getTableHeader().setFont(UIUtils.FONT_BODY_BOLD);
        tablePatients.getTableHeader().setBackground(UIUtils.COLOR_PRIMARY);
        tablePatients.getTableHeader().setForeground(Color.WHITE);
        tablePatients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double-clic pour modifier
        tablePatients.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tablePatients.getSelectedRow() != -1) {
                    onModifier();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablePatients);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Barre d'état inférieure
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(5, 5, 0, 5));

        lblCompteur = new JLabel("Total : 0 patient(s)");
        lblCompteur.setFont(UIUtils.FONT_BODY_BOLD);
        lblCompteur.setForeground(UIUtils.COLOR_TEXT_MUTED);
        bottomBar.add(lblCompteur, BorderLayout.WEST);

        JLabel lblAstuce = new JLabel("Double-clic sur une ligne pour modifier le dossier", SwingConstants.RIGHT);
        lblAstuce.setFont(UIUtils.FONT_SMALL);
        lblAstuce.setForeground(UIUtils.COLOR_TEXT_MUTED);
        bottomBar.add(lblAstuce, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public void chargerDonnees() {
        try {
            listeCourante = patientService.obtenirTousLesPatients();
            mettreAJourTable(listeCourante);
            txtRecherche.setText("");
        } catch (DentalException e) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de charger les patients : " + e.getMessage(),
                    "Erreur de Chargement",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRechercher() {
        String motCle = txtRecherche.getText().trim();
        try {
            listeCourante = patientService.rechercherPatients(motCle);
            mettreAJourTable(listeCourante);
        } catch (DentalException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la recherche : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mettreAJourTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        if (patients != null) {
            for (Patient p : patients) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getCin(),
                    p.getNom(),
                    p.getPrenom(),
                    p.getSexe(),
                    p.getAge() + " ans",
                    p.getTelephone() != null ? p.getTelephone() : "-",
                    p.getEmail() != null ? p.getEmail() : "-",
                    p.getMutuelle() != null ? p.getMutuelle() : "Aucune"
                });
            }
            lblCompteur.setText("Total : " + patients.size() + " patient(s) enregistré(s)");
        } else {
            lblCompteur.setText("Total : 0 patient");
        }
    }

    private void onAjouter() {
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PatientDialog dialog = new PatientDialog(topFrame, patientService, null);
        dialog.setVisible(true);

        if (dialog.isSauvegardeReussie()) {
            chargerDonnees();
        }
    }

    private void onModifier() {
        int selectedRow = tablePatients.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un patient dans la table.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient patientSelectionne = listeCourante.get(selectedRow);
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PatientDialog dialog = new PatientDialog(topFrame, patientService, patientSelectionne);
        dialog.setVisible(true);

        if (dialog.isSauvegardeReussie()) {
            chargerDonnees();
        }
    }

    private void onSupprimer() {
        // Double vérification des droits (Step 2 - Classe Abstraite)
        if (currentUser == null || !currentUser.aPrivilege("SUPPRIMER_PATIENT")) {
            JOptionPane.showMessageDialog(this,
                    "Action refusée : Seul un Chirurgien-Dentiste a le privilège de supprimer un dossier patient.",
                    "Accès Refusé",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = tablePatients.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un patient à supprimer.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient patient = listeCourante.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous certain de vouloir supprimer définitivement le dossier du patient :\n"
                + patient.getNomComplet() + " (CIN: " + patient.getCin() + ") ?\n\n"
                + "Cette action est irréversible !",
                "Confirmation de Suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                patientService.supprimerPatient(patient.getId());
                JOptionPane.showMessageDialog(this,
                        "Patient supprimé avec succès.",
                        "Suppression Effectuée",
                        JOptionPane.INFORMATION_MESSAGE);
                chargerDonnees();
            } catch (DentalException e) {
                JOptionPane.showMessageDialog(this,
                        "Échec de la suppression : " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ajouterBoutonFiltre(JPanel container, String titre, Predicate<Patient> predicate) {
        JButton btn = new JButton(titre);
        btn.setFont(UIUtils.FONT_SMALL);
        btn.setBackground(Color.WHITE);
        btn.setForeground(UIUtils.COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        // Utilisation de Predicate<Patient> pour filtrer
        btn.addActionListener(e -> {
            try {
                listeCourante = patientService.filtrerPatients(predicate);
                mettreAJourTable(listeCourante);
            } catch (DentalException ex) {
                JOptionPane.showMessageDialog(this, "Erreur de filtrage : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        container.add(btn);
    }

    private void onAfficherStats() {
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        StatsDialog dialog = new StatsDialog(topFrame, patientService);
        dialog.setVisible(true);
    }

    /**
     * Trie la liste courante selon le critère choisi en appliquant des lambdas et références de méthodes.
     */
    private void onChangerTri(String critere) {
        if (listeCourante == null || listeCourante.isEmpty() || critere == null) {
            return;
        }

        switch (critere) {
            case "Nom (A -> Z)":
                // Utilisation de la référence de méthode : Patient::getNom
                patientService.trierPatients(listeCourante, PatientService.COMPARATEUR_NOM_ASC);
                break;
            case "Nom (Z -> A)":
                // Utilisation du comparateur inversé via .reversed()
                patientService.trierPatients(listeCourante, PatientService.COMPARATEUR_NOM_DESC);
                break;
            case "Âge (Croissant)":
                // Utilisation d'une lambda expression : (p1, p2) -> ...
                patientService.trierPatients(listeCourante, PatientService.COMPARATEUR_AGE_ASC);
                break;
            case "Âge (Décroissant)":
                // Utilisation d'une lambda expression inversée
                patientService.trierPatients(listeCourante, PatientService.COMPARATEUR_AGE_DESC);
                break;
            case "CIN":
                // Utilisation de la référence de méthode : Patient::getCin
                patientService.trierPatients(listeCourante, PatientService.COMPARATEUR_CIN);
                break;
            default:
                break;
        }
        mettreAJourTable(listeCourante);
    }

    public List<Patient> getListeCourante() {
        return listeCourante;
    }
}
