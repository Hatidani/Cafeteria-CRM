package com.nwu.cafeteria.ui;

import com.nwu.cafeteria.model.Customer;
import com.nwu.cafeteria.model.Employee;
import com.nwu.cafeteria.service.AuthenticationService;
import com.nwu.cafeteria.util.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private AuthenticationService authService;
    
    public LoginFrame() {
        authService = new AuthenticationService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NWU Cafeteria Satisfaction Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(540, 850); // Increased height for register button visibility
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with logo and title
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Login form
        mainPanel.add(createLoginFormPanel(), BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Title
        JLabel titleLabel = new JLabel("NWU CAFETERIA", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        
        JLabel subtitleLabel = new JLabel("Satisfaction Monitor", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(UIStyle.TEXT_SECONDARY);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(UIStyle.BACKGROUND);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createLoginFormPanel() {
        JPanel formPanel = UIStyle.createCardPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 25, 20, 25); // General padding for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel formTitle = new JLabel("Welcome Back", JLabel.CENTER);
        formTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formTitle.setForeground(UIStyle.TEXT_PRIMARY);
        formPanel.add(formTitle, gbc);
        
        JLabel formSubtitle = new JLabel("Sign in to your account", JLabel.CENTER);
        formSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        formSubtitle.setForeground(UIStyle.TEXT_SECONDARY);
        gbc.gridy = 1;
        formPanel.add(formSubtitle, gbc);
        
        // User Type
        gbc.gridy = 2; gbc.gridwidth = 1;
    gbc.insets = new Insets(30, 25, 10, 25); // More space for user type
        JLabel typeLabel = new JLabel("I am a:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Student Customer", "Cafeteria Employee"});
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        userTypeCombo.setBackground(UIStyle.CARD_BACKGROUND);
        formPanel.add(userTypeCombo, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
    gbc.insets = new Insets(25, 25, 10, 25); // More space for email
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);
        
        // --- START FIX: Show Password CheckBox with simplified echo char logic ---
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.WEST; // Align to the left of the cell
        gbc.insets = new Insets(0, 25, 5, 25); // Small top inset, align to right of field, small bottom inset
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.setBackground(UIStyle.CARD_BACKGROUND);
        
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show text
            } else {
                passwordField.setEchoChar('*'); // Use '*' as a safe default hide char
            }
        });
        
        formPanel.add(showPasswordCheckbox, gbc);
        // --- END FIX ---
        
        // Forgot Password Link (gridY updated from 5 to 6)
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1; // Align to the right
        gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 25, 5, 25); // Smaller inset above login button
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new ForgotPasswordListener());
        formPanel.add(forgotPasswordLabel, gbc);
        
        // Login Button (gridY updated from 6 to 7)
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
    gbc.insets = new Insets(25, 25, 15, 25); // More space for login button
    JButton loginButton = UIStyle.createPrimaryButton("SIGN IN");
    loginButton.setPreferredSize(new Dimension(240, 55)); // Slightly larger button
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(new LoginButtonListener());
        formPanel.add(loginButton, gbc);
        
        // Separator (gridY updated from 7 to 8)
        gbc.gridy = 8;
        JSeparator separator = new JSeparator();
        separator.setForeground(UIStyle.LIGHT_PURPLE);
        formPanel.add(separator, gbc);
        
        // Register Section (gridY updated from 8 to 9)
        gbc.gridy = 9;
        JLabel registerLabel = new JLabel("New to NWU Cafeteria?", JLabel.CENTER);
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLabel.setForeground(UIStyle.TEXT_SECONDARY);
        formPanel.add(registerLabel, gbc);
        
        // Register Button (gridY updated from 9 to 10)
        gbc.gridy = 10;
    gbc.insets = new Insets(20, 25, 10, 25); // More space for register button
        JButton registerButton = UIStyle.createSecondaryButton("CREATE STUDENT ACCOUNT");
        registerButton.addActionListener(new RegisterButtonListener());
        formPanel.add(registerButton, gbc);
        
        return formPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(UIStyle.BACKGROUND);
        
        JLabel footerLabel = new JLabel("North-West University Mafikeng Campus • © 2025");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(UIStyle.TEXT_SECONDARY);
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(LoginFrame.this, message, title, messageType);
    }
    
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeCombo.getSelectedItem();
            
            if (email.isEmpty() || password.isEmpty()) {
                showMessage("Please enter both email and password", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                if ("Student Customer".equals(userType)) {
                    Customer customer = authService.loginCustomer(email, password);
                    if (customer != null) {
                        showMessage("Welcome back, " + customer.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        new CustomerDashboard(customer).setVisible(true);
                        dispose();
                    } else {
                        showMessage("Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else { // Employee Login
                    if ("admin@nwu.ac.za".equalsIgnoreCase(email) && "admin123".equals(password)) {
                        Employee admin = new Employee();
                        admin.setName("Admin User");
                        admin.setEmail(email);
                        admin.setRole("Admin"); // Set role to Admin to enable admin features
                        showMessage("Welcome, Admin!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        new EmployeeDashboard(admin).setVisible(true);
                        dispose();
                    } else if ("employee@nwu.ac.za".equalsIgnoreCase(email) && "employee123".equals(password)) {
                        Employee employee = new Employee();
                        employee.setName("Employee");
                        employee.setEmail(email);
                        // Default role is not admin
                        showMessage("Welcome, Employee!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        new EmployeeDashboard(employee).setVisible(true);
                        dispose();
                    } else {
                        showMessage("Invalid employee credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                showMessage("Error during login: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new RegistrationDialog(LoginFrame.this).setVisible(true);
        }
    }
    
    /**
     * MouseListener for the "Forgot Password?" link.
     */
    private class ForgotPasswordListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            String userType = (String) userTypeCombo.getSelectedItem();
            String message;
            String title = "Password Recovery";
            int messageType = JOptionPane.INFORMATION_MESSAGE;
            
            if ("Student Customer".equals(userType)) {
                message = "To reset your password, please contact the NWU Cafeteria system administrator or visit the dedicated student support desk.";
            } else {
                message = "Employee password resets must be requested via the IT support portal or by contacting your immediate supervisor.";
            }
            
            showMessage(message, title, messageType);
        }
    }
}
