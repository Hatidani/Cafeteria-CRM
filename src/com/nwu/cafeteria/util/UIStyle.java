package com.nwu.cafeteria.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIStyle {
    // Enhanced Purple color palette
    public static final Color PRIMARY_PURPLE = new Color(102, 51, 153);      // Deep purple
    public static final Color SECONDARY_PURPLE = new Color(147, 112, 219);   // Medium purple
    public static final Color LIGHT_PURPLE = new Color(216, 191, 216);       // Light purple
    public static final Color ACCENT_PURPLE = new Color(186, 85, 211);       // Vibrant purple
    public static final Color DARK_PURPLE = new Color(75, 0, 130);           // Dark purple
    public static final Color BACKGROUND = new Color(248, 245, 252);         // Light background
    public static final Color CARD_BACKGROUND = new Color(255, 255, 255);    // White cards
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51);          // Dark text
    public static final Color TEXT_SECONDARY = new Color(102, 102, 102);     // Medium text
    public static final Color SUCCESS_GREEN = new Color(46, 125, 50);        // Success messages
    public static final Color WARNING_ORANGE = new Color(237, 108, 2);       // Warning messages
    public static final Color ERROR_RED = new Color(211, 47, 47);            // Error messages
    
    // Gradients for modern look
    public static GradientPaint PRIMARY_GRADIENT = new GradientPaint(0, 0, PRIMARY_PURPLE, 0, 100, DARK_PURPLE);
    public static GradientPaint SECONDARY_GRADIENT = new GradientPaint(0, 0, SECONDARY_PURPLE, 0, 100, PRIMARY_PURPLE);
    public static Object FONT_BODY;
    
    public static void applyPurpleTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            
            // Enhanced UI defaults with purple theme
            UIManager.put("Button.background", PRIMARY_PURPLE);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.focus", ACCENT_PURPLE);
            UIManager.put("Button.select", SECONDARY_PURPLE);
            UIManager.put("Button.border", createRoundBorder());
            
            UIManager.put("Panel.background", BACKGROUND);
            
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            
            UIManager.put("TextField.background", CARD_BACKGROUND);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", PRIMARY_PURPLE);
            UIManager.put("TextField.border", createRoundBorder());
            
            UIManager.put("PasswordField.background", CARD_BACKGROUND);
            UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
            UIManager.put("PasswordField.caretForeground", PRIMARY_PURPLE);
            UIManager.put("PasswordField.border", createRoundBorder());
            
            UIManager.put("ComboBox.background", CARD_BACKGROUND);
            UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
            UIManager.put("ComboBox.border", createRoundBorder());
            
            UIManager.put("TextArea.background", CARD_BACKGROUND);
            UIManager.put("TextArea.foreground", TEXT_PRIMARY);
            UIManager.put("TextArea.border", createRoundBorder());
            
            UIManager.put("ScrollPane.background", BACKGROUND);
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
            UIManager.put("ScrollPane.viewportBorder", BorderFactory.createEmptyBorder());
            
            UIManager.put("Table.background", CARD_BACKGROUND);
            UIManager.put("Table.foreground", TEXT_PRIMARY);
            UIManager.put("Table.gridColor", LIGHT_PURPLE);
            UIManager.put("Table.selectionBackground", SECONDARY_PURPLE);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("Table.headerBackground", LIGHT_PURPLE);
            UIManager.put("Table.headerForeground", DARK_PURPLE);
            
            UIManager.put("TabbedPane.background", BACKGROUND);
            UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
            UIManager.put("TabbedPane.selected", PRIMARY_PURPLE);
            UIManager.put("TabbedPane.border", BorderFactory.createEmptyBorder());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Border createRoundBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_PURPLE, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
    
    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_PURPLE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }
    
    public static Border createSectionBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SECONDARY_PURPLE, 2),
                "",
                0, 0,
                new Font("Arial", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
    
    public static Border createHeaderBorder() {
        return BorderFactory.createEmptyBorder(10, 20, 10, 20);
    }
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_PURPLE, 0, getHeight(), DARK_PURPLE);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        return button;
    }
    
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SECONDARY_PURPLE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBorder(createRoundBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SUCCESS_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        return button;
    }
    
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(createCardBorder());
        return panel;
    }
    
    public static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        Border border = createSectionBorder();
        if (!title.isEmpty() && border instanceof javax.swing.border.TitledBorder) {
            ((javax.swing.border.TitledBorder) border).setTitle(title);
        }
        panel.setBorder(border);
        return panel;
    }
    
    public static JPanel createGradientHeader() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_PURPLE, getWidth(), getHeight(), DARK_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    public static String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
