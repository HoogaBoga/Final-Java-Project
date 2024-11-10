package org.example.Frames;

import org.example.Panels.DashBoardPanel;
import org.sqlite.core.DB;

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

    public ViewFrame(int mealID) {
        setTitle("Meal Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setSize(300, 480);
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
        loadMeals(mealID);
        setUndecorated(true);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

    }

    private void loadMeals(int mealID) {
        contentPanel.removeAll();
        String query = "SELECT * FROM Meals WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, mealID);
            ResultSet resultSet = preparedStatement.executeQuery();

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
        mealPanel.setLayout(null);
        mealPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mealPanel.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel(mealImage);
        imageLabel.setBounds(15, 0, 274, 149);
        mealPanel.add(imageLabel);

        JLabel nameLabel = new JLabel(mealName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setBounds(50, 125, 100, 50);
        mealPanel.add(nameLabel);

        JLabel priceLabel = new JLabel(mealPrice);
        priceLabel.setForeground(new Color(0, 128, 0));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        priceLabel.setBounds(50, 150, 100, 50);
        mealPanel.add(priceLabel);

        // Ingredients table
        String[] columnNames = {"Ingredients", "Volume", "Unit"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ingredientTable = new JTable(tableModel);
        ingredientTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
        ingredientTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(ingredientTable);
        tableScroll.setBounds(15, 200, 274, 270);
        mealPanel.add(tableScroll);

        // Load and split ingredients from database string
        String ingredientsString = getIngredients(mealId); // Method to retrieve ingredients as a single string
        if (ingredientsString != null) {
            String[] ingredientsArray = ingredientsString.split(","); // Adjust delimiter if needed
            for (String ingredient : ingredientsArray) {
                // Add each ingredient with default "Volume" and "Unit" as editable fields
                tableModel.addRow(new Object[]{ingredient.trim(), "", ""});
            }
        }

        return mealPanel;
    }

    // Method to retrieve ingredients as a single string from the database
    private String getIngredients(int mealId) {
        String query = "SELECT ingredients FROM Meals WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ingredients");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args){
//        new ViewFrame();
//     }
}
