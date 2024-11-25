package org.example.Misc;

import java.sql.*;

public class CrudInventory {

    public void addInventory(int quantity, double mealPrice, int mealID) {
        String insertSQL = "INSERT INTO Inventory (quantity, meal_price, meal_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
        ) {

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

    public void editInventory(Integer quantity, Double meal_price, int meal_id) {
        String fetchSQL = "SELECT quantity, meal_price FROM Inventory WHERE meal_id = ?";
        String updateSQL = "UPDATE Inventory SET quantity = ?, meal_price = ? WHERE meal_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
        ) {

            // Set PRAGMA journal_mode to WAL to help with database locking issues
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");  // Set journal mode to WAL
                stmt.execute("PRAGMA locking_mode=NORMAL;");  // Set locking mode to NORMAL (default)
            }

            Integer currentQuantity = null;
            Double currentMealPrice = null;

            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchSQL)) {
                fetchStatement.setInt(1, meal_id);
                try (ResultSet resultSet = fetchStatement.executeQuery()) {
                    if (resultSet.next()) {
                        currentQuantity = resultSet.getInt("quantity");
                        currentMealPrice = resultSet.getDouble("meal_price");
                    } else {
                        System.out.println("No inventory found with the provided ID.");
                        return; // Exit if no inventory is found
                    }
                }
            }

            // Use provided values or fallback to current values
            quantity = (quantity != null) ? quantity : currentQuantity;
            meal_price = (meal_price != null) ? meal_price : currentMealPrice;

            // Update the inventory with the resolved values
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                updateStatement.setInt(1, quantity);
                updateStatement.setDouble(2, meal_price);
                updateStatement.setInt(3, meal_id);

                int rowsAffected = updateStatement.executeUpdate();
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

        try (Connection connection = DatabaseManager.getConnection();
        ) {

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
        try (Connection connection = DatabaseManager.getConnection();
        ) {

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
