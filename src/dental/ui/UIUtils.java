package dental.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Utilitaires d'interface graphique Swing.
 * Centralise les couleurs, polices et composants stylisés.
 */
public final class UIUtils {

    // Palette de couleurs
    public static final Color COLOR_PRIMARY = new Color(26, 82, 118);       // Bleu médical
    public static final Color COLOR_PRIMARY_HOVER = new Color(21, 67, 96);
    public static final Color COLOR_ACCENT = new Color(22, 160, 133);        // Vert canard/turquoise
    public static final Color COLOR_ACCENT_HOVER = new Color(19, 141, 117);
    public static final Color COLOR_DANGER = new Color(192, 57, 43);         // Rouge suppression
    public static final Color COLOR_DANGER_HOVER = new Color(169, 50, 38);
    public static final Color COLOR_BACKGROUND = new Color(245, 247, 250);   // Gris très clair
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    public static final Color COLOR_TEXT_MUTED = new Color(127, 140, 141);
    public static final Color COLOR_BORDER = new Color(220, 224, 230);

    // Polices standard
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    private UIUtils() {
        // Constructeur privé pour classe utilitaire
    }

    /**
     * Crée un bouton stylisé avec coins propres et padding.
     * Sous le Look & Feel système (Windows), setBackground() seul est ignoré :
     * le delegate de rendu peint son propre fond. On force donc la peinture
     * manuelle avec setContentAreaFilled(false) + setOpaque(true).
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY_BOLD);

        // Rendu du fond personnalisé (correction du bug "bouton invisible")
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet de survol simple (MouseListener — Java standard)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bg.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bg);
                }
            }
        });
        return button;
    }

    /**
     * Crée un champ texte avec bordure et padding.
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 32));
        Border line = BorderFactory.createLineBorder(COLOR_BORDER, 1);
        Border padding = new EmptyBorder(4, 8, 4, 8);
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        return field;
    }

    /**
     * Crée un champ mot de passe avec bordure et padding.
     */
    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 32));
        Border line = BorderFactory.createLineBorder(COLOR_BORDER, 1);
        Border padding = new EmptyBorder(4, 8, 4, 8);
        field.setBorder(BorderFactory.createCompoundBorder(line, padding));
        return field;
    }

    /**
     * Crée un panneau avec ombre / bordure et fond blanc.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }
}
