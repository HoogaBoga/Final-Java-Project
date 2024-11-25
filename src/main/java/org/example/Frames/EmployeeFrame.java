package org.example.Frames;

import org.example.Buttons.*;
import org.example.Misc.DatabaseManager;
import org.example.Misc.UserSessionManager;
import org.example.Panels.*;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class EmployeeFrame extends JFrame {
    private DashBoardPanel dashBoardPanel;
    private InventoryPanel inventoryPanel;
    public JPanel cardPanel;
    private OrdersPanel ordersPanel;
    private int userId;
    private String activePanelName = "Dashboard";

    public EmployeeFrame(int userId) throws IOException, SQLException {
        this.userId = userId;
        ImageIcon greeneryImage = new ImageIcon(Objects.requireNonNull(HomeFrame.class.getResource("/Frame 12.png")));
        JLabel greeneryImg = new JLabel();

        UserNameBox userNameBox = new UserNameBox(userId);

        CardLayout cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize DashBoardPanel and buttons
        // Initialize DashBoardPanel, InventoryPanel, and OrdersPanel
        dashBoardPanel = new DashBoardPanel();
        inventoryPanel = new InventoryPanel();
        ordersPanel = new OrdersPanel(dashBoardPanel, inventoryPanel, userId);

        dashBoardPanel.setName("Dashboard");
        ordersPanel.setName("Orders");
        inventoryPanel.setName("Inventory");

        FilterButton filterButton = new FilterButton();
        JLabel removeFilter = new JLabel("Remove Filter");

        removeFilter.setBounds(275, 13, 100, 20);
        removeFilter.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(Font.PLAIN, 12f));
        removeFilter.setForeground(new Color(0x58A558));

        removeFilter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dashBoardPanel.setNeedsRefresh(true);
                dashBoardPanel.refreshMealsDisplay();
            }
        });

        filterButton.addActionListener(e -> {
            new SortingFrame(dashBoardPanel);
        });

        RefreshButton refreshButton = new RefreshButton();

        refreshButton.addActionListener(e -> {
            dashBoardPanel.refreshMealsDisplay();
            inventoryPanel.refresh();
            ordersPanel.loadOrdersFromDatabase();
        });

        LogOutButton logOutButton = new LogOutButton();

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        EmployeeFrame.this, // Use the EmployeeFrame as the parent component
                        "Are you sure you want to logout?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        UserSessionManager.markAsLoggedOut();
                        new FigmaToCodeApp(); // Redirect to the login screen
                        dispose(); // Close the EmployeeFrame
                    } catch (IOException | FontFormatException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                EmployeeFrame.this,
                                "An error occurred while logging out.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        // Set button properties

        // Set up card layout and add panels
        cardPanel.add(dashBoardPanel, "Dashboard");
        cardPanel.add(new OrdersPanel(dashBoardPanel, inventoryPanel, userId), "Orders");
        cardPanel.add(new InventoryPanel(), "Inventory");
        cardPanel.add(new SettingsPanel(this, getLoggedInUserName(), getLoggedInUserRole()),"Settings");
        cardPanel.setBounds(25, 40, 484, 318);

        // Image and layout setup
        greeneryImg.setBounds(11, 15, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryImg.setIcon(greeneryImage);

        RoundedTextField searchBar = new RoundedTextField();
        searchBar.setBounds(24, 10, 210, 21);
        searchBar.setBackground(Color.WHITE);
        searchBar.setForeground(Color.BLACK);
        searchBar.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(10f));
        searchBar.setOpaque(false);
        searchBar.setMargin(new Insets(0, 10, 0, 0));
        searchBar.setPlaceholder("Search");

        searchBar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            private void performSearch() {
                String searchText = searchBar.getText();

                // Check if the search bar is empty or contains the placeholder
                if (searchText.equals(searchBar.getPlaceholder())) {
                    // Restore all meals
                    System.out.println("Search is empty. Reloading full meal data.");
                    dashBoardPanel.setNeedsRefresh(true);
                    dashBoardPanel.refreshMealsDisplay();// This should reload all meals
                    return;
                }

                // Perform the search only if there's valid input
                System.out.println("Searching in panel: " + activePanelName + " with text: " + searchText);
                switch (activePanelName) {
                    case "Dashboard":
                        dashBoardPanel.search(searchText); // Perform search in dashboard panel
                        break;
                    case "Orders":
                        ordersPanel.search(searchText); // Perform search in orders panel
                        break;
                    case "Inventory":
                        inventoryPanel.search(searchText); // Perform search in inventory panel
                        break;
                    default:
                        System.out.println("Search not supported for panel: " + activePanelName);
                        break;
                }
            }

        });

        // Panel setup
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        //Panel Layouts
        panel1.setLayout(null);
        panel2.setLayout(null);
        panel3.setLayout(null);

        //Panel Backgrounds
        panel1.setBackground(new Color(0xE8E8E8));
        panel2.setBackground(Color.WHITE);
        panel3.setBackground(new Color(0xE8E8E8));

        //Panel Sizes
        panel1.setPreferredSize(new Dimension(516, 358));
        panel2.setPreferredSize(new Dimension(105, 358));
        panel3.setBounds(0, 0, 516, 36);

        //Panel Components
        panel1.add(panel3, BorderLayout.NORTH);
        panel1.add(cardPanel);

        panel2.add(greeneryImg);


        panel3.add(searchBar);
        panel3.add(filterButton);
        panel3.add(refreshButton);
        panel3.add(removeFilter);
        panel3.add(userNameBox);
        panel3.add(logOutButton);

        // Buttons for navigation
        DashBoardButton dashBoardButton = new DashBoardButton(cardLayout, cardPanel, "Dashboard");
        dashBoardButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "Dashboard");
            activePanelName = "Dashboard";

            // Only refresh if needed
            if (dashBoardPanel.needsRefresh()) {
                dashBoardPanel.refreshMealsDisplay();
            }

            System.out.println("Switched to DashboardPanel");
        });


        panel2.add(dashBoardButton);

        OrdersButton ordersButton = new OrdersButton(cardLayout, cardPanel, "Orders");
        ordersButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "Orders");
            activePanelName = "Orders";
            ordersPanel.loadOrdersFromDatabase(); // Ensure orders data is refreshed
            System.out.println("Switched to OrdersPanel");
        });

        panel2.add(ordersButton);

        InventoryButton inventoryButton = new InventoryButton(cardLayout, cardPanel, "Inventory");
        inventoryButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "Inventory");
            activePanelName = "Inventory";
            inventoryPanel.refresh(); // Ensure inventory is refreshed
            System.out.println("Switched to InventoryPanel");
        });

        panel2.add(inventoryButton);

        SettingsButton settingsButton = new SettingsButton(cardLayout, cardPanel, "Settings");
        settingsButton.setBounds(12, 124, 86, 16);
        panel2.add(settingsButton);


        // Frame settings
        this.setTitle("Restaurant Management System");
        this.setIconImage(greeneryImage.getImage());
        this.add(panel1, BorderLayout.EAST);
        this.add(panel2, BorderLayout.WEST);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

        // Add a mouse listener to detect clicks outside the searchBar
        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!searchBar.getBounds().contains(e.getPoint()) && searchBar.isFocusable()) {
                    searchBar.setFocusable(false);
                    panel1.requestFocusInWindow(); // Transfer focus to another component
                }
            }
        };

        panel1.addMouseListener(clickListener);
        panel2.addMouseListener(clickListener);
        panel3.addMouseListener(clickListener);
    }

    public void searchMeals(String searchText) {
        String searchQuery = "%" + searchText.toLowerCase() + "%";

        String query = "SELECT Meals.meal_id, Meals.meal_name, Meals.meal_type, Meals.meal_category, Inventory.meal_price, Meals.image " +
                "FROM Meals INNER JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE LOWER(Meals.meal_name) LIKE ?";

        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, searchQuery); // Set the search text in the query

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                dashBoardPanel.contentPanel.removeAll(); // Clear current content
                while (resultSet.next()) {
                    int mealID = resultSet.getInt("meal_id");
                    String mealName = resultSet.getString("meal_name");
                    String mealType = resultSet.getString("meal_type");
                    String mealCategory = resultSet.getString("meal_category");
                    double mealPrice = resultSet.getDouble("meal_price");
                    byte[] imageBytes = resultSet.getBytes("image");

                    // Convert image bytes to an ImageIcon
                    ImageIcon mealImage = null;
                    if (imageBytes != null) {
                        mealImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(imageBytes)
                                .getScaledInstance(121, 91, Image.SCALE_SMOOTH));
                    }

                    // Add filtered meal to the dashboard
                    JPanel mealPanel = dashBoardPanel.createItemPanel(mealID, mealName, "₱" + mealPrice, mealImage, mealType, mealCategory);
                    dashBoardPanel.contentPanel.add(mealPanel);
                }

                dashBoardPanel.contentPanel.revalidate();
                dashBoardPanel.contentPanel.repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentPanelName() {
        return activePanelName;
    }

    public int getUserId() {
        return userId;
    }

    private String getLoggedInUserName() {
        // Fetch the logged-in user's name from the database
        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement stmt = connection.prepareStatement("SELECT username FROM Users WHERE id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown User";
    }

    private String getLoggedInUserRole() {
        // Fetch the logged-in user's role from the database
        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement stmt = connection.prepareStatement("SELECT role FROM Users WHERE id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Role";
    }

}
