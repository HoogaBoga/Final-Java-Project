    package org.example;

    import org.example.Frames.FigmaToCodeApp;
    import org.example.Misc.CrudMeal;

    import java.awt.*;
    import java.io.IOException;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;


    public class Main {

        private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

        public static void main(String[] args) throws IOException, FontFormatException {

            new FigmaToCodeApp();


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
                    + "id INTEGER PRIMARY KEY, "
                    + "user_id INTEGER NOT NULL, "
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
                    + "ingredients TEXT NOT NULL, "
                    + "image BLOB)";

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
                    + "meal_price INTEGER NOT NULL, "
                    + "meal_id INTEGER, "
                    + "FOREIGN KEY (meal_id) REFERENCES Meals(meal_id) ON DELETE CASCADE ON UPDATE CASCADE)";

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

        private static void addUser(String username, String password, String role) {
            String insertSQL = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User '" + username + "' added successfully!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static boolean authenticateUser(String username, String password) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        public class SalesAnalysis {

            private static final String DB_URL = "jdbc:sqlite:C:/Users/stakezy/IdeaProjects/MavenProject/Database.db";

            public void getTopSellingMeals() {
                String query = "SELECT meal_id, SUM(quantity) AS total_quantity FROM Sales " +
                        "GROUP BY meal_id ORDER BY total_quantity DESC LIMIT 5";

                try (Connection connection = DriverManager.getConnection(DB_URL);
                     PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        System.out.println("Meal ID: " + resultSet.getInt("meal_id") +
                                ", Total Sold: " + resultSet.getInt("total_quantity"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void getSalesByDateRange(String startDate, String endDate) {
                String query = "SELECT sales_date, SUM(amount) AS total_sales FROM Sales " +
                        "WHERE sales_date BETWEEN ? AND ? GROUP BY sales_date";

                try (Connection connection = DriverManager.getConnection(DB_URL);
                     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                    preparedStatement.setString(1, startDate);
                    preparedStatement.setString(2, endDate);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        System.out.println("Date: " + resultSet.getString("sales_date") +
                                ", Total Sales: " + resultSet.getDouble("total_sales"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void getLowSellingMeals() {
                String query = "SELECT meal_id, SUM(quantity) AS total_quantity FROM Sales " +
                        "GROUP BY meal_id ORDER BY total_quantity ASC LIMIT 5";

                try (Connection connection = DriverManager.getConnection(DB_URL);
                     PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        System.out.println("Meal ID: " + resultSet.getInt("meal_id") +
                                ", Total Sold: " + resultSet.getInt("total_quantity"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }