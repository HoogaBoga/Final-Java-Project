package org.example.Frames;

import org.example.Panels.DashBoardPanel;
import org.sqlite.core.DB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private Map<Integer, ImageIcon> imageCache = new HashMap<>();

    private static final String DB_URL = "jdbc:sqlite:/Users/matty/IdeaProjects/Final-Java-Project/Database.db";

    public ViewFrame(int mealID) {
        setTitle("Meal Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setSize(300, 480);
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
        loadMeals(mealID);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

    }

    public void loadMeals(int mealID) {
        contentPanel.removeAll();
        String query = "SELECT * FROM Meals WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

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

                ImageIcon mealImage = imageCache.computeIfAbsent(mealId, id -> getImageIcon(imageBytes));
                contentPanel.add(createItemPanel(mealId, mealName, mealCategory, spicy, mealType, mealPrice, mealQuantity, mealImage));
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

    public String displayAmount(int mealId) {
        String query = "SELECT quantity FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
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

    private JPanel createItemPanel(int mealId, String mealName, String mealCategory, String spicy, String mealType, String mealPrice, String mealQuantity, ImageIcon mealImage) {
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(null);
        mealPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mealPanel.setBackground(Color.WHITE);

        JLabel imageLabel = new JLabel(mealImage);
        imageLabel.setBounds(5, 0, 274, 149);
        mealPanel.add(imageLabel);

        JLabel nameLabel = new JLabel(mealName);
        nameLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.BOLD, 18f));
        nameLabel.setBounds(25, 125, 200, 50);
        mealPanel.add(nameLabel);

        JLabel typeLabel = new JLabel(mealType);
        typeLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.ITALIC, 11f));
        typeLabel.setForeground(new Color(0x2F632C));
        typeLabel.setBounds(25, 142, 100, 50);
        mealPanel.add(typeLabel);

        JLabel spicyLabel = new JLabel(spicy);
        spicyLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.ITALIC, 11f));
        spicyLabel.setForeground(new Color(0xE5380C));
        spicyLabel.setBounds(25, 154, 100, 50);
        mealPanel.add(spicyLabel);

        JLabel categoryLabel = new JLabel(mealCategory);
        categoryLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.ITALIC, 11f));
        categoryLabel.setBounds(25, 166, 100, 50);
        mealPanel.add(categoryLabel);

        JLabel mealLabel = new JLabel(mealPrice);
        mealLabel.setForeground(new Color(0, 128, 0));
        mealLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.BOLD, 18f));
        mealLabel.setBounds(25, 190, 100, 50);
        mealPanel.add(mealLabel);

        JLabel quantityLabel = new JLabel("Stocks Left: " + mealQuantity);
        quantityLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 11f));
        quantityLabel.setBounds(200, 215, 100, 50);
        mealPanel.add(quantityLabel);

        JLabel edit = new JLabel("Edit");
        edit.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        edit.setForeground(new Color(0x65B265));
        edit.setBounds(250, 125, 100, 50);
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DashBoardPanel dashBoardPanel = new DashBoardPanel();
                new EditFrame(mealId, dashBoardPanel);
            }
        });

        mealPanel.add(edit);



        // Ingredients table
        String[] columnNames = {"Ingredients", "Volume", "Unit"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ingredientTable = new JTable(tableModel);
        ingredientTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
        ingredientTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(ingredientTable);
        tableScroll.setBounds(7, 250, 274, 200);
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
