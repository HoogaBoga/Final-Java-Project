package org.example.Misc;

import java.sql.*;

public class CrudInventory {
    private static final String DB_URL = "jdbc:sqlite:/Users/matty/IdeaProjects/Final-Java-Project/Database.db";

    public void addInventory(int quantity, double mealPrice, int mealID) {
        String insertSQL = "INSERT INTO Inventory (quantity, meal_price, meal_id) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            // Set PRAGMA journal_mode to WAL to help with database locking issues
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
                stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setDouble(2, mealPrice);
                preparedStatement.setInt(3, mealID);

                preparedStatement.executeUpdate();
                System.out.println("Inventory added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editInventory(int quantity, double meal_price, int meal_id) {
        String updateSQL = "UPDATE Inventory SET quantity = ?, meal_price = ? WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            // Set PRAGMA journal_mode to WAL to help with database locking issues
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
                stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setDouble(2, meal_price);
                preparedStatement.setInt(3, meal_id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Inventory updated successfully!");
                } else {
                    System.out.println("No inventory found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInventory(int meal_id) {
        String deleteSQL = "DELETE FROM Inventory WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            // Set PRAGMA journal_mode to WAL to help with database locking issues
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
                stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, meal_id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Inventory entry for meal ID " + meal_id + " deleted successfully!");
                } else {
                    System.out.println("No inventory entry found for meal ID " + meal_id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listInventory() throws SQLException {
        String query = "SELECT * FROM Inventory";
        StringBuilder inventorylist = new StringBuilder("Inventory:\n");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            // Set PRAGMA journal_mode to WAL to help with database locking issues
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
                stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
            }

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    inventorylist.append("ID: ").append(resultSet.getInt("id"))
                            .append("\n Quantity: ").append(resultSet.getInt("quantity"))
                            .append("\n Meal ID: ").append(resultSet.getInt("meal_id"))
                            .append("\n");
                }
                System.out.println(inventorylist);
            }
        }
    }
}
