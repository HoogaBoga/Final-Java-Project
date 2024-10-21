package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudInventory {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/c202301028/IdeaProjects/MavenProject/spykeDatabase.db";

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
}
