package com.nwu.cafeteria.ui;

import com.nwu.cafeteria.model.Employee;
import com.nwu.cafeteria.model.MenuItem;
import com.nwu.cafeteria.model.Order;
import com.nwu.cafeteria.model.Review;
import com.nwu.cafeteria.service.AnalyticsService;
import com.nwu.cafeteria.service.MenuService;
import com.nwu.cafeteria.service.OrderService;
import com.nwu.cafeteria.service.ReviewService;
import com.nwu.cafeteria.util.UIStyle;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeDashboard extends JFrame {
    private Employee employee;
    private AnalyticsService analyticsService;
    private MenuService menuService;
    private OrderService orderService;
    private ReviewService reviewService;
    
    private JTabbedPane tabbedPane;
    private DefaultTableModel menuTableModel;
    
    public EmployeeDashboard(Employee employee) {
        this.employee = employee;
        this.analyticsService = new AnalyticsService();
        this.menuService = new MenuService();
        this.orderService = new OrderService();
        this.reviewService = new ReviewService();
        initializeUI();
        loadDashboardData();
    }
    
    private void initializeUI() {
        setTitle("NWU Cafeteria - Employee Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
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
        
        // Create tabs based on role
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Menu Management", createMenuManagementPanel());
        tabbedPane.addTab("Reviews", createReviewsPanel());
        tabbedPane.addTab("Analytics", createAnalyticsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer for admin button
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void setupAdminPanels() {
        // Check if admin tabs have already been added to prevent duplicates
        boolean adminTabsExist = false;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if ("Customer Management".equals(tabbedPane.getTitleAt(i))) {
                adminTabsExist = true;
                break;
            }
        }

        if (!adminTabsExist) {
            tabbedPane.addTab("Customer Management", createCustomerManagementPanel());
            tabbedPane.addTab("Orders", createOrdersPanel());
            JOptionPane.showMessageDialog(this, "Admin panels unlocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Switch to the first admin tab
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if ("Customer Management".equals(tabbedPane.getTitleAt(i))) {
                tabbedPane.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = UIStyle.createGradientHeader();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Left side - Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("NWU CAFETERIA MONITOR - EMPLOYEE PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + employee.getName() + " (" + employee.getRole() + ")");
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
        
        JButton helpButton = new JButton("?");
        helpButton.setToolTipText("Get help and dashboard tips");
        helpButton.setBackground(UIStyle.SECONDARY_PURPLE);
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(headerPanel,
            "Employee Dashboard Help:\n- Use the tabs to manage menu, reviews, analytics, and more.\n- Hover over cards for more info.\n- Click 'Refresh' to update dashboard stats.",
            "Dashboard Help", JOptionPane.INFORMATION_MESSAGE));
        rightPanel.add(helpButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(UIStyle.BACKGROUND);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Only show the admin button if the employee has the admin role
        if (employee.isAdmin()) {
            JButton adminButton = new JButton("Admin");
            adminButton.setBackground(UIStyle.WARNING_ORANGE);
            adminButton.setForeground(Color.WHITE);
            adminButton.setFocusPainted(false);
            adminButton.setToolTipText("Access Admin Panels");
            adminButton.addActionListener(e -> showAdminPasswordPrompt());
            footerPanel.add(adminButton);
        }

        return footerPanel;
    }

    private void showAdminPasswordPrompt() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Admin Passkey:");
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(this, panel, "Admin Access",
                                                 JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                                                 null, options, options[0]);
        if (option == 0) { // OK button
            char[] password = pass.getPassword();
            if ("1234".equals(new String(password))) {
                setupAdminPanels();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect passkey.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome section
        JPanel welcomeCard = UIStyle.createCardPanel();
        welcomeCard.setLayout(new BorderLayout());
        JLabel welcomeTitle = new JLabel("Employee Dashboard", JLabel.LEFT);
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeTitle.setForeground(UIStyle.PRIMARY_PURPLE);
        welcomeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel welcomeText = new JLabel("<html><b>Welcome back, " + employee.getName() + "!</b><br>"
                + "Monitor cafeteria performance and manage operations efficiently.<br>"
                + "<span style='color:#666;'>Tip: Use the tabs above to manage menu, reviews, analytics, and more.</span></html>");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 15));
        welcomeText.setForeground(UIStyle.TEXT_SECONDARY);
        JButton refreshButton = UIStyle.createSuccessButton("Refresh Dashboard");
        refreshButton.setToolTipText("Reload dashboard stats");
        refreshButton.addActionListener(e -> loadDashboardData());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(welcomeTitle, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);
        welcomeCard.add(topPanel, BorderLayout.NORTH);
        welcomeCard.add(welcomeText, BorderLayout.CENTER);
        
        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(UIStyle.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        Map<String, Object> stats = analyticsService.getDashboardStats();
        
        statsPanel.add(createStatCard("Total Customers", 
            stats.get("totalCustomers").toString(), "👤", UIStyle.PRIMARY_PURPLE));
        statsPanel.add(createStatCard("Total Orders", 
            stats.get("totalOrders").toString(), "#", UIStyle.SECONDARY_PURPLE));
        statsPanel.add(createStatCard("Total Revenue", 
            "R" + stats.get("totalRevenue"), "R", UIStyle.SUCCESS_GREEN));
        statsPanel.add(createStatCard("Total Reviews", 
            stats.get("totalReviews").toString(), "★", UIStyle.WARNING_ORANGE));
        statsPanel.add(createStatCard("Menu Items", 
            stats.get("availableMenuItems").toString(), "P", UIStyle.ACCENT_PURPLE));
        statsPanel.add(createStatCard("Avg Order Value", 
            "R" + analyticsService.getAverageOrderValue(), "%", UIStyle.DARK_PURPLE));
        
        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        actionsPanel.setBackground(UIStyle.BACKGROUND);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        actionsPanel.add(createActionCard("Manage Menu", "Add/Edit menu items", "+",
            e -> tabbedPane.setSelectedIndex(1)));
        actionsPanel.add(createActionCard("View Reviews", "Check customer feedback", "★",
            e -> tabbedPane.setSelectedIndex(2)));
        actionsPanel.add(createActionCard("Analytics", "View detailed reports", "↑",
            e -> tabbedPane.setSelectedIndex(3)));
        
        panel.add(welcomeCard, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout());
        
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconLabel.setForeground(color);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        iconLabel.setToolTipText(title + ": " + value);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(UIStyle.TEXT_PRIMARY);
        valueLabel.setToolTipText(title + ": " + value);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(UIStyle.TEXT_SECONDARY);
        titleLabel.setToolTipText(title);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyle.CARD_BACKGROUND);
        contentPanel.add(iconLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(titleLabel, BorderLayout.SOUTH);
        
        card.add(contentPanel, BorderLayout.CENTER);
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
        descLabel.setToolTipText(description);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(card.getBackground());
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
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
    
    private JPanel createMenuManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.BACKGROUND);

        JLabel titleLabel = new JLabel("Menu Management", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);

        JButton addButton = UIStyle.createPrimaryButton("Add New Item");
        addButton.addActionListener(e -> showAddMenuItemDialog());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Menu items table
        String[] columnNames = {"ID", "Name", "Description", "Price", "Rating", "Reviews", "Available", "Actions"};
        menuTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7; // "Available" and "Actions" are interactive
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Boolean.class; // Render as a checkbox
                }
                return String.class;
            }
        };
        
        JTable menuTable = new JTable(menuTableModel);
        menuTable.setFillsViewportHeight(true);
        menuTable.setRowHeight(40);
        menuTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Listener to handle the availability toggle
        menuTableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
                int row = e.getFirstRow();
                int itemId = (Integer) menuTableModel.getValueAt(row, 0);
                boolean isAvailable = (Boolean) menuTableModel.getValueAt(row, 6);

                Optional<MenuItem> itemOpt = menuService.getAllMenuItems().stream().filter(i -> i.getItemId() == itemId).findFirst();
                if (itemOpt.isPresent()) {
                    MenuItem item = itemOpt.get();
                    boolean success = menuService.updateMenuItem(itemId, item.getName(), item.getDescription(), item.getPrice().doubleValue(), isAvailable);
                    if (!success) {
                        JOptionPane.showMessageDialog(EmployeeDashboard.this, "Failed to update availability. Reverting.", "Error", JOptionPane.ERROR_MESSAGE);
                        loadMenuItems(menuTableModel); // Reload to revert the UI change
                    }
                }
            }
        });
        
        // Set custom renderer and editor for the "Actions" column
        menuTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(menuTable));

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Load menu items
        loadMenuItems(menuTableModel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReviewsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Header Panel for Title and Sort Dropdown
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Customer Reviews", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
    
        // Create a panel for sorting controls
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.setOpaque(false);
        
        JLabel sortLabel = new JLabel("Sort by Item:");
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sortLabel.setForeground(UIStyle.TEXT_SECONDARY);
    
        // Sorting dropdown
        JComboBox<String> sortComboBox = new JComboBox<>();
        sortComboBox.addItem("All Items"); // Default option
    
        // Populate with unique item names that have reviews
        reviewService.getAllReviews().stream()
                .map(Review::getItemName)
                .distinct()
                .sorted()
                .forEach(sortComboBox::addItem);
    
        sortPanel.add(sortLabel);
        sortPanel.add(sortComboBox);
    
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(sortPanel, BorderLayout.EAST);
        
        // Reviews will be loaded here
        JPanel reviewsContainer = new JPanel();
        reviewsContainer.setLayout(new BoxLayout(reviewsContainer, BoxLayout.Y_AXIS));
        reviewsContainer.setBackground(UIStyle.BACKGROUND);
        
        // Action listener for the dropdown to filter reviews
        sortComboBox.addActionListener(e -> {
            String selectedItem = (String) sortComboBox.getSelectedItem();
            loadReviews(reviewsContainer, selectedItem);
        });
        
        JScrollPane scrollPane = new JScrollPane(reviewsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Initial load of all reviews
        loadReviews(reviewsContainer, "All Items");
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Analytics & Reports", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Analytics content
        JPanel analyticsContent = new JPanel(new GridLayout(2, 2, 15, 15));
        analyticsContent.setBackground(UIStyle.BACKGROUND);
        
        // Popular items
        JPanel popularItemsPanel = UIStyle.createCardPanel();
        popularItemsPanel.setLayout(new BorderLayout());
        
        JLabel popularTitle = new JLabel("Popular Menu Items", JLabel.LEFT);
        popularTitle.setFont(new Font("Arial", Font.BOLD, 16));
        popularTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JTextArea popularItemsArea = new JTextArea();
        popularItemsArea.setEditable(false);
        popularItemsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        popularItemsArea.setBackground(UIStyle.CARD_BACKGROUND);
        
        List<MenuItem> popularItems = analyticsService.getPopularItems(5);
        StringBuilder popularText = new StringBuilder();
        for (MenuItem item : popularItems) {
            popularText.append("• ").append(item.getName())
                      .append(" - ").append(item.getRatingStars())
                      .append("\n");
        }
        popularItemsArea.setText(popularText.toString());
        
        popularItemsPanel.add(popularTitle, BorderLayout.NORTH);
        popularItemsPanel.add(new JScrollPane(popularItemsArea), BorderLayout.CENTER);
        
        // Rating distribution
        JPanel ratingPanel = UIStyle.createCardPanel();
        ratingPanel.setLayout(new BorderLayout());
        
        JLabel ratingTitle = new JLabel("Rating Distribution", JLabel.LEFT);
        ratingTitle.setFont(new Font("Arial", Font.BOLD, 16));
        ratingTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JTextArea ratingArea = new JTextArea();
        ratingArea.setEditable(false);
        ratingArea.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingArea.setBackground(UIStyle.CARD_BACKGROUND);
        
        Map<String, Integer> ratingDist = analyticsService.getRatingDistribution();
        StringBuilder ratingText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : ratingDist.entrySet()) {
            ratingText.append(entry.getKey()).append(": ").append(entry.getValue()).append("%\n");
        }
        ratingArea.setText(ratingText.toString());
        
        ratingPanel.add(ratingTitle, BorderLayout.NORTH);
        ratingPanel.add(new JScrollPane(ratingArea), BorderLayout.CENTER);
        
        analyticsContent.add(popularItemsPanel);
        analyticsContent.add(ratingPanel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(analyticsContent, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCustomerManagementPanel() {
        JPanel panel = UIStyle.createCardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Customer Management - Admin Only", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createOrdersPanel() {
        JPanel panel = UIStyle.createCardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Order Management - Admin Only", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(UIStyle.PRIMARY_PURPLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private void loadDashboardData() {
        // Load any initial data needed
    }
    
    private void loadMenuItems(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        
        List<MenuItem> items = menuService.getAllMenuItems();
        for (MenuItem item : items) {
            Object[] row = {
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getFormattedPrice(),
                item.getRatingStars(),
                item.getReviewCount(),
                item.isAvailable(), // Boolean for the checkbox
                "Edit" 
            };
            model.addRow(row);
        }
    }
    
    private void loadReviews(JPanel container, String filterItemName) {
        container.removeAll();
        
        List<Review> allReviews = reviewService.getAllReviews();
        List<Review> filteredReviews;
    
        // Filter reviews based on the selected item name
        if (filterItemName == null || "All Items".equals(filterItemName)) {
            filteredReviews = allReviews;
        } else {
            filteredReviews = allReviews.stream()
                .filter(review -> filterItemName.equals(review.getItemName()))
                .collect(Collectors.toList());
        }
    
        for (Review review : filteredReviews) {
            container.add(createReviewCard(review));
            container.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        if (filteredReviews.isEmpty()) {
            String message = "All Items".equals(filterItemName) ? "No reviews yet" : "No reviews found for this item.";
            JLabel noReviewsLabel = new JLabel(message, JLabel.CENTER);
            noReviewsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noReviewsLabel.setForeground(UIStyle.TEXT_SECONDARY);
            container.add(noReviewsLabel);
        }
        
        container.revalidate();
        container.repaint();
    }
    
    private JPanel createReviewCard(Review review) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(card.getWidth(), 200));

        // Header with customer name and rating
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.CARD_BACKGROUND);
        
        JLabel customerLabel = new JLabel("By: " + review.getCustomerName());
        customerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel ratingLabel = new JLabel(review.getRatingStars());
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ratingLabel.setForeground(UIStyle.SECONDARY_PURPLE);
        
        headerPanel.add(customerLabel, BorderLayout.WEST);
        headerPanel.add(ratingLabel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Item name
        JLabel itemLabel = new JLabel("Item: " + review.getItemName());
        itemLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        itemLabel.setForeground(UIStyle.TEXT_SECONDARY);
        
        // Comment
        JTextArea commentArea = new JTextArea(review.getComment() != null ? review.getComment() : "No comment provided");
        commentArea.setEditable(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBackground(UIStyle.CARD_BACKGROUND);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        commentScrollPane.setBorder(BorderFactory.createTitledBorder("Comment"));
        commentScrollPane.setPreferredSize(new Dimension(0, 80)); 
        
        // Date
        JLabel dateLabel = new JLabel("Posted: " + review.getCreatedAt().toLocalDate().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(UIStyle.TEXT_SECONDARY);
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        JPanel contentStackPanel = new JPanel();
        contentStackPanel.setLayout(new BoxLayout(contentStackPanel, BoxLayout.Y_AXIS));
        contentStackPanel.setBackground(UIStyle.CARD_BACKGROUND);
        
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentStackPanel.add(headerPanel);
        contentStackPanel.add(itemLabel);
        contentStackPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentStackPanel.add(commentScrollPane);
        
        card.add(contentStackPanel, BorderLayout.CENTER);
        card.add(dateLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void showAddMenuItemDialog() {
        JDialog dialog = new JDialog(this, "Add Menu Item", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridy = 1;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(descLabel, gbc);
        
        gbc.gridy = 3;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(UIStyle.createRoundBorder());
        formPanel.add(new JScrollPane(descArea), gbc);
        
        gbc.gridy = 4;
        JLabel priceLabel = new JLabel("Price (R):");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(priceLabel, gbc);
        
        gbc.gridy = 5;
        JTextField priceField = new JTextField(20);
        formPanel.add(priceField, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIStyle.BACKGROUND);
        
        JButton saveButton = UIStyle.createPrimaryButton("Save Item");
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descArea.getText().trim();
            String priceText = priceField.getText().trim();
            
            if (name.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double price = Double.parseDouble(priceText);
                boolean success = menuService.addMenuItem(name, description, price);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Menu item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadMenuItems(menuTableModel);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add menu item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = UIStyle.createSecondaryButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel, gbc);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showEditMenuItemDialog(MenuItem item) {
        JDialog dialog = new JDialog(this, "Edit Menu Item", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(UIStyle.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(new JLabel("Item Name: " + item.getName()), gbc);

        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridy = 2;
        JTextArea descArea = new JTextArea(item.getDescription(), 3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(descArea), gbc);

        gbc.gridy = 3;
        formPanel.add(new JLabel("Price (R):"), gbc);
        gbc.gridy = 4;
        JTextField priceField = new JTextField(item.getPrice().toString());
        formPanel.add(priceField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIStyle.BACKGROUND);

        JButton saveButton = UIStyle.createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> {
            String description = descArea.getText().trim();
            String priceText = priceField.getText().trim();

            if (priceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Price cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                boolean success = menuService.updateMenuItem(item.getItemId(), item.getName(), description, price, item.isAvailable());
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadMenuItems(menuTableModel);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = UIStyle.createSecondaryButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JTable table) {
            super(new JCheckBox());
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                      boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
                int itemId = (Integer) menuTableModel.getValueAt(selectedRow, 0);

                Optional<MenuItem> itemOpt = menuService.getAllMenuItems().stream()
                    .filter(i -> i.getItemId() == itemId).findFirst();

                if (itemOpt.isPresent()) {
                    showEditMenuItemDialog(itemOpt.get());
                } else {
                    JOptionPane.showMessageDialog(EmployeeDashboard.this, "Could not find item details to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
