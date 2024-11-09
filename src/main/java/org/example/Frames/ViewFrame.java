package org.example.Frames;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

import java.util.HashMap;
import java.util.Map;

public class ViewFrame extends JFrame {

    private JPanel contentPanel;
    private Map<Integer, ImageIcon> imageCache = new HashMap<>();

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public ViewFrame() {
        setTitle("Meal Viewer");
        setSize(403, 646);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
        loadMeals();
        setVisible(true);

        setLocationRelativeTo(null);
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

                ImageIcon mealImage = imageCache.computeIfAbsent(mealId, id -> getImageIcon(imageBytes));
                contentPanel.add(createItemPanel(mealId, mealName, mealPrice, mealImage));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displayPrice(int mealId) {
        String query = "SELECT meal_price FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "₱" + resultSet.getString("meal_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "₱0.00";
    }

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

    private JPanel createItemPanel(int mealId, String mealName, String mealPrice, ImageIcon mealImage) {
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(new BorderLayout());
        mealPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top Section with Image
        JLabel imageLabel = new JLabel(mealImage);
        mealPanel.add(imageLabel, BorderLayout.NORTH);

        // Center Section with Name and Price
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(mealName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel priceLabel = new JLabel(mealPrice);
        priceLabel.setForeground(new Color(0, 128, 0)); // Green color for price
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        JButton editButton = new JButton("Edit");
        editButton.setForeground(new Color(0, 128, 0)); // Green color for Edit
        infoPanel.add(editButton);

        mealPanel.add(infoPanel, BorderLayout.CENTER);

        // Table for Ingredients with ScrollPane
        String[] columnNames = {"Ingredients", "Volume", "Unit"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ingredientTable = new JTable(tableModel);
        ingredientTable.setPreferredScrollableViewportSize(new Dimension(380, 100)); // Set preferred size for visibility

        JScrollPane tableScroll = new JScrollPane(ingredientTable);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Dummy data - replace with database query
        tableModel.addRow(new Object[]{"Pancit Bihon", "1", "pounds"});
        tableModel.addRow(new Object[]{"Pork", "1/2", "pounds"});
        tableModel.addRow(new Object[]{"Chicken", "1/2", "pounds"});
        tableModel.addRow(new Object[]{"Snow Peas", "1/8", "pounds"});
        tableModel.addRow(new Object[]{"Carrot", "1", "cup"});
        tableModel.addRow(new Object[]{"Cabbage", "1/2", "cup"});
        tableModel.addRow(new Object[]{"Chicken Powder", "1/2", "tbsp"});

        mealPanel.add(tableScroll, BorderLayout.SOUTH);

        // Footer Section with "View Inventory" and "Add" Button
        JPanel footerPanel = new JPanel();
        JButton viewInventoryButton = new JButton("View Inventory");
        JButton addButton = new JButton("Add");

        footerPanel.add(viewInventoryButton);
        footerPanel.add(addButton);
        mealPanel.add(footerPanel, BorderLayout.PAGE_END);

        return mealPanel;
    }

    public void refreshMeasslsDisplay() {
        imageCache.clear();
        contentPanel.removeAll(); // Remove existing items from the panel
        contentPanel.revalidate();  // Refresh the layout
        contentPanel.repaint();
    }

     public static void main(String[] args){
        new ViewFrame();
     }
}
