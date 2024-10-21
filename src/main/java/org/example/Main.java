package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/Final-Java-Project/Database.db";

    public static void main(String[] args) {
        new MyFrame();

        createMealTable();
    }

    private static void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Users' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createOrdersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Orders ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL,"
                + "meal_id INTEGER NOT NULL, "
                + "order_quantity INTEGER NOT NULL, "
                + "order_date TEXT NOT NULL, "
                + "status INTEGER NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Orders' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createPromotionsTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Promotions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sales_ID INTEGER NOT NULL, "
                + "promotion_type TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "duration_in_days INTEGER NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Promotions' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createMealTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Meals ("
                + "meal_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "meal_name TEXT NOT NULL, "
                + "meal_category TEXT NOT NULL, "
                + "serving_size TEXT NOT NULL, "
                + "meal_type TEXT NOT NULL, "
                + "nutritional_value TEXT NOT NULL, "
                + "spicy_or_not_spicy TEXT NOT NULL, "
                + "meal_price TEXT NOT NULL, "
                + "ingredients TEXT NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Meals' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createInventoryTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Inventory ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "quantity INTEGER NOT NULL, "
                + "meal_id INTEGER NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Inventory' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void createSalesTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Sales ("
                + "sales_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "amount DECIMAL(10, 2) NOT NULL, "
                + "sales_date TEXT NOT NULL)";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);
            System.out.println("Table 'Sales' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}