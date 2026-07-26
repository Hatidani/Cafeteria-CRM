package com.nwu.cafeteria.ui;

import com.nwu.cafeteria.service.AuthenticationService;
import com.nwu.cafeteria.util.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationDialog extends JDialog {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextArea allergiesArea;
    private AuthenticationService authService;
    
    public RegistrationDialog(JFrame parent) {
        super(parent, "Student Registration", true);
        authService = new AuthenticationService();
        initializeUI();
    }
    
    private void initializeUI() {
    setSize(540, 850); // Increased height and width for better visibility
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Create Student Account", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = UIStyle.createCardPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        addFormField(formPanel, gbc, "Full Name:", nameField = new JTextField(20));
        
        // Email
        addFormField(formPanel, gbc, "Student Email:", emailField = new JTextField(20));
        
        // Password
        addFormField(formPanel, gbc, "Password:", passwordField = new JPasswordField(20));
        
        // Confirm Password
        addFormField(formPanel, gbc, "Confirm Password:", confirmPasswordField = new JPasswordField(20));
        
        // --- START FIX: Show Password CheckBox with simplified echo char logic ---
        // The addFormField method leaves gbc.gridy pointing to the next available row.
        gbc.gridx = 0; gbc.gridwidth = 2; // Span two columns
        gbc.insets = new Insets(0, 10, 10, 10); // Small space above, 10 below
        
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Passwords");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.setBackground(UIStyle.CARD_BACKGROUND);
        
        // Listener to toggle password visibility for both fields
        showPasswordCheckbox.addActionListener(e -> {
            char echoChar = showPasswordCheckbox.isSelected() ? (char) 0 : '*'; // Use '*' as a safe default
            passwordField.setEchoChar(echoChar);
            confirmPasswordField.setEchoChar(echoChar);
        });
        
        formPanel.add(showPasswordCheckbox, gbc);
        gbc.gridy++; // Increment gridy for the next element
        // --- END FIX ---
        
        // Allergies (New gridy placement after CheckBox)
        JLabel allergiesLabel = new JLabel("Food Allergies/Dietary Restrictions:");
        allergiesLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(allergiesLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 15, 10);
        allergiesArea = new JTextArea(3, 20);
        allergiesArea.setLineWrap(true);
        allergiesArea.setWrapStyleWord(true);
        allergiesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        allergiesArea.setBorder(UIStyle.createRoundBorder());
        JScrollPane scrollPane = new JScrollPane(allergiesArea);
        formPanel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIStyle.CARD_BACKGROUND);
        
        JButton registerButton = UIStyle.createPrimaryButton("CREATE ACCOUNT");
        registerButton.addActionListener(new RegisterButtonListener());
        
        JButton cancelButton = UIStyle.createSecondaryButton("CANCEL");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        // Store the original insets (used for the space between form groups)
        Insets originalInsets = gbc.insets;
        
        // Add Label (places label above the field)
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 10, 0, 10); // Adjust insets: 0 at the bottom to place label closer to field
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);
        
        // Add Field
        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 15, 10); // Adjust insets: 15 at the bottom for spacing after the field
        panel.add(field, gbc);
        
        // Prepare for the next field: increment gridy and reset insets
        gbc.gridy++;
        gbc.insets = originalInsets;
    }
    
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String allergies = allergiesArea.getText().trim();
            
            // Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("Please fill in all required fields.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match.");
                return;
            }
            
            if (password.length() < 6) {
                showError("Password must be at least 6 characters long.");
                return;
            }
            
            if (!email.endsWith("@nwu.ac.za")) {
                showError("Please use your NWU student email address.");
                return;
            }
            
            try {
                boolean success = authService.registerCustomer(name, email, password, allergies);
                if (success) {
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                        "Account created successfully! You can now login.",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    showError("Failed to create account. Email might already be registered.");
                }
            } catch (Exception ex) {
                showError("Error creating account: " + ex.getMessage());
            }
        }
        
        private void showError(String message) {
            JOptionPane.showMessageDialog(RegistrationDialog.this, 
                message, "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}