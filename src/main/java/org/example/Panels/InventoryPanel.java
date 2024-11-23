package org.example.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryPanel extends JScrollPane {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private DefaultTableModel lowInventoryModel;
    private DefaultTableModel outofStocksModel;
    private DefaultTableModel inventoryModel;

    public InventoryPanel() {
        // Main panel with BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

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
        // Clear existing data from all tables
        System.out.println("Refreshing InventoryPanel...");
        clearTableData();

        // Reload data for Low Inventory Table
        String lowInventoryQuery = "SELECT Meals.meal_name, Inventory.quantity " +
                "FROM Meals " +
                "INNER JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE Inventory.quantity <= 5 AND Inventory.quantity > 0";
        loadData(lowInventoryQuery, lowInventoryModel);

        // Reload data for Out of Stocks Table
        String outofStocksQuery = "SELECT Meals.meal_name " +
                "FROM Meals " +
                "INNER JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE Inventory.quantity = 0";
        loadData(outofStocksQuery, outofStocksModel);

        // Reload data for Inventory Table
        String inventoryQuery = "SELECT Meals.meal_name, Meals.meal_category, Inventory.meal_price, Inventory.quantity " +
                "FROM Meals " +
                "LEFT JOIN Inventory ON Meals.meal_id = Inventory.meal_id";
        loadData(inventoryQuery, inventoryModel);

        // Revalidate and repaint to update the UI
        revalidate();
        repaint();
        System.out.println("InventoryPanel refresh complete.");
    }

    // Method to clear all table data
    private void clearTableData() {
        // Clear Low Inventory Table
        lowInventoryModel.setRowCount(0);

        // Clear Out of Stocks Table
        outofStocksModel.setRowCount(0);

        // Clear Inventory Table
        inventoryModel.setRowCount(0);
    }

    // Method to load data into a table model from a query
    private static void loadData(String query, DefaultTableModel model) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int columnCount = model.getColumnCount();
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }
}
