package org.example;

import java.sql.*;

public class CrudInventory {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/c202301028/IdeaProjects/Final-Java-Project/Database.db";

    public void addInventory(int quantity,int mealID) {
        String insertSQL = "INSERT INTO Inventory (quantity, meal_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, mealID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editInventory(int inventory_id, int meal_id, int quantity) {
        String updateSQL = "UPDATE \"Inventory\" SET meal_id = ?, quantity = ? WHERE inventory_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setInt(1, meal_id);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setInt(3, inventory_id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inventory updated successfully!");
            } else {
                System.out.println("No inventory found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInventory(int inventory_id) {
        String deleteSQL = "DELETE FROM Inventory WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, inventory_id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inventory entry for meal ID " + inventory_id + " deleted successfully!");
            } else {
                System.out.println("No inventory entry found for meal ID " + inventory_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listInventory() throws SQLException {
        String query = "SELECT * FROM Inventory";
        StringBuilder inventorylist = new StringBuilder("Inventory:\n");
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                inventorylist.append("ID: ").append(resultSet.getInt("id"))
                        .append("\n Quantity: ").append(resultSet.getString("quantity"))
                        .append("\n Meal ID: ").append(resultSet.getString("meal_id"))
                        .append("\n");

            }
        }
    }
}
