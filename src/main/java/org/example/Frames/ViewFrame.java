package org.example.Frames;

import org.example.Misc.DatabaseManager;
import org.example.Panels.DashBoardPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ViewFrame extends JFrame {

    private JPanel contentPanel;
    private final Map<Integer, ImageIcon> imageCache = new HashMap<>();
    private DashBoardPanel dashBoardPanel;
    private int userID;

    public ViewFrame(int mealID, DashBoardPanel dashBoardPanel, int userId) {

        this.dashBoardPanel = dashBoardPanel;
        this.userID = userId;

        setTitle("Meal Viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setSize(300, 520); // Slightly increased height to accommodate the Save button
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
        loadMeals(mealID);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void loadMeals(int mealID) {
        contentPanel.removeAll();
        String query = "SELECT * FROM Meals WHERE meal_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int mealId = resultSet.getInt("meal_id");
                String mealCategory = resultSet.getString("meal_category");
                String spicy = resultSet.getString("spicy_or_not_spicy");
                String mealType = resultSet.getString("meal_type");
                String mealName = resultSet.getString("meal_name");
                String mealPrice = displayPrice(mealId);
                String mealQuantity = displayAmount(mealID);
                byte[] imageBytes = resultSet.getBytes("image");
                int mealServing = resultSet.getInt("serving_size");
                int nutrition = resultSet.getInt("nutritional_value");

                ImageIcon mealImage = imageCache.computeIfAbsent(mealId, id -> getImageIcon(imageBytes));
                contentPanel.add(createItemPanel(mealId, mealName, mealCategory, spicy, mealType, mealPrice, mealQuantity, mealImage, mealServing, nutrition));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String displayPrice(int mealId) {
        String query = "SELECT meal_price FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "\u20B1" + resultSet.getString("meal_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "\u20B10.00";
    }

    public String displayAmount(int mealId) {
        String query = "SELECT quantity FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "0";
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

    private JPanel createItemPanel(int mealId, String mealName, String mealCategory, String spicy, String mealType, String mealPrice, String mealQuantity, ImageIcon mealImage, int servingSize, int nutrition) {
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(null);
        mealPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mealPanel.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel(mealImage);
        imageLabel.setBounds(5, 0, 274, 149);
        mealPanel.add(imageLabel);

        JLabel nameLabel = new JLabel(mealName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setBounds(25, 125, 200, 50);
        mealPanel.add(nameLabel);

        JLabel typeLabel = new JLabel(mealType);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        typeLabel.setForeground(new Color(0x2F632C));
        typeLabel.setBounds(25, 142, 100, 50);
        mealPanel.add(typeLabel);

        JLabel spicyLabel = new JLabel(spicy);
        spicyLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        spicyLabel.setForeground(new Color(0xE5380C));
        spicyLabel.setBounds(25, 154, 100, 50);
        mealPanel.add(spicyLabel);

        JLabel categoryLabel = new JLabel(mealCategory);
        categoryLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        categoryLabel.setBounds(25, 166, 100, 50);
        mealPanel.add(categoryLabel);

        JLabel mealLabel = new JLabel(mealPrice);
        mealLabel.setForeground(new Color(0, 128, 0));
        mealLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mealLabel.setBounds(25, 210, 100, 50);
        mealPanel.add(mealLabel);

        JLabel quantityLabel = new JLabel("Stocks Left: " + mealQuantity);
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        quantityLabel.setBounds(200, 215, 100, 50);
        mealPanel.add(quantityLabel);

        JLabel servingLabel = new JLabel("Serving Size: " + servingSize + " grams");
        servingLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        servingLabel.setBounds(25, 178, 200, 50);
        mealPanel.add(servingLabel);

        JLabel nutritionLabel = new JLabel("Nutritional Value: " + nutrition + " calories");
        nutritionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        nutritionLabel.setBounds(25, 190, 200, 50);
        mealPanel.add(nutritionLabel);

        JLabel edit = new JLabel("Edit");
        edit.setFont(new Font("Arial", Font.PLAIN, 12));
        edit.setForeground(new Color(0x65B265));
        edit.setBounds(250, 125, 100, 50);
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new EditFrame(mealId, dashBoardPanel, ViewFrame.this, userID);
            }
        });

        mealPanel.add(edit);

        // Ingredients table
        String[] columnNames = {"Ingredients", "Volume", "Unit"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ingredientTable = new JTable(tableModel);
        ingredientTable.setPreferredScrollableViewportSize(new Dimension(250, 50));
        ingredientTable.setFillsViewportHeight(false);

        JScrollPane tableScroll = new JScrollPane(ingredientTable);
        tableScroll.setBounds(7, 250, 274, 180);
        mealPanel.add(tableScroll);

        // Load ingredients and split by '|'
        String ingredientsString = getIngredients(mealId);
        if (ingredientsString != null && !ingredientsString.isEmpty()) {
            String[] ingredientsArray = ingredientsString.split(",");
            for (String ingredientEntry : ingredientsArray) {
                String[] parts = ingredientEntry.split("\\|");
                String ingredient = parts.length > 0 ? parts[0] : "";
                String volume = parts.length > 1 ? parts[1] : "";
                String unit = parts.length > 2 ? parts[2] : "";
                tableModel.addRow(new Object[]{ingredient, volume, unit});
            }
        }

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(100, 450, 100, 30);
        saveButton.setBackground(new Color(0x58A558));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveIngredients(mealId, ingredientTable));
        mealPanel.add(saveButton);

        return mealPanel;
    }


    private void saveIngredients(int mealId, JTable ingredientTable) {
        StringBuilder ingredientsBuilder = new StringBuilder();
        DefaultTableModel tableModel = (DefaultTableModel) ingredientTable.getModel();

        // Loop through the table rows to construct the ingredients string
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String ingredient = (String) tableModel.getValueAt(i, 0);
            String volume = (String) tableModel.getValueAt(i, 1);
            String unit = (String) tableModel.getValueAt(i, 2);

            // Skip empty rows
            if (ingredient == null || ingredient.trim().isEmpty()) {
                continue;
            }

            // Add to the string, separated by '|'
            ingredientsBuilder.append(ingredient.trim()).append("|")
                    .append(volume == null ? "" : volume.trim()).append("|")
                    .append(unit == null ? "" : unit.trim()).append(",");
        }

        // Remove trailing comma
        if (ingredientsBuilder.length() > 0) {
            ingredientsBuilder.setLength(ingredientsBuilder.length() - 1);
        }

        String ingredientsString = ingredientsBuilder.toString();

        // Update the database
        String query = "UPDATE Meals SET ingredients = ? WHERE meal_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ingredientsString); // Save the updated ingredients string
            preparedStatement.setInt(2, mealId);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Ingredients saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save ingredients. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String getIngredients(int mealId) {
        String query = "SELECT ingredients FROM Meals WHERE meal_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
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
}
