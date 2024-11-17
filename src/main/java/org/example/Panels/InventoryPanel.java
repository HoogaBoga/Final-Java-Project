package org.example.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends JPanel {
    private JTable inventoryTable; // Declare the variables here
    private JTable orderHistoryTable;
    private JTable supplierListTable;

    public InventoryPanel() {
        setLayout(new BorderLayout());

        // ORDER AND SUPPLIER HISTORY LIST
        String[] orderHistoryColumns = {"Item", "Stocks"};
        String[][] orderHistoryData = {
                {"Pancit Bihon", "0"},
                {"Pork", "0"},
        };
        orderHistoryTable = new JTable(orderHistoryData, orderHistoryColumns);

        // SUPPLIER LIST
        String[] supplierListColumns = {"Supplier", "Item", "Contact"};
        String[][] supplierListData = {
                {"Mr. Shein", "Pancit Bihon", "093425342"},
                // ... other suppliers
        };
        supplierListTable = new JTable(supplierListData, supplierListColumns);

        // INVENTORY LIST
        String[] inventoryColumns = {"Item", "Meal ID", "Category", "Date", "Price", "Quantity"};
        DefaultTableModel inventoryModel = new DefaultTableModel(inventoryColumns, 0);
        // Populate the inventory model with data
        inventoryTable = new JTable(inventoryModel);

        // Create a panel to hold the Order History and Supplier List tables
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(new JScrollPane(orderHistoryTable));
        topPanel.add(new JScrollPane(supplierListTable));

        // Add components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Greenery Restaurant Inventory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        InventoryPanel panel = new InventoryPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}


