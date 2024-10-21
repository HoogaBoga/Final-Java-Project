package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddMealTable {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/c202301028/IdeaProjects/MavenProject/spykeDatabase.db";
    public void addMeal (String mealName, String category,int servingSize, String type,int nutritionalValue, String
            spice,double mealPrice, String ingredients) {
        String insertSQL = "INSERT INTO Meals (meal_name, meal_category, serving_size, meal_type, nutritional_value, spicy_or_not_spicy, meal_price, ingredients) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, mealName);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, servingSize);
            preparedStatement.setString(4, type);
            preparedStatement.setInt(5, nutritionalValue);
            preparedStatement.setString(6, spice);
            preparedStatement.setDouble(7, mealPrice);
            preparedStatement.setString(8, ingredients);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
