package com.nwu.cafeteria;

import com.nwu.cafeteria.ui.LoginFrame;
import com.nwu.cafeteria.util.DatabaseConfig;
import com.nwu.cafeteria.util.UIStyle;

import javax.swing.*;

public class CafeteriaApp {
    public static void main(String[] args) {
        // Test database connection
        if (!DatabaseConfig.testConnection()) {
            JOptionPane.showMessageDialog(null,
                "Cannot connect to database. Please ensure:\n" +
                "1. MySQL is running\n" +
                "2. Database 'nwu_cafeteria_monitor' exists\n" +
                "3. User 'cafeteria_app' with correct permissions exists",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Set custom UI style with purple theme
        UIStyle.applyPurpleTheme();
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}