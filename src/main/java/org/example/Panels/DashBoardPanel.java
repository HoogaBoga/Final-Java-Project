package org.example.Panels;

import org.example.Frames.ViewFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardPanel extends JScrollPane {
    private JPanel contentPanel;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private Map<Integer, ImageIcon> imageCache;// Cache for images to avoid reloading

    public DashBoardPanel() {
        contentPanel = new JPanel(new GridLayout(0, 3, 12, 12));
        contentPanel.setBackground(new Color(0xE8E8E8));
        contentPanel.setDoubleBuffered(true);
        this.setViewportView(contentPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        imageCache = new HashMap<>();

        // Start background loading
        loadDataInBackground();
    }

    private void loadDataInBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                loadMeals(); // Load meals in the background
                return null;
            }

            @Override
            protected void done() {
                // Ensure UI updates happen on the EDT
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Revalidate and repaint the contentPanel to reflect the changes
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                });
            }
        };
        worker.execute(); // Start the SwingWorker
    }


    private void loadMeals() {
        contentPanel.removeAll();
        String query = "SELECT * FROM Meals";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int mealId = resultSet.getInt("meal_id");
                String mealName = resultSet.getString("meal_name");
                String mealPrice = displayPrice(mealId);
                byte[] imageBytes = resultSet.getBytes("image");

                System.out.println("Loaded Meal: " + mealId + ", " + mealName + ", " + mealPrice);

                ImageIcon mealImage = imageCache.computeIfAbsent(mealId, id -> getImageIcon(imageBytes));
                contentPanel.add(createItemPanel(mealId, mealName, mealPrice, mealImage));
                System.out.println("Added Meal Panel to Content Panel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon getImageIcon(byte[] imageBytes) {
        if (imageBytes == null) return new ImageIcon(); // Default empty icon if no image
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return new ImageIcon(img.getScaledInstance(121, 91, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }

    private JPanel createItemPanel(int mealID, String itemName, String itemPrice, ImageIcon imageIcon) {
        JPanel itemPanel = new JPanel(null);
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setPreferredSize(new Dimension(147, 191));
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JLabel imageLabel = new JLabel(imageIcon, JLabel.CENTER);
        imageLabel.setBounds(10, 10, 121, 91);
        itemPanel.add(imageLabel);

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        nameLabel.setBounds(10, 111, 100, 18);
        itemPanel.add(nameLabel);

        JLabel priceLabel = new JLabel(itemPrice);
        priceLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0x4CAF50));
        priceLabel.setBounds(10, 136, 70, 15);
        itemPanel.add(priceLabel);

        JButton viewButton = new JButton("View");
        viewButton.setBackground(new Color(0x4CAF50));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBounds(10, 161, 130, 15);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewFrame(mealID);
            }
        });

        itemPanel.add(viewButton);

        return itemPanel;
    }

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

    public void refreshMealsDisplay() {
        // Clear the cache of images to prevent reloading the same image data
        imageCache.clear();

        // Clear existing items from the panel before reloading new ones
        contentPanel.removeAll();

        // Reapply the layout to ensure proper arrangement of components
        contentPanel.setLayout(new GridLayout(0, 3, 12, 12));

        // Log for debugging purposes to ensure the method is called
        System.out.println("Refreshing meals display...");

        // Load the data again in the background, ensuring the UI is updated after the task
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                loadMeals(); // Load meals in the background
                return null;
            }

            @Override
            protected void done() {

                // Ensure UI updates happen on the Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Revalidate and repaint the contentPanel to reflect the changes
                        contentPanel.revalidate();
                        contentPanel.repaint();

                        // Revalidate and repaint the JScrollPane itself to make sure scroll updates
                        DashBoardPanel.this.revalidate();
                        DashBoardPanel.this.repaint();
                    }
                });
            }
        };

        // Start the SwingWorker in the background to refresh the meal data
        worker.execute();
    }


    public List<String> getMealNames() {
        List<String> mealNames = new ArrayList<>();
        String query = "SELECT meal_name FROM Meals";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String mealName = resultSet.getString("meal_name");
                mealNames.add(mealName);  // Add each meal name to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mealNames;  // Return the list of meal names
    }

}
