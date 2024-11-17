package org.example.Misc;

import org.example.Panels.DashBoardPanel;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudMeal {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private DashBoardPanel dashBoardPanel;

    public CrudMeal(DashBoardPanel dashBoardPanel) {
        this.dashBoardPanel = dashBoardPanel;  // Set the DashBoardPanel
    }

    public int addMeal(String mealName, String category, int servingSize, String type, int nutritionalValue,
                       String spice, String ingredients, File imageFile) {
        String insertSQL = "INSERT INTO Meals (meal_name, meal_category, serving_size, meal_type, nutritional_value, "
                + "spicy_or_not_spicy, ingredients, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Set PRAGMA settings
            setPragmas(connection);

            preparedStatement.setString(1, mealName);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, servingSize);
            preparedStatement.setString(4, type);
            preparedStatement.setInt(5, nutritionalValue);
            preparedStatement.setString(6, spice);
            preparedStatement.setString(7, ingredients);

            // Convert the image file to a byte array if it exists
            if (imageFile != null && imageFile.exists()) {
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    byte[] imageData = fis.readAllBytes();
                    preparedStatement.setBytes(8, imageData);
                }
            } else {
                preparedStatement.setNull(8, java.sql.Types.BLOB);
            }

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // Retrieve the generated meal_id
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int mealId = generatedKeys.getInt(1);
                        JOptionPane.showMessageDialog(null, "Meal added successfully! Meal ID: " + mealId, "Success", JOptionPane.INFORMATION_MESSAGE);

                        List<String> categories = new ArrayList<>();
                        categories.add(category); // Add the single category to the list
                        dashBoardPanel.refreshMealsDisplay();
                        dashBoardPanel.loadDataInBackground(categories, spice, type);
                        return mealId; // Return the generated meal_id
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add meal. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    public void editMeal(int mealId, String mealName, String mealCategory, Integer servingSize, String mealType,
                         Integer nutritionalValue, String spicyOrNotSpicy, String ingredients, File imageFile) {
        // Query to fetch current meal details
        String fetchSQL = "SELECT * FROM Meals WHERE meal_id = ?";
        // Query to update the meal
        String updateSQL = "UPDATE Meals SET meal_name = ?, meal_category = ?, serving_size = ?, meal_type = ?, " +
                "nutritional_value = ?, spicy_or_not_spicy = ?, ingredients = ?, image = ? WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Set PRAGMA settings
            setPragmas(connection);

            // Fetch the current values of the meal
            String currentMealName = null;
            String currentMealCategory = null;
            int currentServingSize = 0;
            String currentMealType = null;
            int currentNutritionalValue = 0;
            String currentSpicyOrNotSpicy = null;
            String currentIngredients = null;
            byte[] currentImage = null;

            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchSQL)) {
                fetchStatement.setInt(1, mealId);
                try (ResultSet resultSet = fetchStatement.executeQuery()) {
                    if (resultSet.next()) {
                        currentMealName = resultSet.getString("meal_name");
                        currentMealCategory = resultSet.getString("meal_category");
                        currentServingSize = resultSet.getInt("serving_size");
                        currentMealType = resultSet.getString("meal_type");
                        currentNutritionalValue = resultSet.getInt("nutritional_value");
                        currentSpicyOrNotSpicy = resultSet.getString("spicy_or_not_spicy");
                        currentIngredients = resultSet.getString("ingredients");
                        currentImage = resultSet.getBytes("image");
                    } else {
                        JOptionPane.showMessageDialog(null, "No meal found with the provided ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Use provided values or fallback to current values
            mealName = (mealName != null && !mealName.isEmpty()) ? mealName : currentMealName;
            mealCategory = (mealCategory != null && !mealCategory.isEmpty()) ? mealCategory : currentMealCategory;
            servingSize = (servingSize != null) ? servingSize : currentServingSize;
            mealType = (mealType != null && !mealType.isEmpty()) ? mealType : currentMealType;
            nutritionalValue = (nutritionalValue != null) ? nutritionalValue : currentNutritionalValue;
            spicyOrNotSpicy = (spicyOrNotSpicy != null && !spicyOrNotSpicy.isEmpty()) ? spicyOrNotSpicy : currentSpicyOrNotSpicy;
            ingredients = (ingredients != null && !ingredients.isEmpty()) ? ingredients : currentIngredients;
            byte[] imageBytes = (imageFile != null && imageFile.exists()) ? Files.readAllBytes(imageFile.toPath()) : currentImage;

            // Update the meal with the resolved values
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                updateStatement.setString(1, mealName);
                updateStatement.setString(2, mealCategory);
                updateStatement.setInt(3, servingSize);
                updateStatement.setString(4, mealType);
                updateStatement.setInt(5, nutritionalValue);
                updateStatement.setString(6, spicyOrNotSpicy);
                updateStatement.setString(7, ingredients);
                if (imageBytes != null) {
                    updateStatement.setBytes(8, imageBytes);
                } else {
                    updateStatement.setNull(8, Types.BLOB);
                }
                updateStatement.setInt(9, mealId);

                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Meal updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashBoardPanel.refreshMealsDisplay();
                } else {
                    JOptionPane.showMessageDialog(null, "No meal found with the provided ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update meal. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void deleteMeal(int meal_id) {
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this meal?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // Proceed if user selects 'Yes'
        if (confirm == JOptionPane.YES_OPTION) {
            String deleteSQL = "DELETE FROM Meals WHERE meal_id = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

                // Set PRAGMA settings
                setPragmas(connection);

                preparedStatement.setInt(1, meal_id);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Meal with ID " + meal_id + " deleted successfully.");
                    JOptionPane.showMessageDialog(null, "Meal Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashBoardPanel.refreshMealsDisplay();
                } else {
                    System.out.println("No meal found with ID " + meal_id);
                    JOptionPane.showMessageDialog(null, "Meal was not Deleted!", "Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while deleting the meal.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User chose 'No', so don't delete
            System.out.println("Meal deletion canceled.");
            JOptionPane.showMessageDialog(null, "Meal deletion canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void listMeals() throws SQLException {
        String query = "SELECT * FROM Meals";
        StringBuilder mealList = new StringBuilder("Meals:\n");
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Set PRAGMA settings
            setPragmas(connection);

            while (resultSet.next()) {
                mealList.append("ID: ").append(resultSet.getInt("meal_id"))
                        .append("\n Meal Name: ").append(resultSet.getString("meal_name"))
                        .append("\n Meal Category: ").append(resultSet.getString("meal_category"))
                        .append("\n Serving Size: ").append(resultSet.getString("serving_size"))
                        .append("\n Meal Type: ").append(resultSet.getString("meal_type"))
                        .append("\n Nutritional Value: ").append(resultSet.getInt("nutritional_value"))
                        .append("\n Spicy: ").append(resultSet.getString("spicy_or_not_spicy"))
                        .append("\n Meal Price: ").append(resultSet.getString("meal_price"))
                        .append("\n Ingredients: ").append(resultSet.getString("ingredients")).append("\n");
            }

            // Print the meal list to the console
            System.out.println(mealList.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPragmas(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
            stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
        }
    }
}
