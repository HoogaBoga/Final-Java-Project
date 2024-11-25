package org.example.Panels;

import org.example.Frames.*;
import org.example.Misc.DatabaseManager;
import org.example.Misc.UserSessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class SettingsPanel extends JScrollPane {

    public SettingsPanel(JFrame parentFrame, String username, String role) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(10, 20, 0, 25)); // Padding for top-left alignment

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(Font.BOLD, 24f));
        settingsLabel.setForeground(new Color(0x58A558));
        titlePanel.add(settingsLabel, BorderLayout.NORTH);

        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(16f));
        nameLabel.setForeground(new Color(0x58A558));
        gbc.gridx = 0;
        gbc.gridy = 0;
        userInfoPanel.add(nameLabel, gbc);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));
        roleLabel.setForeground(new Color(0x987284));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, -7, 0, 0);
        userInfoPanel.add(roleLabel, gbc);

        mainPanel.add(titlePanel);
        mainPanel.add(userInfoPanel);

        // Add sections with unique ActionListeners
        mainPanel.add(createSection("Account Settings", new String[]{"Change Username", "Change Password"}));
        mainPanel.add(Box.createVerticalStrut(10));

        if(role.trim().equals("Manager")) {
            mainPanel.add(createSection("Manage Users", new String[]{"Add Users", "Delete Users", "Manage Roles"}));
            mainPanel.add(Box.createVerticalStrut(20));
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton deleteAccountButton = createButton("Delete Account", Color.WHITE, new Color(0xFF7979), e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account? This action cannot be undone.",
                    "Delete Account",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Perform account deletion
                boolean isDeleted = deleteUserAccount(UserSessionManager.getLoggedInUserID());

                if (isDeleted) {
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Window parentWindow = SwingUtilities.getWindowAncestor(this);
                    if (parentWindow != null) {
                        parentWindow.dispose(); // Close the parent frame
                    }
                    try {
                        new FigmaToCodeApp();
                        UserSessionManager.markAsLoggedOut();// Redirect to login
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (FontFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete the account. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JButton logoutButton = createButton("Logout", Color.WHITE, new Color(0xDDDDDD), e -> {
            System.out.println("Logout button clicked!");

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if(confirm == JOptionPane.YES_OPTION){
                parentFrame.dispose();
                try {
                    new FigmaToCodeApp();// Logout logic
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (FontFormatException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        bottomPanel.add(deleteAccountButton);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(logoutButton);

        mainPanel.add(bottomPanel);

        setViewportView(mainPanel);
        setBorder(BorderFactory.createEmptyBorder());
        getVerticalScrollBar().setUnitIncrement(16);
    }

    private JPanel createSection(String title, String[] buttonLabels) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f),
                new Color(0x58A558)
        ));

        for (String label : buttonLabels) {
            // Determine the specific action for each button
            ActionListener actionListener = null;
            switch (label) {
                case "Add Users":
                    actionListener = e -> {
                        System.out.println("Add Users button clicked!");
                        // Open the AddUserFrame
                        new RegisterFrame();// Replace with your frame class
                    };
                    break;
                case "Delete Users":
                    actionListener = e -> {
                        System.out.println("Delete Users button clicked!");
                        new DeleteUserFrame();
                    };
                    break;
                case "Manage Roles":
                    actionListener = e -> {
                        System.out.println("Manage Roles button clicked!");
                        new ChangeRoleFrame();
                    };
                    break;
                case "Change Username":
                    actionListener = e -> {
                        System.out.println("Change Username button clicked!");
                       new ChangeUserNameEmail();
                    };
                    break;
                case "Change Password":
                    actionListener = e -> {
                        System.out.println("Change Password button clicked!");
                        new ChangePasswordEmail();
                    };
                    break;
            }

            // Create button with actionListener
            JButton button = createButton(label, Color.BLACK, new Color(0xDDDDDD), actionListener);
            sectionPanel.add(button);
            sectionPanel.add(Box.createVerticalStrut(5)); // Spacing between buttons
        }

        return sectionPanel;
    }


    private JButton createButton(String text, Color textColor, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(0xCCCCCC), 1));
        button.setMaximumSize(new Dimension(300, 40));
        button.addActionListener(actionListener);
        return button;
    }

    /**
     * Deletes the user's account from the database based on their user ID.
     *
     * @param userId The ID of the user whose account is to be deleted.
     * @return true if the account was deleted successfully, false otherwise.
     */
    private boolean deleteUserAccount(int userId) {
        String deleteQuery = "DELETE FROM Users WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0; // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error while deleting account. Please contact support.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false; // Return false if the deletion failed
    }

}

