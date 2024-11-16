package org.example.Panels;

import org.example.Frames.ViewFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DashBoardPanel extends JScrollPane {
    public final JPanel contentPanel;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private static final Map<Integer, ImageIcon> imageCache = new HashMap<>();

    public DashBoardPanel() {
        contentPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        contentPanel.setBackground(new Color(0xE8E8E8));
        contentPanel.setDoubleBuffered(true);

        this.setViewportView(contentPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        loadDataInBackground();
    }

    /**
     * Loads meal data asynchronously using SwingWorker.
     */
    public void loadDataInBackground() {
        SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String query = "SELECT * FROM Meals";
                try (Connection connection = DriverManager.getConnection(DB_URL);
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {

                    while (resultSet.next()) {
                        int mealId = resultSet.getInt("meal_id");
                        String mealName = resultSet.getString("meal_name");
                        String mealPrice = displayPrice(mealId);
                        byte[] imageBytes = resultSet.getBytes("image");

                        ImageIcon mealImage = imageCache.getOrDefault(mealId, getImageIcon(imageBytes));
                        imageCache.put(mealId, mealImage);

                        JPanel mealPanel = createItemPanel(mealId, mealName, mealPrice, mealImage);
                        publish(mealPanel); // Send panel to the UI thread
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void process(java.util.List<JPanel> panels) {
                for (JPanel panel : panels) {
                    contentPanel.add(panel);
                }
            }

            @Override
            protected void done() {
                contentPanel.revalidate();
                contentPanel.repaint();
                System.out.println("Dashboard Refreshed");
            }
        };
        worker.execute();
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
        nameLabel.setFont(new Font("Actor", Font.PLAIN, 14));
        nameLabel.setBounds(10, 111, 100, 18);
        itemPanel.add(nameLabel);

        JLabel priceLabel = new JLabel(itemPrice);
        priceLabel.setFont(new Font("Actor", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0x4CAF50));
        priceLabel.setBounds(10, 136, 70, 15);
        itemPanel.add(priceLabel);

        JButton viewButton = new JButton("View");
        viewButton.setFont(new Font("Actor", Font.PLAIN, 10));
        viewButton.setBackground(new Color(0x4CAF50));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBounds(10, 161, 130, 15);

        viewButton.addActionListener(e -> new ViewFrame(mealID));
        itemPanel.add(viewButton);

        return itemPanel;
    }

    /**
     * Retrieves the price for a meal from the Inventory table.
     */
    public String displayPrice(int meal_id) {
        String query = "SELECT meal_price FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, meal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "₱" + resultSet.getString("meal_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "₱0.00";
    }

    /**
     * Refreshes the displayed meals by clearing and reloading the data.
     */
    public void refreshMealsDisplay() {
        System.out.println("Refreshing meals display...");
        imageCache.clear(); // Clear image cache to ensure updated images
        contentPanel.removeAll();
        loadDataInBackground();
    }
}
