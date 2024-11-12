package org.example.Panels;

import org.example.Buttons.DashBoardButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class OrdersPanel extends JScrollPane {
    private JPanel contentPanel;
    private JTable ordersTable;

    public OrdersPanel() {
        // Create the content panel to hold all components
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Column names for the table
        String[] columnNames = {"Order ID", "Orders", "Date", "Customer", "Status", "Payment"};

        // Sample data similar to the image
        Object[][] data = {
                {"#ID3245", "Hamburger", "Dec 25, 2024", "Petra Laines", "Paid", "Shipping"},
                {"#ID3245", "Hamburger", "Dec 25, 2024", "Petra Laines", "Paid", "Shipping"},
                {"#ID3245", "Hamburger", "Dec 25, 2024", "Petra Laines", "Paid", "Shipping"},
                {"#ID3245", "Hamburger", "Dec 25, 2024", "Petra Laines", "Paid", "Shipping"},
                {"#ID3245", "Hamburger", "Dec 25, 2024", "Petra Laines", "Paid", "Shipping"}
        };

        // Table model and table
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        ordersTable = new OrdersTable(model);  // Use custom JTable for scroll handling

        ordersTable.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(Font.PLAIN, 12));
        ordersTable.setRowHeight(40);

        // Customize header to remove shadow effect
        JTableHeader header = ordersTable.getTableHeader();
        header.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(Font.BOLD, 16));
        header.setBackground(Color.WHITE); // Ensure a flat background color
        header.setForeground(new Color(0, 128, 0)); // Set foreground color
        header.setBorder(BorderFactory.createEmptyBorder()); // Remove any border/shadow effect

        ordersTable.setGridColor(Color.WHITE); // Remove grid lines

        // Set custom renderer to support multiline text wrapping
        ordersTable.setDefaultRenderer(Object.class, new MultiLineTableCellRenderer());

        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(ordersTable, BorderLayout.CENTER);

        this.setViewportView(contentPanel);

        // Set scroll bar policy to NEVER
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    // Custom JTable to allow scrolling without scroll bars
    private static class OrdersTable extends JTable {
        public OrdersTable(DefaultTableModel model) {
            super(model);
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getPreferredSize().height < getParent().getHeight();
        }
    }

    // Custom cell renderer using JTextArea for multiline support
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

            // Adjust row height to fit content
            int height = getPreferredSize().height;
            if (table.getRowHeight(row) != height) {
                table.setRowHeight(row, height);
            }

            return this;
        }
    }
}
