// OrdersPanel.java (Updated with Missing Methods Restored)
package org.example.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.Date;

import org.example.Buttons.AddImageLabel;
import org.example.Buttons.DashBoardButton;
import org.example.Frames.AddMealFrame;
import org.example.Frames.FigmaToCodeApp;
import org.example.Frames.HomeFrame;
import org.example.Frames.ViewFrame;
import org.example.Misc.UserSessionManager;

public class OrdersPanel extends JPanel {
    private DashBoardPanel dashBoardPanel;
    private InventoryPanel inventoryPanel;
    private JTable ordersTable;
    private DefaultTableModel model;
    private JPanel activityPanel;
    private JLabel noActivityLabel;
    private Queue<JPanel> activityQueue;
    private HomeFrame parentFrame;
    private Set<String> generatedOrderIDs;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";
    private int useriD;

    public OrdersPanel(DashBoardPanel dashBoardPanel, InventoryPanel inventoryPanel, int usersID) {
        this.dashBoardPanel = dashBoardPanel;
        this.parentFrame = parentFrame;
        this.inventoryPanel = inventoryPanel;
        this.activityQueue = new LinkedList<>();
        this.generatedOrderIDs = new HashSet<>();
        this.useriD = usersID;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);



        // Activity Section
        activityPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        noActivityLabel = new JLabel("No Recent Activity", SwingConstants.CENTER);
        noActivityLabel.setFont(new Font("Actor", Font.ITALIC, 14));
        noActivityLabel.setForeground(Color.GRAY);
        activityPanel.add(noActivityLabel);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Orders");
        headerLabel.setFont(new Font("Actor", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLACK);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JButton addOrderButton = new JButton("Make an Order");
        addOrderButton.setFont(new Font("Actor", Font.PLAIN, 14));
        addOrderButton.setBackground(Color.decode("#987284"));
        addOrderButton.setForeground(Color.WHITE);
        addOrderButton.setFocusPainted(false);

        addOrderButton.addActionListener(e -> makeOrder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addOrderButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Orders Table
        String[] columnNames = {"Order ID", "User ID", "Meal Details", "Date", "Total Price", "Status"};
        model = new DefaultTableModel(columnNames, 0);

        ordersTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable.setFont(new Font("Actor", Font.PLAIN, 12));
        ordersTable.setRowHeight(40);

        JTableHeader header = ordersTable.getTableHeader();
        header.setFont(new Font("Actor", Font.BOLD, 16));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(0, 128, 0));
        header.setBorder(BorderFactory.createEmptyBorder());
        ordersTable.setShowGrid(false);

        ordersTable.setDefaultRenderer(Object.class, new MultiLineTableCellRenderer());

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(14f));
        updateStatusButton.setBackground(Color.decode("#4CAF50"));
        updateStatusButton.setForeground(Color.WHITE);
        updateStatusButton.setFocusPainted(false);

        updateStatusButton.addActionListener(e -> updateOrderStatus());

        JPanel statusButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusButtonPanel.setBackground(Color.WHITE);
        statusButtonPanel.add(updateStatusButton);

        JButton deleteOrderButton = new JButton("Delete Order");
        deleteOrderButton.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(14f));
        deleteOrderButton.setBackground(new Color(0xFF7979));
        deleteOrderButton.setForeground(Color.WHITE);
        deleteOrderButton.setFocusPainted(false);
        statusButtonPanel.add(deleteOrderButton);

        deleteOrderButton.addActionListener(e -> deleteOrder());

        // Apply MultiLineTableCellRenderer to the Meal Details column
        TableColumn mealDetailsColumn = ordersTable.getColumnModel().getColumn(2); // Index 2 is Meal Details
        mealDetailsColumn.setCellRenderer(new MultiLineTableCellRenderer());
        mealDetailsColumn.setPreferredWidth(100); // Wrap text at a width of ~200px

