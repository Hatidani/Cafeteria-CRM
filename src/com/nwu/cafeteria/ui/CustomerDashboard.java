package com.nwu.cafeteria.ui;

import com.nwu.cafeteria.dao.CustomerDAO;
import com.nwu.cafeteria.model.Customer;
import com.nwu.cafeteria.model.MenuItem;
import com.nwu.cafeteria.model.Review;
import com.nwu.cafeteria.service.MenuService;
import com.nwu.cafeteria.service.ReviewService;
import com.nwu.cafeteria.util.UIStyle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerDashboard extends JFrame {
    private Customer customer;
    private ReviewService reviewService;
    private MenuService menuService;
    private CustomerDAO customerDAO; // NEW FIELD: For profile updates
    
    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel;
    private JPanel menuPanel;
    private JPanel reviewsPanel;
    private JPanel profilePanel;
    private JPanel menuItemsContainer; // Container for menu item cards
    private JPanel reviewsContainer; // Container for customer's reviews

    // NEW FIELDS: Fields to hold profile labels for dynamic updating
    private JLabel nameValueLabel;
    private JLabel emailValueLabel;
    private JLabel memberSinceValueLabel;
    private JLabel allergiesValueLabel;

    
    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        this.reviewService = new ReviewService();
        this.menuService = new MenuService();
        this.customerDAO = new CustomerDAO(); // Initialize DAO
        initializeUI();
        loadDashboardData();
    }
    
    private void initializeUI() {
        setTitle("NWU Cafeteria - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Create main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BACKGROUND);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Tabbed content
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(UIStyle.BACKGROUND);
        tabbedPane.setForeground(UIStyle.TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create tabs
        dashboardPanel = createDashboardPanel();
        menuPanel = createMenuPanel();
        reviewsPanel = createReviewsPanel();
        profilePanel = createProfilePanel();
        
        // Emojis replaced with general characters
        tabbedPane.addTab("D Dashboard", dashboardPanel); 
        tabbedPane.addTab("M Menu", menuPanel); 
        tabbedPane.addTab("★ My Reviews", reviewsPanel);
        tabbedPane.addTab("P Profile", profilePanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // NEW: Add a listener to refresh the reviews panel when its tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { // "★ My Reviews" is the 3rd tab (index 2)
                refreshReviewsPanel();
            }
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.PRIMARY_PURPLE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Left side - Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(UIStyle.PRIMARY_PURPLE);
        
        JLabel titleLabel = new JLabel("NWU CAFETERIA MONITOR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(UIStyle.PRIMARY_PURPLE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        rightPanel.add(welcomeLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(UIStyle.PRIMARY_PURPLE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        rightPanel.add(logoutButton);
        // Right side - User info, help/info, and logout
        JButton helpButton = new JButton("?");
        helpButton.setToolTipText("Get help and dashboard tips");
        helpButton.setBackground(UIStyle.SECONDARY_PURPLE);
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(rightPanel,
            "Student Dashboard Help:\n- Use the tabs to view menu, reviews, and your profile.\n- Hover over cards for more info.\n- Click 'Refresh' to update dashboard stats.",
            "Dashboard Help", JOptionPane.INFORMATION_MESSAGE));
        rightPanel.add(helpButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome section
        JPanel welcomeCard = UIStyle.createCardPanel();
        welcomeCard.setLayout(new BorderLayout());
        
        JLabel welcomeTitle = new JLabel("Cafeteria Satisfaction Dashboard", JLabel.LEFT);
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeTitle.setForeground(UIStyle.PRIMARY_PURPLE);
        welcomeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel welcomeText = new JLabel("<html><b>Welcome back, " + customer.getName() + "!</b><br>"
        + "Share your dining experience and help us improve our services.<br>"
        + "<span style='color:#666;'>Tip: Use the tabs above to explore menu, reviews, and your profile.</span></html>");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 15));
        welcomeText.setForeground(UIStyle.TEXT_SECONDARY);
        
        JButton refreshButton = UIStyle.createSuccessButton("Refresh Dashboard");
        refreshButton.setToolTipText("Reload dashboard data");
        refreshButton.addActionListener(e -> loadDashboardData());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(welcomeTitle, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);
        welcomeCard.add(topPanel, BorderLayout.NORTH);
        welcomeCard.add(welcomeText, BorderLayout.CENTER);
        
        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(UIStyle.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Emojis replaced with general characters
        statsPanel.add(createStatCard("Total Reviews", "12", "V")); 
        statsPanel.add(createStatCard("Loyalty Points", "450", "★")); 
        statsPanel.add(createStatCard("Avg. Rating", "4.2", "R")); 
        
        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        actionsPanel.setBackground(UIStyle.BACKGROUND);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        // Emojis replaced with general characters
        actionsPanel.add(createActionCard("Rate Food", "Share your feedback on today's meal", "★", e -> tabbedPane.setSelectedIndex(1))); 
        actionsPanel.add(createActionCard("View Menu", "See what's available today", "M", e -> tabbedPane.setSelectedIndex(1))); 
        actionsPanel.add(createActionCard("My Reviews", "View and edit your feedback", "^", e -> tabbedPane.setSelectedIndex(2))); 
        actionsPanel.add(createActionCard("Loyalty", "Check your rewards and points", "G", e -> showLoyaltyInfo())); 
        
        panel.add(welcomeCard, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout());
        
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(UIStyle.TEXT_SECONDARY);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        iconLabel.setToolTipText(title + ": " + value);
        valueLabel.setToolTipText(title + ": " + value);
        titleLabel.setToolTipText(title);
        
        return card;
    }
    
    private JPanel createActionCard(String title, String description, String icon, ActionListener action) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(UIStyle.LIGHT_PURPLE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(UIStyle.CARD_BACKGROUND);
            }
        });
        
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(UIStyle.TEXT_PRIMARY);
        
        JLabel descLabel = new JLabel(description, JLabel.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(UIStyle.TEXT_SECONDARY);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(card.getBackground());
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        iconLabel.setToolTipText(title);
        titleLabel.setToolTipText(title);
        descLabel.setToolTipText(description);
        
        // Add click listener
        for (Component comp : card.getComponents()) {
            comp.addMouseListener((java.awt.event.MouseAdapter) card.getMouseListeners()[0]);
        }
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, ""));
            }
        });
        
        return card;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Today's Menu", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Container for menu item cards, arranged vertically
        menuItemsContainer = new JPanel();
        menuItemsContainer.setLayout(new BoxLayout(menuItemsContainer, BoxLayout.Y_AXIS));
        menuItemsContainer.setBackground(UIStyle.BACKGROUND);
        
        JScrollPane scrollPane = new JScrollPane(menuItemsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyle.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Improve scroll speed

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Populate the menu with data from the service
        loadMenuItems();
        
        return panel;
    }
    
    /**
     * Fetches menu items from the MenuService and populates the menu panel.
     */
    private void loadMenuItems() {
        menuItemsContainer.removeAll(); // Clear existing items before loading new ones

        List<MenuItem> menuItems = menuService.getAvailableMenuItems();

        if (menuItems == null || menuItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("No menu items are available at the moment.", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(UIStyle.TEXT_SECONDARY);
            menuItemsContainer.add(emptyLabel);
        } else {
            for (MenuItem item : menuItems) {
                menuItemsContainer.add(createMenuItemCard(item));
                // Add a small vertical space between cards
                menuItemsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Refresh the container panel
        menuItemsContainer.revalidate();
        menuItemsContainer.repaint();
    }

    /**
     * Creates a styled JPanel card for a single menu item with a rate button.
     * @param item The MenuItem object to display.
     * @return A JPanel representing the menu item card.
     */
    private JPanel createMenuItemCard(MenuItem item) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, UIStyle.PRIMARY_PURPLE),
            BorderFactory.createEmptyBorder(15, 15, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Panel for item name and description
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(new JLabel(item.getName()) {{
            setFont(new Font("Arial", Font.BOLD, 18));
            setForeground(UIStyle.TEXT_PRIMARY);
        }});
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(new JLabel("<html><p style='width:450px;'>" + item.getDescription() + "</p></html>") {{
            setFont(new Font("Arial", Font.PLAIN, 13));
            setForeground(UIStyle.TEXT_SECONDARY);
        }});

        // Right-side panel for price and the rate button
        JPanel actionPanel = new JPanel(new BorderLayout(0, 10));
        actionPanel.setOpaque(false);
        
        JLabel priceLabel = new JLabel(String.format("R %.2f", item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Emoji replaced with standard character
        JButton rateButton = new JButton("★ Rate Item");
        rateButton.setToolTipText("Leave a review for " + item.getName());
        rateButton.setBackground(UIStyle.SECONDARY_PURPLE);
        rateButton.setForeground(Color.WHITE);
        rateButton.setFocusPainted(false);
        rateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        rateButton.addActionListener(e -> showReviewDialog(item));

        actionPanel.add(priceLabel, BorderLayout.NORTH);
        actionPanel.add(rateButton, BorderLayout.SOUTH);

        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }
    
    /**
     * Displays a dialog for submitting a review for a specific menu item.
     * @param item The MenuItem to be reviewed.
     */
    private void showReviewDialog(MenuItem item) {
        // Main panel for the dialog
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Reviewing: " + item.getName(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Rating input
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Rating (1-5):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        Integer[] ratings = {5, 4, 3, 2, 1}; // 5-star is typically first
        JComboBox<Integer> ratingComboBox = new JComboBox<>(ratings);
        inputPanel.add(ratingComboBox, gbc);
        
        // Comment input
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTH;
        inputPanel.add(new JLabel("Comment (optional):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JTextArea commentArea = new JTextArea(5, 25);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        inputPanel.add(commentScrollPane, gbc);
        
        dialogPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Show the dialog using JOptionPane
        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Submit a Review",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
        if (result == JOptionPane.OK_OPTION) {
            // User clicked "OK", so we submit the review
            int rating = (int) ratingComboBox.getSelectedItem();
            String comment = commentArea.getText();
            
            // Call the review service to submit the review
            boolean success = reviewService.submitReview(customer.getCustomerId(), item.getItemId(), rating, comment); 
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Thank you! Your review has been submitted successfully.",
                        "Review Submitted", JOptionPane.INFORMATION_MESSAGE);
                // Optionally refresh the reviews panel after submission
                if (tabbedPane.getSelectedIndex() == 2) {
                    refreshReviewsPanel();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, there was an error submitting your review. Please try again.",
                        "Submission Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createReviewsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Feedback History", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Reviews will be loaded here
        reviewsContainer = new JPanel(); 
        reviewsContainer.setLayout(new BoxLayout(reviewsContainer, BoxLayout.Y_AXIS));
        reviewsContainer.setBackground(UIStyle.BACKGROUND);
        
        JScrollPane scrollPane = new JScrollPane(reviewsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyle.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Fetches the customer's reviews and populates the reviews panel.
     */
    private void refreshReviewsPanel() {
        reviewsContainer.removeAll(); // Clear existing items

        List<Review> reviews = reviewService.getReviewsByCustomer(customer.getCustomerId());

        if (reviews == null || reviews.isEmpty()) {
            JLabel emptyLabel = new JLabel("You have not submitted any reviews yet.", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(UIStyle.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            reviewsContainer.add(Box.createVerticalStrut(50));
            reviewsContainer.add(emptyLabel);
        } else {
            for (Review review : reviews) {
                reviewsContainer.add(createReviewCard(review));
                reviewsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Refresh the container panel
        reviewsContainer.revalidate();
        reviewsContainer.repaint();
    }
    
    /**
     * Creates a styled JPanel card for a single customer review.
     * @param review The Review object to display.
     * @return A JPanel representing the review card.
     */
    private JPanel createReviewCard(Review review) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(15, 0));
        // Note: UIStyle.SECONDARY_PURPLE is used here for a slightly different look
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, UIStyle.SECONDARY_PURPLE),
            BorderFactory.createEmptyBorder(15, 15, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Panel for item name, rating, and date
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setOpaque(false);
        
        JLabel itemLabel = new JLabel(review.getItemName());
        itemLabel.setFont(new Font("Arial", Font.BOLD, 18));
        itemLabel.setForeground(UIStyle.TEXT_PRIMARY);
        
        JLabel ratingLabel = new JLabel(review.getRatingStars());
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        Color starColor = new Color(255, 165, 0); // Orange
        ratingLabel.setForeground(starColor); 
        
        JLabel dateLabel = new JLabel("Reviewed on: " + review.getCreatedAt().toLocalDate().toString());
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dateLabel.setForeground(UIStyle.TEXT_SECONDARY);

        headerPanel.add(itemLabel);
        headerPanel.add(ratingLabel);
        
        // Comment/Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Use a JTextArea inside a wrapper panel for better layout control
        JTextArea commentArea = new JTextArea(review.getComment() != null && !review.getComment().isEmpty() ? review.getComment() : "No comment provided.");
        commentArea.setFont(new Font("Arial", Font.PLAIN, 13));
        commentArea.setForeground(UIStyle.TEXT_PRIMARY);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setEditable(false);
        commentArea.setOpaque(false);
        commentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        contentPanel.add(headerPanel);
        contentPanel.add(commentArea);
        contentPanel.add(dateLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }
    
    /**
     * Creates the profile display panel with a button to open the change profile dialog.
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel profileCard = UIStyle.createCardPanel();
        profileCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Profile header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        JLabel profileTitle = new JLabel("Student Profile", JLabel.LEFT);
        profileTitle.setFont(new Font("Arial", Font.BOLD, 20));
        profileTitle.setForeground(UIStyle.PRIMARY_PURPLE);
        profileCard.add(profileTitle, gbc);

        // Add Change Profile Button (NEW)
        JButton changeProfileButton = UIStyle.createPrimaryButton("Change Profile");
        changeProfileButton.addActionListener(e -> showChangeProfileDialog());
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0; // Push button to the right
        profileCard.add(changeProfileButton, gbc);
        gbc.weightx = 0; // Reset weight
        
        // Profile details (Assigning value labels to class fields)
        nameValueLabel = addProfileField(profileCard, gbc, 1, "Name:", customer.getName());
        emailValueLabel = addProfileField(profileCard, gbc, 2, "Email:", customer.getEmail());
        memberSinceValueLabel = addProfileField(profileCard, gbc, 3, "Member Since:", customer.getCreatedAt().toLocalDate().toString());
        allergiesValueLabel = addProfileField(profileCard, gbc, 4, "Allergies:", customer.getAllergies() != null ? customer.getAllergies() : "None specified");
        
        panel.add(profileCard, BorderLayout.NORTH); // Use NORTH to keep the card compact at the top
        return panel;
    }
    
    /**
     * Helper to add a label/value pair to the profile panel and return the value label. 
     * FIX: Corrected gridx assignment to prevent overlap.
     */
    private JLabel addProfileField(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        // Label on the left (column 0)
        gbc.gridx = 0; 
        gbc.gridy = row; 
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; // Anchor to the left
        
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fieldLabel.setForeground(UIStyle.TEXT_PRIMARY);
        panel.add(fieldLabel, gbc);
        
        // Value on the right (column 1)
        gbc.gridx = 1; 
        gbc.gridwidth = 2; // Span columns 1 and 2
        gbc.weightx = 1.0; // Give it extra space
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        valueLabel.setForeground(UIStyle.TEXT_SECONDARY);
        panel.add(valueLabel, gbc);
        
        gbc.weightx = 0; // Reset weight
        // gbc.gridx = 0; // This reset is not needed if the next call will set gridx=0 again
        
        return valueLabel;
    }

    /**
     * Displays a dialog for the customer to change their name and allergies.
     */
    private void showChangeProfileDialog() {
        // Main panel for the dialog
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Update Your Profile Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name input (Username)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        JTextField nameField = new JTextField(customer.getName(), 20);
        inputPanel.add(nameField, gbc);
        
        // Allergies input
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(new JLabel("Allergies:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        JTextField allergiesField = new JTextField(customer.getAllergies() != null ? customer.getAllergies() : "", 20);
        inputPanel.add(allergiesField, gbc);
        
        dialogPanel.add(inputPanel, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Change Profile",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newAllergies = allergiesField.getText().trim();
            
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty.",
                        "Update Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the Customer model object
            customer.setName(newName);
            customer.setAllergies(newAllergies.isEmpty() ? null : newAllergies);

            // Call DAO to persist the change
            boolean success = customerDAO.updateCustomer(customer);
            
            if (success) {
                // Refresh the profile panel labels and header welcome message
                updateProfileLabels();
                
                JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile. Please try again.",
                        "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Updates the labels on the profile panel and the dashboard header after a successful update.
     */
    private void updateProfileLabels() {
        // 1. Update Profile Panel Labels
        nameValueLabel.setText(customer.getName());
        allergiesValueLabel.setText(customer.getAllergies() != null ? customer.getAllergies() : "None specified");
        profilePanel.revalidate();
        profilePanel.repaint();
        
        // 2. Update the welcome message in the header
        JPanel mainPanel = (JPanel)getContentPane().getComponent(0);
        JPanel headerPanel = (JPanel)mainPanel.getComponent(0);
        
        // Find the rightPanel, which holds the welcome message
        if (headerPanel.getComponent(1) instanceof JPanel) {
            JPanel rightPanel = (JPanel) headerPanel.getComponent(1);
            for (Component rc : rightPanel.getComponents()) {
                // Find the JLabel that starts with "Welcome,"
                if (rc instanceof JLabel && ((JLabel)rc).getText().startsWith("Welcome,")) {
                     ((JLabel)rc).setText("Welcome, " + customer.getName() + "!");
                     break;
                }
            }
        }
    }
    
    private void loadDashboardData() {
        // This would load actual data from services
        // For now, we'll use mock data
    }
    
    private void showLoyaltyInfo() {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align: center;'>"
            + "<h3 style='color: " + String.format("#%02x%02x%02x", 
                UIStyle.PRIMARY_PURPLE.getRed(),
                UIStyle.PRIMARY_PURPLE.getGreen(),
                UIStyle.PRIMARY_PURPLE.getBlue()) + ";'>Loyalty Program</h3>"
            + "<p>Earn 1 point for every R1 spent!</p>"
            + "<p><b>Current Points: 450</b></p>"
            + "<p>Redeem 500 points for a free drink!</p>"
            + "</div></html>",
            "Loyalty Program",
            JOptionPane.INFORMATION_MESSAGE);
    }
}