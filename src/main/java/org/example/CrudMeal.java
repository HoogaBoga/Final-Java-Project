package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CrudMeal {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/c202301028/IdeaProjects/Final-Java-Project/Database.db";

    public void addMeal(String mealName, String category,int servingSize, String type,int nutritionalValue, String
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
            JOptionPane.showMessageDialog(null, "Meal added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add meal. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editMeal(int meal_id, String meal_name, String meal_category,
                                 String meal_type, String nutritional_value,
                                 String spicy_or_not_spicy, float meal_price,
                                 String ingredients) {
        String updateSQL = "UPDATE Meals SET meal_name = ?, meal_category = ?, meal_type = ?, " +
                "nutritional_value = ?, spicy_or_not_spicy = ?, meal_price = ?, ingredients = ? " +
                "WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, meal_name);
            preparedStatement.setString(2, meal_category);
            preparedStatement.setString(3, meal_type);
            preparedStatement.setString(4, nutritional_value);
            preparedStatement.setString(5, spicy_or_not_spicy);
            preparedStatement.setFloat(6, meal_price);
            preparedStatement.setString(7, ingredients);
            preparedStatement.setInt(8, meal_id); // Use the provided meal ID

            int rowsAffected = preparedStatement.executeUpdate(); // Execute the update
            if (rowsAffected > 0) {
                System.out.println("Meal updated successfully!");
            } else {
                System.out.println("No meal found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeal(int meal_id) {
        String deleteSQL = "DELETE FROM Meals WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, meal_id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Meal with ID " + meal_id + " deleted successfully.");
            } else {
                System.out.println("No meal found with ID " + meal_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listMeals() throws SQLException {
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
        }
    }
}
