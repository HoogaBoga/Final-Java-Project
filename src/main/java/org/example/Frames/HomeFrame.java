package org.example.Frames;

import org.example.Buttons.*;
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

public class HomeFrame extends JFrame {
    private DashBoardPanel dashBoardPanel;
    private InventoryPanel inventoryPanel;
    public JPanel cardPanel;
    private int userId;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public HomeFrame(int userId) throws IOException, SQLException {
        this.userId = userId;
        ImageIcon greeneryImage = new ImageIcon(Objects.requireNonNull(HomeFrame.class.getResource("/Frame 12.png")));
        JLabel greeneryImg = new JLabel();

        UserNameBox userNameBox = new UserNameBox(userId);

        CardLayout cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize DashBoardPanel and buttons
        dashBoardPanel = new DashBoardPanel();
        inventoryPanel = new InventoryPanel();
        PlusAddButton plusAddButton = new PlusAddButton(dashBoardPanel);
        FilterButton filterButton = new FilterButton();
        JLabel removeFilter = new JLabel("Remove Filter");

        removeFilter.setBounds(285, 13, 100, 20);
        removeFilter.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(Font.PLAIN, 12f));
        removeFilter.setForeground(new Color(0x58A558));

        removeFilter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
        });

        LogOutButton logOutButton = new LogOutButton();
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserSessionManager.markAsLoggedOut();
                    new FigmaToCodeApp();
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (FontFormatException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        // Set button properties
        plusAddButton.setBounds(37, 308, 31, 31);
        plusAddButton.setOpaque(false);

        // Set up card layout and add panels
        cardPanel.add(dashBoardPanel, "Dashboard");
        cardPanel.add(new OrdersPanel(dashBoardPanel, inventoryPanel), "Orders");
        cardPanel.add(new InventoryPanel(), "Inventory");
        cardPanel.add(new SalesPanel(), "Sales");
        cardPanel.add(new PromotionsPanel(), "Promotions");
        cardPanel.add(new SettingsPanel(), "Settings");
        cardPanel.setBounds(25, 40, 484, 318);

        // Image and layout setup
        greeneryImg.setBounds(11, 15, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryImg.setIcon(greeneryImage);

        RoundedTextField searchBar = new RoundedTextField();
        searchBar.setBounds(24, 10, 220, 21);
        searchBar.setBackground(Color.WHITE);
        searchBar.setForeground(Color.BLACK);
        searchBar.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(10f));
        searchBar.setOpaque(false);
        searchBar.setMargin(new Insets(0, 10, 0, 0));
        searchBar.setPlaceholder("Search");

        searchBar.addActionListener(e -> searchMeals(searchBar.getText()));

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
        panel2.add(plusAddButton);

        panel3.add(searchBar);
        panel3.add(filterButton);
        panel3.add(refreshButton);
        panel3.add(removeFilter);
        panel3.add(userNameBox);
        panel3.add(logOutButton);

        // Buttons for navigation
        DashBoardButton dashBoardButton = new DashBoardButton(cardLayout, cardPanel, "Dashboard");
        panel2.add(dashBoardButton);

        OrdersButton ordersButton = new OrdersButton(cardLayout, cardPanel, "Orders");
        panel2.add(ordersButton);

        InventoryButton inventoryButton = new InventoryButton(cardLayout, cardPanel, "Inventory");
        panel2.add(inventoryButton);

        SalesButton salesButton = new SalesButton(cardLayout, cardPanel, "Sales");
        panel2.add(salesButton);

        PromotionsButton promotionsButton = new PromotionsButton(cardLayout, cardPanel, "Promotions");
        panel2.add(promotionsButton);

        SettingsButton settingsButton = new SettingsButton(cardLayout, cardPanel, "Settings");
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

        String query = "SELECT Meals.meal_id, Meals.meal_name, Inventory.meal_price, Meals.image " +
                "FROM Meals INNER JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE LOWER(Meals.meal_name) LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, searchQuery); // Set the search text in the query

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                dashBoardPanel.contentPanel.removeAll(); // Clear current content
                while (resultSet.next()) {
                    int mealID = resultSet.getInt("meal_id");
                    String mealName = resultSet.getString("meal_name");
                    double mealPrice = resultSet.getDouble("meal_price");
                    byte[] imageBytes = resultSet.getBytes("image");

                    // Convert image bytes to an ImageIcon
                    ImageIcon mealImage = null;
                    if (imageBytes != null) {
                        mealImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(imageBytes)
                                .getScaledInstance(121, 91, Image.SCALE_SMOOTH));
                    }

                    // Add filtered meal to the dashboard
                    JPanel mealPanel = dashBoardPanel.createItemPanel(mealID, mealName, "₱" + mealPrice, mealImage);
                    dashBoardPanel.contentPanel.add(mealPanel);
                }

                dashBoardPanel.contentPanel.revalidate();
                dashBoardPanel.contentPanel.repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserId() {
        return userId;
    }
}
