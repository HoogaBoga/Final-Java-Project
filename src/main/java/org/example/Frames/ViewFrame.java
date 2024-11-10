package org.example.Frames;

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
        setSize(403, 646);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
        loadMeals(mealID);
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
        mealPanel.setLayout(new BorderLayout());
        mealPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel imageLabel = new JLabel(mealImage);
        mealPanel.add(imageLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(mealName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel priceLabel = new JLabel(mealPrice);
        priceLabel.setForeground(new Color(0, 128, 0));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> new ViewFrame(mealId)); // Pass mealId
        infoPanel.add(viewButton);

        mealPanel.add(infoPanel, BorderLayout.CENTER);
        return mealPanel;
    }


//    public static void main(String[] args){
//        new ViewFrame();
//     }
}
