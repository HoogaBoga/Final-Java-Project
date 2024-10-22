package org.example;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;


public class Newframe extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/c202301028/IdeaProjects/Final-Java-Project/Database.db";

    public Newframe() throws IOException, SQLException {

        JPanel panel1 = new JPanel();
        JTextArea textArea =  new JTextArea();

        textArea.setEditable(false);
        textArea.setText(listMeals());
        panel1.add(textArea);

        this.add(panel1);
        this.setVisible(true);
        this.pack();

    }

    public String listMeals() throws SQLException {
        String query = "SELECT * FROM Meals";
        StringBuilder mealList = new StringBuilder("Meals:\n");
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                mealList.append("ID: ").append(resultSet.getInt("meal_id"))
                        .append("\n Meal Name: ").append(resultSet.getString("meal_name"))
                        .append("\n Meal Category: ").append(resultSet.getString("meal_category"))
                        .append("\n Serving Size: ").append(resultSet.getString("meal_type"))
                        .append("\n Meal Type: ").append(resultSet.getString("meal_type"))
                        .append("\n Nutritional Value: ").append(resultSet.getString("nutritional_value"))
                        .append("\n Spicy: ").append(resultSet.getString("spicy_or_not_spicy"))
                        .append("\n Meal Price: ").append(resultSet.getString("meal_price"))
                        .append("\n Ingredients: ").append(resultSet.getString("ingredients")).append("\n");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mealList.toString();
    }
}
