package org.example.Frames;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class ViewFrame extends JScrollPane {

    private JScrollPane contentPanel = new JScrollPane();
    private Map<Integer, ImageIcon> imageCache;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public ViewFrame(){
        this.setSize(403, 646);
        this.setVisible(true);
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
//                contentPanel.add(createItemPanel(mealId, mealName, mealPrice, mealImage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}

