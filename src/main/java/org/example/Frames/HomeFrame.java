package org.example.Frames;

import org.example.Buttons.*;
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
    DashBoardPanel dashBoardPanel;
    private static final String DB_URL = "jdbc:sqlite:/Users/matty/IdeaProjects/Final-Java-Project/Database.db";


    public HomeFrame() throws IOException, SQLException {
        ImageIcon greeneryImage = new ImageIcon(Objects.requireNonNull(HomeFrame.class.getResource("/Frame 12.png")));
        JLabel greeneryImg = new JLabel();

        DashBoardPanel getMealName = new DashBoardPanel();

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        dashBoardPanel = new DashBoardPanel();
        PlusAddButton plusAddButton = new PlusAddButton(dashBoardPanel);
        FilterButton filterButton = new FilterButton();
        RefreshButton refreshButton = new RefreshButton();

        refreshButton.addActionListener(_ -> dashBoardPanel.refreshMealsDisplay());


        plusAddButton.setBounds(37, 308, 31, 31);

        plusAddButton.setOpaque(false);

        cardPanel.add(dashBoardPanel, "Dashboard");
        cardPanel.add(new OrdersPanel(), "Orders");
        cardPanel.add(new InventoryPanel(), "Inventory");
        cardPanel.add(new SalesPanel(), "Sales");
        cardPanel.add(new PromotionsPanel(), "Promotions");
        cardPanel.add(new SettingsPanel(), "Settings");
        cardPanel.setBounds(25, 40, 484, 318);

        ImageIcon greeneryyImage = new ImageIcon("Resources/Vector.png");
        greeneryImg.setBounds(11, 15, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryImg.setIcon(greeneryImage);

        RoundedTextField searchBar = new RoundedTextField();
        searchBar.setBounds(24, 10, 304, 21);
        searchBar.setBackground(Color.WHITE);
        searchBar.setForeground(Color.BLACK);
        searchBar.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(10f));
        searchBar.setOpaque(false);
        searchBar.setMargin(new Insets(0, 10, 0, 0));
        searchBar.setPlaceholder("Search");

        searchBar.addActionListener(e -> searchMeals(searchBar.getText()));


        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        panel1.setLayout(null);
        panel2.setLayout(null);
        panel3.setLayout(null);

        panel1.setBackground(new Color(0xE8E8E8));
        panel2.setBackground(Color.WHITE);
        panel3.setBackground(new Color(0xE8E8E8));

        panel1.setPreferredSize(new Dimension(516, 358));
        panel2.setPreferredSize(new Dimension(105, 358));
        panel3.setBounds(0, 0, 516, 36);

        panel2.add(greeneryImg);
        panel2.add(plusAddButton);
        panel1.add(panel3, BorderLayout.NORTH);
        panel3.add(searchBar);
        panel3.add(filterButton);
        panel3.add(refreshButton);
        panel1.add(cardPanel);

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

        this.setTitle("Restaurant Management System");
        this.setIconImage(greeneryyImage.getImage());
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
        // Convert the search text to lowercase for case-insensitive comparison
        String searchQuery = "%" + searchText.toLowerCase() + "%";

        // Query to search for meals with names matching the search text
        String query = "SELECT meal_name FROM Meals WHERE LOWER(meal_name) LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, searchQuery);  // Set the search text in the query

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String mealName = resultSet.getString("meal_name");
                    // Display or add meal names to a list in the UI
                    System.out.println("Found meal: " + mealName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws SQLException, IOException {

        new HomeFrame();
    }
}
