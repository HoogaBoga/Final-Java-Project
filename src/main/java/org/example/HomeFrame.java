package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;


public class HomeFrame extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public HomeFrame() throws IOException, SQLException {

        ImageIcon greeneryImage = new ImageIcon("Resources/Frame 12.png");
        JLabel greeneryImg = new JLabel();

        greeneryImg.setBounds(10,15, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryImg.setIcon(greeneryImage);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        panel2.setLayout(null);

        panel1.setBackground(new Color(0xE8E8E8));
        panel2.setBackground(Color.WHITE);

        panel1.setPreferredSize(new Dimension(516, 358));
        panel2.setPreferredSize(new Dimension(105, 358));

        panel2.add(greeneryImg);

        this.add(panel1, BorderLayout.EAST);
        this.add(panel2, BorderLayout.WEST);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws SQLException, IOException {
        new HomeFrame();
    }

//    public String listMeals() throws SQLException {
//        String query = "SELECT * FROM Meals";
//        StringBuilder mealList = new StringBuilder("Meals:\n");
//        try (Connection connection = DriverManager.getConnection(DB_URL);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            while (resultSet.next()) {
//                mealList.append("ID: ").append(resultSet.getInt("meal_id"))
//                        .append("\n Meal Name: ").append(resultSet.getString("meal_name"))
//                        .append("\n Meal Category: ").append(resultSet.getString("meal_category"))
//                        .append("\n Serving Size: ").append(resultSet.getString("meal_type"))
//                        .append("\n Meal Type: ").append(resultSet.getString("meal_type"))
//                        .append("\n Nutritional Value: ").append(resultSet.getString("nutritional_value"))
//                        .append("\n Spicy: ").append(resultSet.getString("spicy_or_not_spicy"))
//                        .append("\n Meal Price: ").append(resultSet.getString("meal_price"))
//                        .append("\n Ingredients: ").append(resultSet.getString("ingredients")).append("\n");
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return mealList.toString();
//    }
}
