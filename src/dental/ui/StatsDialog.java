package dental.ui;

import dental.exception.DentalException;
import dental.model.Patient;
import dental.service.PatientService;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Boîte de dialogue de statistiques et synthèse des patients.
 * Démonstration des interfaces fonctionnelles pré-définies de java.util.function :
 * - Predicate<Patient> : comptages conditionnels (hommes, femmes, seniors, mineurs)
 * - Function<Patient, String> : transformation de chaque patient en ligne de rapport
 * - Consumer<Patient> : application d'actions de journalisation
 */
public class StatsDialog extends JDialog {

    private final PatientService patientService;
    private JTextArea txtRapport;

    public StatsDialog(Frame parent, PatientService patientService) {
        super(parent, "Statistiques & Interfaces Fonctionnelles Pré-définies", true);
        this.patientService = patientService;
        initUI();
        calculerEtAfficher();
    }

    private void initUI() {
        setSize(650, 520);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // En-tête
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(UIUtils.COLOR_PRIMARY);
        JLabel lblTitre = new JLabel("Statistiques Pédagogiques (java.util.function)");
        lblTitre.setFont(UIUtils.FONT_SUBTITLE);
        lblTitre.setForeground(Color.WHITE);
        header.add(lblTitre);
        add(header, BorderLayout.NORTH);

        // Zone de texte centrale affichant le rapport
        txtRapport = new JTextArea();
        txtRapport.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtRapport.setEditable(false);
        txtRapport.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(txtRapport);
        scrollPane.setBorder(new EmptyBorder(15, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Boutons inférieurs
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtils.COLOR_BORDER));

        JButton btnJournaliser = UIUtils.createStyledButton("Journaliser (Consumer<T>)", UIUtils.COLOR_ACCENT, Color.WHITE);
        JButton btnFermer = UIUtils.createStyledButton("Fermer", UIUtils.COLOR_PRIMARY, Color.WHITE);

        btnJournaliser.addActionListener(e -> onJournaliser());
        btnFermer.addActionListener(e -> dispose());

        footer.add(btnJournaliser);
        footer.add(btnFermer);
        add(footer, BorderLayout.SOUTH);
    }

    private void calculerEtAfficher() {
        try {
            List<Patient> tous = patientService.obtenirTousLesPatients();

            // 1. Définition d'expressions lambda compatibles avec Predicate<Patient>
            Predicate<Patient> estHomme = p -> "M".equalsIgnoreCase(p.getSexe());
            Predicate<Patient> estFemme = p -> "F".equalsIgnoreCase(p.getSexe());
            Predicate<Patient> estSenior = p -> p.getAge() >= 60;
            Predicate<Patient> estMineur = p -> p.getAge() < 18;
            Predicate<Patient> avecMutuelle = p -> p.getMutuelle() != null && !p.getMutuelle().equalsIgnoreCase("Aucune");

            // Filtrage via les méthodes de service utilisant Predicate
            int total = tous.size();
            int nbHommes = patientService.filtrerPatients(estHomme).size();
            int nbFemmes = patientService.filtrerPatients(estFemme).size();
            int nbSeniors = patientService.filtrerPatients(estSenior).size();
            int nbMineurs = patientService.filtrerPatients(estMineur).size();
            int nbAvecMutuelle = patientService.filtrerPatients(avecMutuelle).size();

            // Combinaison de Predicates avec la méthode par défaut .and(...)
            Predicate<Patient> seniorAvecMutuelle = estSenior.and(avecMutuelle);
            int nbSeniorAvecMutuelle = patientService.filtrerPatients(seniorAvecMutuelle).size();

            // Calcul de la moyenne d'âge
            double moyenneAge = tous.isEmpty() ? 0 : tous.stream().mapToInt(Patient::getAge).average().orElse(0);

            // 2. Définition d'une expression lambda compatible avec Function<Patient, String>
            Function<Patient, String> formateurLigne = p -> String.format(
                    "| %-9s | %-20s | %-4s | %2d ans | %-12s | %-10s |",
                    p.getCin(), p.getNomComplet(), p.getSexe(), p.getAge(),
                    (p.getTelephone() != null ? p.getTelephone() : "-"),
                    (p.getMutuelle() != null ? p.getMutuelle() : "Aucune")
            );

            List<String> lignesFormatees = patientService.transformerPatients(formateurLigne);

            // Construction du rapport d'affichage
            StringBuilder sb = new StringBuilder();
            sb.append("========================================================================\n");
            sb.append("    SYNTHÈSE DU CABINET DENTAIRE (Interfaces java.util.function)\n");
            sb.append("========================================================================\n\n");
            sb.append(String.format("  • Total des patients enregistrés : %d\n", total));
            sb.append(String.format("  • Hommes (Predicate<T>)          : %d (%.1f%%)\n", nbHommes, total > 0 ? (nbHommes * 100.0 / total) : 0));
            sb.append(String.format("  • Femmes (Predicate<T>)          : %d (%.1f%%)\n", nbFemmes, total > 0 ? (nbFemmes * 100.0 / total) : 0));
            sb.append(String.format("  • Âge moyen des patients         : %.1f ans\n", moyenneAge));
            sb.append(String.format("  • Patients seniors (>= 60 ans)   : %d\n", nbSeniors));
            sb.append(String.format("  • Patients mineurs (< 18 ans)    : %d\n", nbMineurs));
            sb.append(String.format("  • Couverture mutuelle active     : %d (%.1f%%)\n", nbAvecMutuelle, total > 0 ? (nbAvecMutuelle * 100.0 / total) : 0));
            sb.append(String.format("  • Seniors AVEC mutuelle (.and()) : %d\n\n", nbSeniorAvecMutuelle));

            sb.append("------------------------------------------------------------------------\n");
            sb.append("  Dossiers formatés via Function<Patient, String> :\n");
            sb.append("------------------------------------------------------------------------\n");
            sb.append("| CIN       | Nom & Prénom         | Sexe | Âge    | Téléphone    | Mutuelle   |\n");
            sb.append("+-----------+----------------------+------+--------+--------------+------------+\n");
            for (String ligne : lignesFormatees) {
                sb.append(ligne).append("\n");
            }
            sb.append("------------------------------------------------------------------------\n");

            txtRapport.setText(sb.toString());
            txtRapport.setCaretPosition(0);

        } catch (DentalException e) {
            txtRapport.setText("Erreur lors du calcul des statistiques : " + e.getMessage());
        }
    }

    private void onJournaliser() {
        try {
            // 3. Définition d'un Consumer<Patient>
            Consumer<Patient> journaliseur = p -> System.out.println(
                    "[AUDIT-CONSUMER] Dossier audité pour : " + p.getNomComplet() + " (CIN: " + p.getCin() + ")"
            );

            patientService.appliquerSurPatients(journaliseur);

            JOptionPane.showMessageDialog(this,
                    "Action Consumer<Patient> exécutée avec succès !\n"
                    + "Chaque dossier a été traité sans valeur de retour via Consumer.accept(T).\n"
                    + "Consultez la console pour voir la sortie de l'action.",
                    "Démonstration Consumer<T>",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DentalException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
