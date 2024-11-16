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

public class OrdersPanel extends JPanel {
    private DashBoardPanel dashBoardPanel;
    private JTable ordersTable;
    private DefaultTableModel model;
    private JPanel activityPanel;
    private JLabel noActivityLabel;
    private Queue<JPanel> activityQueue;
    private Set<String> generatedOrderIDs;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public OrdersPanel(DashBoardPanel dashBoardPanel) {
        this.dashBoardPanel = dashBoardPanel;
        this.activityQueue = new LinkedList<>();
        this.generatedOrderIDs = new HashSet<>();

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

        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeOrder();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addOrderButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Orders Table
        String[] columnNames = {"Order ID", "User ID", "Meal ID", "Quantity", "Date", "Status"};
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

        // Adjust the column widths to fit the content
        TableColumnModel columnModel = ordersTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            int width = 0;
            for (int row = 0; row < ordersTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = column.getCellRenderer();
                Component comp = ordersTable.prepareRenderer(cellRenderer, row, i);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            column.setPreferredWidth(width + 10); // Add some padding
        }

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);

        // Update Status Button
        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.setFont(new Font("Actor", Font.PLAIN, 14));
        updateStatusButton.setBackground(Color.decode("#4CAF50"));
        updateStatusButton.setForeground(Color.WHITE);
        updateStatusButton.setFocusPainted(false);

        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrderStatus();
            }
        });

        JPanel statusButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusButtonPanel.setBackground(Color.WHITE);
        statusButtonPanel.add(updateStatusButton);

        contentPanel.add(activityPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(statusButtonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Load existing orders from the database
        loadOrders();
    }

    private void loadOrders() {
        String query = "SELECT * FROM Orders";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int orderID = resultSet.getInt("id");
                int userID = resultSet.getInt("user_id");
                int mealID = resultSet.getInt("meal_id");
                int quantity = resultSet.getInt("order_quantity");
                String orderDate = resultSet.getString("order_date");
                String status = resultSet.getString("status");

                model.addRow(new Object[]{orderID, userID, mealID, quantity, orderDate, status});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeOrder() {
        String customerName = JOptionPane.showInputDialog(this, "Enter customer name:", "Customer Name", JOptionPane.PLAIN_MESSAGE);
        if (customerName == null || customerName.trim().isEmpty()) return;

        Map<Integer, String> meals = dashBoardPanel.getMealData(); // Assumes getMealData method is implemented
        String[] mealOptions = meals.values().toArray(new String[0]);

        String selectedMeal = (String) JOptionPane.showInputDialog(this, "Select a food item:", "Food Item", JOptionPane.PLAIN_MESSAGE, null, mealOptions, mealOptions[0]);

        if (selectedMeal == null) return;

        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "Quantity", JOptionPane.PLAIN_MESSAGE);
        int quantity = Integer.parseInt(quantityStr);

        JPanel datePanel = new JPanel(new FlowLayout());
        JComboBox<String> monthComboBox = new JComboBox<>(getMonths());
        JComboBox<String> dayComboBox = new JComboBox<>(getDays(31));
        JComboBox<String> yearComboBox = new JComboBox<>(getYears());

        monthComboBox.addActionListener(e -> {
            int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);
            dayComboBox.setModel(new DefaultComboBoxModel<>(getDays(daysInMonth)));
        });

        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("Day:"));
        datePanel.add(dayComboBox);
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearComboBox);

        int result = JOptionPane.showConfirmDialog(this, datePanel, "Select Date", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String selectedDate = monthComboBox.getSelectedItem() + " " + dayComboBox.getSelectedItem() + ", " + yearComboBox.getSelectedItem();

        String mealID = getMealID(selectedMeal);
        String userID = "1"; // For simplicity, assuming the user ID is "1". Modify as needed.

        insertOrderIntoDatabase(userID, mealID, quantity, selectedDate);

        model.addRow(new Object[]{getLatestOrderID(), userID, mealID, quantity, selectedDate, "Pending"});

        updateActivity(customerName, selectedMeal);
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
            System.out.println("Order added to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentStatus = (String) model.getValueAt(selectedRow, 5);
        if ("Pending".equals(currentStatus)) {
            model.setValueAt("Paid", selectedRow, 5);
        } else if ("Paid".equals(currentStatus)) {
            model.setValueAt("Pending", selectedRow, 5);
        }

        JOptionPane.showMessageDialog(this, "Order status updated successfully.", "Status Updated", JOptionPane.INFORMATION_MESSAGE);
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

    private int getDaysInMonth(String month, int year) {
        switch (month) {
            case "January":
            case "March":
            case "May":
            case "July":
            case "August":
            case "October":
            case "December":
                return 31;
            case "April":
            case "June":
            case "September":
            case "November":
                return 30;
            case "February":
                return (isLeapYear(year) ? 29 : 28);
            default:
                return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setFont(table.getFont());
            setText(value != null ? value.toString() : "");

            int height = getPreferredSize().height;
            if (table.getRowHeight(row) != height) {
                table.setRowHeight(row, height);
            }

            return this;
        }
    }
}