package org.example.Panels;

import org.example.Frames.AddMealFrame;
import org.example.Frames.HomeFrame;
import org.example.Frames.ViewFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DashBoardPanel extends JScrollPane {
    public final JPanel contentPanel;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private static final Map<Integer, ImageIcon> imageCache = new ConcurrentHashMap<>(); // Thread-safe cache
    private final Map<Integer, JPanel> mealPanelCache = new ConcurrentHashMap<>(); // Cache for meal panels

    public DashBoardPanel() {
        contentPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        contentPanel.setBackground(new Color(0xE8E8E8));
        contentPanel.setDoubleBuffered(true);

        this.setViewportView(contentPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        loadDataInBackground(null, null, null);
    }

    /**
     * Loads meal data asynchronously using SwingWorker.
     */
    public void loadDataInBackground(List<String> categories, String spiciness, String diet) {
        SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                contentPanel.removeAll(); // Clear UI before loading
                StringBuilder query = new StringBuilder("SELECT Meals.meal_id, Meals.meal_name, Meals.image, Inventory.meal_price FROM Meals ");
                query.append("LEFT JOIN Inventory ON Meals.meal_id = Inventory.meal_id WHERE 1=1");

                // Dynamically add filters
                if (categories != null && !categories.isEmpty()) {
                    query.append(" AND meal_category IN (")
                            .append("?,".repeat(categories.size()))
                            .deleteCharAt(query.length() - 1) // Remove trailing comma
                            .append(")");
                }
                if (spiciness != null) {
                    query.append(" AND spicy_or_not_spicy = ?");
                }
                if (diet != null) {
                    query.append(" AND meal_type = ?");
                }

                try (Connection connection = DriverManager.getConnection(DB_URL);
                     PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                    int paramIndex = 1;

                    if (categories != null && !categories.isEmpty()) {
                        for (String category : categories) {
                            preparedStatement.setString(paramIndex++, category);
                        }
                    }
                    if (spiciness != null) {
                        preparedStatement.setString(paramIndex++, spiciness);
                    }
                    if (diet != null) {
                        preparedStatement.setString(paramIndex++, diet);
                    }

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int mealId = resultSet.getInt("meal_id");
                        String mealName = resultSet.getString("meal_name");
                        String mealPrice = "₱" + resultSet.getDouble("meal_price");
                        byte[] imageBytes = resultSet.getBytes("image");

                        ImageIcon mealImage = imageCache.computeIfAbsent(mealId, id -> getImageIcon(imageBytes));

                        JPanel mealPanel = mealPanelCache.computeIfAbsent(mealId, id -> createItemPanel(mealId, mealName, mealPrice, mealImage));
                        publish(mealPanel);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void process(List<JPanel> panels) {
                for (JPanel panel : panels) {
                    contentPanel.add(panel);
                }

            }

            @Override
            protected void done() {
                contentPanel.revalidate();
                contentPanel.repaint();
                System.out.println("Dashboard refreshed with filters.");
            }
        };
        worker.execute();
    }

    /**
     * Retrieves meal data (meal ID and meal name) from the database.
     */
    public Map<Integer, String> getMealData() {
        Map<Integer, String> meals = new HashMap<>();
        String query = "SELECT meal_id, meal_name FROM Meals";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int mealId = resultSet.getInt("meal_id");
                String mealName = resultSet.getString("meal_name");
                meals.put(mealId, mealName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meals;
    }

    /**
     * Converts byte array to ImageIcon with scaling.
     */
    private ImageIcon getImageIcon(byte[] imageBytes) {
        if (imageBytes == null) return new ImageIcon();
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return new ImageIcon(img.getScaledInstance(121, 91, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }

    /**
     * Creates a panel for a single meal item.
     */
    public JPanel createItemPanel(int mealID, String itemName, String itemPrice, ImageIcon imageIcon) {
        JPanel itemPanel = new JPanel(null);
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setPreferredSize(new Dimension(147, 191));
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JLabel imageLabel = new JLabel(imageIcon, JLabel.CENTER);
        imageLabel.setBounds(10, 10, 121, 91);
        itemPanel.add(imageLabel);

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 14f));
        nameLabel.setBounds(10, 111, 100, 18);
        itemPanel.add(nameLabel);

        JLabel priceLabel = new JLabel(itemPrice);
        priceLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        priceLabel.setForeground(new Color(0x4CAF50));
        priceLabel.setBounds(10, 130, 100, 20);
        itemPanel.add(priceLabel);

        JLabel lowInventory = new JLabel("Low inventory");
        lowInventory.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.ITALIC, 8f));
        lowInventory.setForeground(Color.RED);
        lowInventory.setBounds(10, 143, 100, 20);
        lowInventory.setVisible(false);
        itemPanel.add(lowInventory);

        JLabel noStock = new JLabel("No Stock");
        noStock.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.ITALIC, 8f));
        noStock.setForeground(Color.RED);
        noStock.setBounds(10, 143, 100, 20);
        noStock.setVisible(false);
        itemPanel.add(noStock);

        if(getAmount(mealID) <= 5 && getAmount(mealID) > 0){
            lowInventory.setVisible(true);
        }

        if(getAmount(mealID) <= 0){
            lowInventory.setVisible(false);
            noStock.setVisible(true);
        }

        JButton viewButton = new JButton("View");
        viewButton.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 10f));
        viewButton.setBackground(new Color(0x4CAF50));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBounds(10, 161, 130, 15);

        viewButton.addActionListener(e -> new ViewFrame(mealID, this));
        itemPanel.add(viewButton);

        return itemPanel;
    }

    /**
     * Refreshes the displayed meals by clearing and reloading the data without filters.
     */
    public void refreshMealsDisplay() {
        SwingUtilities.invokeLater(() -> {
            // Clear caches and UI before loading new data
            imageCache.clear();
            mealPanelCache.clear();
            contentPanel.removeAll();
            contentPanel.revalidate();
            contentPanel.repaint();

            // Reload meals in the background
            loadDataInBackground(null, null, null);
        });
    }

    public int getAmount(int meal_ID){
        String query = "SELECT quantity FROM Inventory WHERE meal_id = ?";

        int amount = 0;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, meal_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                amount = resultSet.getInt("quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amount;
    }

    public int getLoggedInUserId() {
        String query = "SELECT id FROM Users WHERE is_logged_in = 1";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if no user is logged in
    }
}