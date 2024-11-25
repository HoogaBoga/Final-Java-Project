package org.example.Frames;

import org.example.Buttons.CloseDeleteUserFrame;
import org.example.Misc.DatabaseManager;
import org.example.Misc.UserSessionManager;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class DeleteUserFrame extends JFrame {

    public DeleteUserFrame() {
        JLabel deleteUser = new JLabel("Delete User");
        JLabel selectUserLabel = new JLabel("Select User");
        JLabel deleteUserLabel = new JLabel("Delete User");
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel noUserSelected = new JLabel("No user selected. Please try again.");
        JButton confirmButton = new JButton("Delete");

        JComboBox<String> userDropdown = new JComboBox<>();
        JLabel managerCodeLabel = new JLabel("Manager Code");
        JPasswordField managerCodeField = new JPasswordField();

        CloseDeleteUserFrame closeDeleteUserFrame = new CloseDeleteUserFrame(this);

        closeDeleteUserFrame.setPreferredSize(new Dimension(19, 19));

        noUserSelected.setForeground(Color.RED);
        noUserSelected.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        noUserSelected.setBounds(130, 200, 250, 30);
        noUserSelected.setVisible(false);

        deleteUserLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        deleteUserLabel.setForeground(Color.WHITE);

        deleteUser.setBounds(140, 50, 177, 52);
        deleteUser.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        deleteUser.setForeground(new Color(0x58A558));

        selectUserLabel.setForeground(new Color(0x407340));
        selectUserLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        selectUserLabel.setBounds(50, 120, 96, 29);

        userDropdown.setBounds(165, 120, 200, 29);
        userDropdown.setBackground(new Color(0xE9E5E5));
        userDropdown.setForeground(Color.BLACK);
        userDropdown.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        loadUsers(userDropdown); // Load users from the database

        managerCodeLabel.setForeground(new Color(0x407340));
        managerCodeLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        managerCodeLabel.setBounds(50, 170, 120, 29);

        managerCodeField.setBounds(165, 170, 200, 29);
        managerCodeField.setBackground(new Color(0xE9E5E5));
        managerCodeField.setForeground(Color.BLACK);
        managerCodeField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        managerCodeField.setOpaque(true);

        confirmButton.setBounds(130, 260, 162, 29);
        confirmButton.setBackground(new Color(0x987284));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));

        confirmButton.addActionListener(e -> {
            String selectedUser = (String) userDropdown.getSelectedItem();
            if (selectedUser == null || selectedUser.isEmpty()) {
                noUserSelected.setText("No user selected.");
                noUserSelected.setVisible(true);
                return;
            }

            String managerCode = new String(managerCodeField.getPassword());
            if (!managerCode.equals("1108")) { // Replace with your manager code validation logic
                JOptionPane.showMessageDialog(this, "Invalid manager code.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                deleteUserFromDatabase(selectedUser.split(" - ")[1]); // Pass the userID
                JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                loadUsers(userDropdown); // Reload users
            }
        });

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(AskEmailFrame.class.getResource("/Forgot Password.png")));
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };

        topPanel.setPreferredSize(new Dimension(350, 29));
        topPanel.setBackground(new Color(0x58A558));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(closeDeleteUserFrame, BorderLayout.EAST);
        topPanel.add(deleteUserLabel, BorderLayout.CENTER);

        backgroundPanel.setLayout(null);

        backgroundPanel.add(deleteUser);
        backgroundPanel.add(selectUserLabel);
        backgroundPanel.add(userDropdown);
        backgroundPanel.add(managerCodeLabel);
        backgroundPanel.add(managerCodeField);
        backgroundPanel.add(noUserSelected);
        backgroundPanel.add(confirmButton);

        this.setSize(408, 380);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void loadUsers(JComboBox<String> dropdown) {
        dropdown.removeAllItems();
        String query = "SELECT id, username FROM Users";

        try (Connection connection = DatabaseManager.getConnection();

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                dropdown.addItem(username + " - " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load users.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUserFromDatabase(String userId) {
        String query = "DELETE FROM Users WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(userId));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new DeleteUserFrame();
    }
}