// Adjust auto-resize mode
        ordersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Refresh table layout
        ordersTable.revalidate();
        ordersTable.repaint();


        contentPanel.add(activityPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(statusButtonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        loadOrdersFromDatabase();
    }

    private void makeOrder() {
        String customerName = JOptionPane.showInputDialog(this, "Enter customer name:", "Customer Name", JOptionPane.PLAIN_MESSAGE);
        if (customerName == null || customerName.trim().isEmpty()) return;

        Map<Integer, Integer> selectedMeals = new HashMap<>(); // Meal ID -> Quantity mapping
        boolean addMoreMeals = true;

        while (addMoreMeals) {
            Map<Integer, String> meals = dashBoardPanel.getMealData(); // Meal ID -> Meal Name mapping
            String[] mealOptions = meals.values().toArray(new String[0]);

            String selectedMeal = (String) JOptionPane.showInputDialog(this,
                    "Select a food item:",
                    "Food Item",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    mealOptions,
                    mealOptions[0]);
            if (selectedMeal == null) break;

            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "Quantity", JOptionPane.PLAIN_MESSAGE);
            if (quantityStr == null || quantityStr.trim().isEmpty()) break;

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            int mealID = Integer.parseInt(getMealID(selectedMeal));
            if (mealID == -1) {
                JOptionPane.showMessageDialog(this, "Invalid meal selection.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // **Validate Inventory Stock**
            int availableStock = getMealStock(String.valueOf(mealID));
            if (quantity > availableStock) {
                JOptionPane.showMessageDialog(this, "Not enough stock for " + selectedMeal + ". Available: " + availableStock, "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            selectedMeals.put(mealID, selectedMeals.getOrDefault(mealID, 0) + quantity);
            dashBoardPanel.setNeedsRefresh(true);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Do you want to add another meal to this order?",
                    "Add Another Meal",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                addMoreMeals = false;
            }
        }

        if (selectedMeals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No meals selected for the order.", "No Meals Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userID = UserSessionManager.getLoggedInUserID();
        createOrderWithItems(userID, selectedMeals);

        // **Call updateActivity() here**
        for (Map.Entry<Integer, Integer> entry : selectedMeals.entrySet()) {
            String mealName = dashBoardPanel.getMealData().get(entry.getKey());
            int quantity = entry.getValue();
            updateActivity(customerName, mealName + " x" + quantity);
        }

        JOptionPane.showMessageDialog(this, "Order created successfully.", "Order Created", JOptionPane.INFORMATION_MESSAGE);
        loadOrdersFromDatabase();
    }



    private void createOrderWithItems(int userID, Map<Integer, Integer> items) {
        String insertOrderQuery = "INSERT INTO Orders (user_id, order_date, status) VALUES (?, datetime('now', 'localtime'), 'Pending')";
        String insertOrderItemsQuery = "INSERT INTO OrderItems (order_id, meal_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
        String updateInventoryQuery = "UPDATE Inventory SET quantity = quantity - ? WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.setAutoCommit(false); // Enable transaction

            int orderID;
            // Insert the order and get its ID
            try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userID);
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    orderID = rs.getInt(1); // Get the generated order ID
                } else {
                    connection.rollback();
                    throw new SQLException("Failed to create order.");
                }
            }

            // Insert items into OrderItems and update inventory
            try (PreparedStatement itemStmt = connection.prepareStatement(insertOrderItemsQuery);
                 PreparedStatement inventoryStmt = connection.prepareStatement(updateInventoryQuery)) {

                for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
                    int mealID = entry.getKey();
                    int quantity = entry.getValue();

                    // **Validate Inventory Stock**
                    int availableStock = getMealStock(String.valueOf(mealID));
                    if (quantity > availableStock) {
                        connection.rollback();
                        throw new SQLException("Not enough stock for Meal ID: " + mealID);
                    }

                    double price = getMealPrice(mealID); // Get price from Inventory
                    double subtotal = price * quantity;

                    // Insert into OrderItems
                    itemStmt.setInt(1, orderID);
                    itemStmt.setInt(2, mealID);
                    itemStmt.setInt(3, quantity);
                    itemStmt.setDouble(4, subtotal);
                    itemStmt.addBatch();

                    // Update Inventory
                    inventoryStmt.setInt(1, quantity); // Subtract quantity
                    inventoryStmt.setInt(2, mealID);
                    inventoryStmt.addBatch();
                }

                itemStmt.executeBatch();
                inventoryStmt.executeBatch();
            }

            connection.commit(); // Commit the transaction
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create order. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getMealPrice(int mealID) throws SQLException {
        String query = "SELECT meal_price FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, mealID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("meal_price");
            }
        }
        throw new SQLException("Meal not found in Inventory.");
    }

    private void insertOrderIntoDatabase(String userID, String mealID, int quantity, String orderDate) {
        String query = "INSERT INTO Orders (user_id, meal_id, order_quantity, order_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(userID));
            preparedStatement.setInt(2, Integer.parseInt(mealID));
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, orderDate);
            preparedStatement.setString(5, "Pending");

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateInventory(String mealID, int newStock) {
        String query = "UPDATE Inventory SET quantity = ? WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, newStock);
            preparedStatement.setInt(2, Integer.parseInt(mealID));

            preparedStatement.executeUpdate();

            inventoryPanel.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getMealStock(String mealID) {
        String query = "SELECT quantity FROM Inventory WHERE meal_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(mealID));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getLatestOrderID() {
        String query = "SELECT id FROM Orders ORDER BY id DESC LIMIT 1";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return String.valueOf(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadOrdersFromDatabase() {
        String query = "SELECT Orders.id AS order_id, Orders.user_id, Orders.order_date, Orders.status, " +
                "GROUP_CONCAT(Meals.meal_name || ' (x' || OrderItems.quantity || ')', ', ') AS meal_details, " +
                "SUM(OrderItems.quantity * Inventory.meal_price) AS total_price " +
                "FROM Orders " +
                "JOIN OrderItems ON Orders.id = OrderItems.order_id " +
                "JOIN Meals ON OrderItems.meal_id = Meals.meal_id " +
                "JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "GROUP BY Orders.id";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            model.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int userId = resultSet.getInt("user_id");
                String mealDetails = resultSet.getString("meal_details");
                String orderDate = resultSet.getString("order_date");
                double totalPrice = resultSet.getDouble("total_price");
                String status = resultSet.getString("status");

                model.addRow(new Object[]{orderId, userId, mealDetails, orderDate, totalPrice, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void updateActivity(String customerName, String foodItem) {
        if (noActivityLabel != null) {
            activityPanel.remove(noActivityLabel);
            noActivityLabel = null;
        }

        JPanel newActivity = createActivityCard(customerName, "Ordered " + foodItem, "Just now");

        if (activityQueue.size() == 2) {
            JPanel oldestActivity = activityQueue.poll();
            activityPanel.remove(oldestActivity);
        }

        activityQueue.add(newActivity);
        activityPanel.add(newActivity);

        activityPanel.revalidate();
        activityPanel.repaint();
    }

    private JPanel createActivityCard(String customerName, String action, String timeAgo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel customerLabel = new JLabel(customerName);
        JLabel actionLabel = new JLabel(action);
        JLabel timeLabel = new JLabel(timeAgo);

        customerLabel.setFont(new Font("Actor", Font.PLAIN, 14));
        actionLabel.setFont(new Font("Actor", Font.PLAIN, 14));
        actionLabel.setForeground(new Color(0, 128, 0));
        timeLabel.setFont(new Font("Actor", Font.ITALIC, 12));
        timeLabel.setForeground(Color.GRAY);

        card.add(customerLabel, BorderLayout.NORTH);
        card.add(actionLabel, BorderLayout.CENTER);
        card.add(timeLabel, BorderLayout.SOUTH);

        return card;
    }

    private void updateOrderStatus() {
        int[] selectedRows = ordersTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select one or more orders to update.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loggedInUserId = UserSessionManager.getLoggedInUserID();
        for (int selectedRow : selectedRows) {
            Object userIdObj = model.getValueAt(selectedRow, 1); // Column 1 is User ID
            int orderUserId = Integer.parseInt(userIdObj.toString());

            if (orderUserId != loggedInUserId) {
                JOptionPane.showMessageDialog(this, "You can only edit your own orders.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        for (int selectedRow : selectedRows) {
            int orderId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString()); // Column 0 is Order ID
            String currentStatus = model.getValueAt(selectedRow, 5).toString(); // Column 5 is Status
            String newStatus = "Pending".equals(currentStatus) ? "Paid" : "Pending";

            model.setValueAt(newStatus, selectedRow, 5);
            updateOrderStatusInDatabase(orderId, newStatus);

            // **Call updateActivity() here**
            String mealDetails = model.getValueAt(selectedRow, 2).toString(); // Column 2 is Meal Details
            updateActivity("Order #" + orderId, "Status updated to " + newStatus + ": " + mealDetails);
        }

        JOptionPane.showMessageDialog(this, "Order statuses updated successfully.", "Status Updated", JOptionPane.INFORMATION_MESSAGE);
    }




    private void updateOrderStatusInDatabase(int orderId, String newStatus) {
        String query = "UPDATE Orders SET status = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, orderId);
            preparedStatement.setInt(3, UserSessionManager.getLoggedInUserID()); // Ensure user owns the order
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(this, "You can only update your own orders.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update order status for Order ID: " + orderId, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }





    private String getMealID(String mealName) {
        Map<Integer, String> meals = dashBoardPanel.getMealData();
        for (Map.Entry<Integer, String> entry : meals.entrySet()) {
            if (entry.getValue().equals(mealName)) {
                return String.valueOf(entry.getKey());
            }
        }
        return null;
    }

    private String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    private String[] getDays(int maxDays) {
        String[] days = new String[maxDays];
        for (int i = 1; i <= maxDays; i++) {
            days[i - 1] = String.valueOf(i);
        }
        return days;
    }

    private String[] getYears() {
        String[] years = new String[10];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        return years;
    }

    private void deleteOrder() {
        int[] selectedRows = ordersTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select one or more orders to delete.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loggedInUserId = UserSessionManager.getLoggedInUserID(); // Get logged-in user's ID

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected orders?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int rowIndex = selectedRows[i];

            // Retrieve the User ID and Order ID for the selected row
            int orderId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString()); // Column 0 is Order ID
            int userId = Integer.parseInt(model.getValueAt(rowIndex, 1).toString()); // Column 1 is User ID

            // Check if the logged-in user owns the order
            if (userId != loggedInUserId) {
                JOptionPane.showMessageDialog(this, "You can only delete your own orders.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Retrieve meal details for activity logging
            String mealDetails = model.getValueAt(rowIndex, 2).toString(); // Column 2 is Meal Details

            // Restore inventory and delete the order
            restoreInventoryStock(orderId);
            deleteOrderFromDatabase(orderId);

            // Update recent activity
            updateActivity("Order #" + orderId, "Deleted: " + mealDetails);

            // Remove the order from the table
            model.removeRow(rowIndex);

            dashBoardPanel.setNeedsRefresh(true);
        }

        JOptionPane.showMessageDialog(this, "Selected orders deleted successfully.", "Orders Deleted", JOptionPane.INFORMATION_MESSAGE);
    }



    private void restoreInventoryStock(int orderID) {
        String query = "SELECT meal_id, quantity FROM OrderItems WHERE order_id = ?";
        String updateStockQuery = "UPDATE Inventory SET quantity = quantity + ? WHERE meal_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = connection.prepareStatement(query);
             PreparedStatement updateStmt = connection.prepareStatement(updateStockQuery)) {

            selectStmt.setInt(1, orderID);
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                int mealID = rs.getInt("meal_id");
                int quantity = rs.getInt("quantity");

                // Update stock in Inventory
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, mealID);
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to restore inventory stock for order ID: " + orderID, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteOrderFromDatabase(int orderID) {
        String deleteOrderItemsQuery = "DELETE FROM OrderItems WHERE order_id = ?";
        String deleteOrderQuery = "DELETE FROM Orders WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement deleteItemsStmt = connection.prepareStatement(deleteOrderItemsQuery);
             PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery)) {

            // Delete associated OrderItems first
            deleteItemsStmt.setInt(1, orderID);
            deleteItemsStmt.executeUpdate();

            // Delete the order
            deleteOrderStmt.setInt(1, orderID);
            deleteOrderStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete order with ID: " + orderID, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void search(String searchText) {
        // If the search bar is empty, reload the original data
        if (searchText == null || searchText.trim().isEmpty()) {
            loadOrdersFromDatabase();
            return;
        }

        // Prepare the search query
        String query = "%" + searchText.toLowerCase() + "%";

        String searchQuery = "SELECT Orders.id AS order_id, Orders.user_id, " +
                "GROUP_CONCAT(Meals.meal_name || ' (x' || OrderItems.quantity || ')', ', ') AS meal_details, " +
                "Orders.order_date, Orders.status, " +
                "SUM(OrderItems.quantity * Inventory.meal_price) AS total_price " +
                "FROM Orders " +
                "JOIN OrderItems ON Orders.id = OrderItems.order_id " +
                "JOIN Meals ON OrderItems.meal_id = Meals.meal_id " +
                "JOIN Inventory ON Meals.meal_id = Inventory.meal_id " +
                "WHERE LOWER(Orders.id || '' || Orders.user_id || '' || Meals.meal_name || '' || Orders.status) LIKE ? " +
                "GROUP BY Orders.id";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {

            preparedStatement.setString(1, query);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                model.setRowCount(0); // Clear the table model

                while (resultSet.next()) {
                    int orderId = resultSet.getInt("order_id");
                    int userId = resultSet.getInt("user_id");
                    String mealDetails = resultSet.getString("meal_details");
                    String orderDate = resultSet.getString("order_date");
                    double totalPrice = resultSet.getDouble("total_price");
                    String status = resultSet.getString("status");

                    // Add matching rows to the table
                    model.addRow(new Object[]{orderId, userId, mealDetails, orderDate, totalPrice, status});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh table UI
        ordersTable.revalidate();
        ordersTable.repaint();
    }

    private static class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true); // Ensure the background is visible
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Set the text for the cell
            setText(value != null ? value.toString() : "");

            // Get the width of the column
            TableColumnModel columnModel = table.getColumnModel();
            int columnWidth = columnModel.getColumn(column).getWidth();

            // Set the preferred size of the JTextArea to trigger wrapping
            setSize(columnWidth, Short.MAX_VALUE);
            int preferredHeight = getPreferredSize().height;

            // Adjust the row height to fit wrapped text
            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            // Set selection colors
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }
}
