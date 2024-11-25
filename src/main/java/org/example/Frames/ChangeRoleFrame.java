package org.example.Frames;

import org.example.Buttons.CloseChangeRoleFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class ChangeRoleFrame extends JFrame {
    private static final String DB_URL = "jdbc:sqlite:" + ChangeRoleFrame.class.getResource("/Database.db").getPath();

    public ChangeRoleFrame() {
        JLabel changeRoleLabel = new JLabel("Change User Role");
        JLabel changeRole = new JLabel("Changer User Role");
        JLabel selectUserLabel = new JLabel("Select User");
        JLabel selectRoleLabel = new JLabel("Select Role");
        JLabel managerCodeLabel = new JLabel("Manager Code");
        JLabel errorLabel = new JLabel();
        JButton confirmButton = new JButton("Change Role");

        JComboBox<String> userDropdown = new JComboBox<>();
        JComboBox<String> roleDropdown = new JComboBox<>(new String[]{"Manager", "Employee"});
        JPasswordField managerCodeField = new JPasswordField();

        CloseChangeRoleFrame closeChangeRoleFrame = new CloseChangeRoleFrame(this);

        closeChangeRoleFrame.setPreferredSize(new Dimension(19, 19));

        changeRole.setForeground(Color.WHITE);
        changeRole.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));

        // Error Label Setup
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        errorLabel.setBounds(50, 250, 300, 20);
        errorLabel.setVisible(false);

        // Title Label
        changeRoleLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(20f));
        changeRoleLabel.setForeground(new Color(0x58A558));
        changeRoleLabel.setBounds(140, 30, 200, 40);

        // Select User Label
        selectUserLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        selectUserLabel.setForeground(new Color(0x407340));
        selectUserLabel.setBounds(50, 100, 100, 30);

        // User Dropdown
        userDropdown.setBounds(180, 100, 200, 30);
        userDropdown.setBackground(new Color(0xE9E5E5));
        userDropdown.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));
        loadUsers(userDropdown); // Load employees and managers

        // Select Role Label
        selectRoleLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        selectRoleLabel.setForeground(new Color(0x407340));
        selectRoleLabel.setBounds(50, 150, 100, 30);

        // Role Dropdown
        roleDropdown.setBounds(180, 150, 200, 30);
        roleDropdown.setBackground(new Color(0xE9E5E5));
        roleDropdown.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));

        // Manager Code Label
        managerCodeLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        managerCodeLabel.setForeground(new Color(0x407340));
        managerCodeLabel.setBounds(50, 200, 120, 30);

        // Manager Code Field
        managerCodeField.setBounds(180, 200, 200, 30);
        managerCodeField.setBackground(new Color(0xE9E5E5));
        managerCodeField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));

        // Confirm Button
        confirmButton.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        confirmButton.setBackground(new Color(0x987284));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBounds(140, 300, 162, 29);

        confirmButton.addActionListener(e -> {
            String selectedUser = (String) userDropdown.getSelectedItem();
            String selectedRole = (String) roleDropdown.getSelectedItem();
            String managerCode = new String(managerCodeField.getPassword());

            // Validation
            if (selectedUser == null || selectedUser.isEmpty() || selectedUser.equals("No users available")) {
                showError(errorLabel, "Please select a user.");
                return;
            }
            if (managerCode.isEmpty()) {
                showError(errorLabel, "Manager code is required.");
                return;
            }
            if (!managerCode.equals("1108")) { // Manager code verification
                showError(errorLabel, "Invalid manager code.");
                return;
            }

            // Extract user ID
            String userId = selectedUser.split(" - ")[1]; // Extract user ID

            // Fetch the current role of the user
            String currentRole = getCurrentRole(userId);
            if (currentRole == null) {
                showError(errorLabel, "Failed to retrieve user role. Please try again.");
                return;
            }

            // Check if the selected role matches the current role
            if (currentRole.equalsIgnoreCase(selectedRole)) {
                showError(errorLabel, "User is already a/an " + currentRole + ".");
                return;
            }

            // Confirm role change
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to change the role of this user to " + selectedRole + "?",
                    "Confirm Role Change",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                updateUserRoleInDatabase(userId, selectedRole); // Update role
                JOptionPane.showMessageDialog(this, "Role updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                loadUsers(userDropdown); // Reload users
            }
        });


        // Top Panel (for close button)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(350, 29));
        topPanel.setBackground(new Color(0x58A558));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(closeChangeRoleFrame, BorderLayout.EAST);
        topPanel.add(changeRole, BorderLayout.CENTER);

        // Background Panel
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

        backgroundPanel.setLayout(null);
        // Add Components
        backgroundPanel.add(changeRoleLabel);
        backgroundPanel.add(selectUserLabel);
        backgroundPanel.add(userDropdown);
        backgroundPanel.add(selectRoleLabel);
        backgroundPanel.add(roleDropdown);
        backgroundPanel.add(managerCodeLabel);
        backgroundPanel.add(managerCodeField);
        backgroundPanel.add(errorLabel);
        backgroundPanel.add(confirmButton);

        // Frame Setup
        this.setSize(450, 400);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Load employees and managers from the database into the dropdown.
     */
    private void loadUsers(JComboBox<String> dropdown) {
        dropdown.removeAllItems();
        String query = "SELECT id, username FROM Users WHERE TRIM(role) IN ('Employee', 'Manager')";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            boolean hasUsers = false;
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                dropdown.addItem(username + " - " + userId);
                hasUsers = true;
            }

            if (!hasUsers) {
                dropdown.addItem("No users available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load users. Please check the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Update the role of the selected user in the database.
     */
    private void updateUserRoleInDatabase(String userId, String newRole) {
        String query = "UPDATE Users SET role = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newRole);
            preparedStatement.setInt(2, Integer.parseInt(userId));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update user role.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display an error message.
     */
    private void showError(JLabel errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Get the current role of a user by their ID.
     */
    private String getCurrentRole(String userId) {
        String query = "SELECT TRIM(role) AS role FROM Users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(userId));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role"); // Return the role
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve user role. Please check the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Return null if something goes wrong
    }


    public static void main(String[] args) {
        new ChangeRoleFrame();
    }
}
