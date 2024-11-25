package org.example.Panels;

import org.example.Misc.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryPanel extends JScrollPane {

    private DefaultTableModel lowInventoryModel;
    private DefaultTableModel outofStocksModel;
    private DefaultTableModel inventoryModel;
    private JComboBox<String> sortComboBox; // Sorting dropdown
    private String currentSort = "name_asc"; // Default sort order

    public InventoryPanel() {
        // Main panel with BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Sorting options
        JPanel sortingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortingPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel sortLabel = new JLabel("Sort by:");
        sortComboBox = new JComboBox<>(new String[]{"Name (A-Z)", "Name (Z-A)", "Stock (Low to High)", "Stock (High to Low)"});
        sortComboBox.addActionListener(e -> {
            String selectedSort = (String) sortComboBox.getSelectedItem();
            switch (selectedSort) {
                case "Name (A-Z)":
                    currentSort = "name_asc";
                    break;
                case "Name (Z-A)":
                    currentSort = "name_desc";
                    break;
                case "Stock (Low to High)":
                    currentSort = "stock_asc";
                    break;
                case "Stock (High to Low)":
                    currentSort = "stock_desc";
                    break;
            }
            refresh(); // Refresh the panel with the selected sorting option
        });
        sortingPanel.add(sortLabel);
        sortingPanel.add(sortComboBox);

        // Low Inventory Section
        JPanel lowInventoryPanel = new JPanel(new BorderLayout());
        lowInventoryPanel.setBorder(BorderFactory.createTitledBorder("Low Inventory"));
        String[] lowInventoryColumns = {"Item", "Stocks"};
        lowInventoryModel = new DefaultTableModel(lowInventoryColumns, 0);
        JTable lowInventoryTable = new JTable(lowInventoryModel);
        lowInventoryTable.setPreferredScrollableViewportSize(new Dimension(300, 150));
        lowInventoryPanel.add(new JScrollPane(lowInventoryTable), BorderLayout.CENTER);

        // Out of Stocks Section
        JPanel outofStocksPanel = new JPanel(new BorderLayout());
        outofStocksPanel.setBorder(BorderFactory.createTitledBorder("Out of Stocks"));
        String[] outofStocksColumns = {"Meal Name"};
        outofStocksModel = new DefaultTableModel(outofStocksColumns, 0);
        JTable outofStocksTable = new JTable(outofStocksModel);
        outofStocksTable.setPreferredScrollableViewportSize(new Dimension(300, 150));
        outofStocksPanel.add(new JScrollPane(outofStocksTable), BorderLayout.CENTER);

        // Inventory Section
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        String[] inventoryColumns = {"Meal Name", "Meal Category", "Price", "Quantity"};
        inventoryModel = new DefaultTableModel(inventoryColumns, 0);
        JTable inventoryTable = new JTable(inventoryModel);
        inventoryTable.setPreferredScrollableViewportSize(new Dimension(300, 150));
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Add panels to the main panel
        mainPanel.add(sortingPanel); // Add sorting panel
        mainPanel.add(lowInventoryPanel);  // First panel
        mainPanel.add(Box.createVerticalStrut(10)); // Add spacing
        mainPanel.add(outofStocksPanel);    // Second panel
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(inventoryPanel);    // Third panel

        // Add the main panel to this JScrollPane
        setViewportView(mainPanel);

        // Load initial data
        refresh();
    }

    // Method to refresh all data in the tables
    public void refresh() {
        System.out.println("Refreshing InventoryPanel...");
        clearTableData();

        // Query to load all inventory data with sorting
        String inventoryQuery = getInventoryQueryWithSorting();
        loadAndCategorizeData(inventoryQuery);
        revalidate();
        repaint();
        System.out.println("InventoryPanel refresh complete.");
    }

    // Generate the inventory query based on the selected sorting option
    private String getInventoryQueryWithSorting() {
        String orderBy;
        switch (currentSort) {
            case "name_asc":
                orderBy = "LOWER(Meals.meal_name) ASC"; // Case-insensitive ascending sort
                break;
            case "name_desc":
                orderBy = "LOWER(Meals.meal_name) DESC"; // Case-insensitive descending sort
                break;
            case "stock_asc":
                orderBy = "Inventory.quantity ASC";
                break;
            case "stock_desc":
                orderBy = "Inventory.quantity DESC";
                break;
            default:
                orderBy = "LOWER(Meals.meal_name) ASC"; // Default case-insensitive ascending sort
                break;
        }
        return "SELECT Meals.meal_name, Meals.meal_category, Inventory.meal_price, Inventory.quantity " +
                "FROM Meals " +
                "LEFT JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "ORDER BY " + orderBy;
    }

    // Method to load data and categorize items
    private void loadAndCategorizeData(String query) {
        try (Connection connection = DatabaseManager.getConnection();

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String mealName = resultSet.getString("meal_name");
                String mealCategory = resultSet.getString("meal_category");
                double price = resultSet.getDouble("meal_price");
                int quantity = resultSet.getInt("quantity");

                if (quantity == 0) {
                    // Move to Out of Stocks model
                    outofStocksModel.addRow(new Object[]{mealName});
                } else if (quantity <= 5) {
                    // Move to Low Inventory model
                    lowInventoryModel.addRow(new Object[]{mealName, quantity});
                } else {
                    // Keep in Inventory model
                    inventoryModel.addRow(new Object[]{mealName, mealCategory, price, quantity});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    // Method to clear all table data
    private void clearTableData() {
        lowInventoryModel.setRowCount(0);
        outofStocksModel.setRowCount(0);
        inventoryModel.setRowCount(0);
    }

    public void search(String searchText) {
        String query = "%" + searchText.toLowerCase() + "%";

        // Search query to filter all data
        String searchQuery = "SELECT Meals.meal_name, Meals.meal_category, Inventory.meal_price, Inventory.quantity " +
                "FROM Meals " +
                "LEFT JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE LOWER(Meals.meal_name) LIKE ? OR LOWER(Meals.meal_category) LIKE ? " +
                "ORDER BY " + (currentSort.equals("name_asc") ? "Meals.meal_name ASC" : currentSort.equals("name_desc") ? "Meals.meal_name DESC" : currentSort.equals("stock_asc") ? "Inventory.quantity ASC" : "Inventory.quantity DESC");

        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {

            preparedStatement.setString(1, query);
            preparedStatement.setString(2, query);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                clearTableData(); // Clear all models before applying search

                while (resultSet.next()) {
                    String mealName = resultSet.getString("meal_name");
                    String mealCategory = resultSet.getString("meal_category");
                    double price = resultSet.getDouble("meal_price");
                    int quantity = resultSet.getInt("quantity");

                    if (quantity == 0) {
                        // Add to Out of Stocks model
                        outofStocksModel.addRow(new Object[]{mealName});
                    } else if (quantity <= 5) {
                        // Add to Low Inventory model
                        lowInventoryModel.addRow(new Object[]{mealName, quantity});
                    } else {
                        // Add to Inventory model
                        inventoryModel.addRow(new Object[]{mealName, mealCategory, price, quantity});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
