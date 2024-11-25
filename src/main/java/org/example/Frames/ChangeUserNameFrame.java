package org.example.Frames;

import org.example.Buttons.CloseAddNewPass;
import org.example.Buttons.CloseChangeUsernameFrame;
import org.example.Misc.UserSessionManager;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ChangeUserNameFrame extends JFrame {

    public ChangeUserNameFrame(){

        JLabel createNewPassword = new JLabel("Create New Username");
        JLabel newPassword = new JLabel("New Username");
        JLabel reenterPassword = new JLabel("Confirm Username");
        JLabel code = new JLabel("Code");
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton confirmButton = new JButton("Confirm");

        CloseChangeUsernameFrame closeChangeUsernameFrame = new CloseChangeUsernameFrame(this);

        closeChangeUsernameFrame.setPreferredSize(new Dimension(19, 19));

        createNewPassword.setBounds(75, 40, 400, 30);
        createNewPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        createNewPassword.setForeground(new Color(0x58A558));

        newPassword.setForeground(new Color(0x407340));
        newPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        newPassword.setBounds(45, 89, 150, 29);

        RoundedTextField newUsernameField = new RoundedTextField();

        newUsernameField.setBounds(175, 89, 200, 29);
        newUsernameField.setBackground(new Color(0xE9E5E5));
        newUsernameField.setForeground(Color.BLACK); // Text color
        newUsernameField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        newUsernameField.setOpaque(true); // Ensure it's opaque
        newUsernameField.setMargin(new Insets(0,10,0,0));
        newUsernameField.setPlaceholder("Enter New Username");

        reenterPassword.setBounds(20, 130, 400, 30);
        reenterPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        reenterPassword.setForeground(new Color(0x407340));

        RoundedTextField reenterUsernameField = new RoundedTextField();

        reenterUsernameField.setBounds(175, 130, 200, 29);
        reenterUsernameField.setBackground(new Color(0xE9E5E5));
        reenterUsernameField.setForeground(Color.BLACK); // Text color
        reenterUsernameField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        reenterUsernameField.setOpaque(true); // Ensure it's opaque
        reenterUsernameField.setMargin(new Insets(0,10,0,0));
        reenterUsernameField.setPlaceholder("Re-enter New Username");

        code.setBounds(112, 171, 100, 30);
        code.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        code.setForeground(new Color(0x407340));

        RoundedTextField codeField = new RoundedTextField();

        codeField.setBounds(175, 171, 200, 29);
        codeField.setBackground(new Color(0xE9E5E5));
        codeField.setForeground(Color.BLACK); // Text color
        codeField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        codeField.setOpaque(true); // Ensure it's opaque
        codeField.setMargin(new Insets(0,10,0,0));
        codeField.setPlaceholder("Enter Code");

        confirmButton.setBounds(130, 260, 162, 29);
        confirmButton.setBackground(new Color(0x987284));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));

        confirmButton.addActionListener(e -> {
            String newUsernameS = newUsernameField.getText().trim();
            String confirmUsername = reenterUsernameField.getText().trim();
            String resetCode = codeField.getText().trim();

            if (newUsernameS.isEmpty() || confirmUsername.isEmpty() || resetCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newUsernameS.equals(confirmUsername)) {
                JOptionPane.showMessageDialog(this, "Usernames do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate the reset token and update the password
            boolean success = UserSessionManager.updateUsernameWithToken(resetCode, newUsernameS);
            if (success) {
                JOptionPane.showMessageDialog(this, "Username updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid or expired reset token. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(AskEmailFrame.class.getResource("/Forgot Password.png")));
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };

        topPanel.setPreferredSize(new Dimension(350, 29));
        topPanel.setBackground(new Color(0x58A558));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(closeChangeUsernameFrame, BorderLayout.EAST);

        backgroundPanel.setLayout(null);

        backgroundPanel.add(createNewPassword);
        backgroundPanel.add(newPassword);
        backgroundPanel.add(newUsernameField);
        backgroundPanel.add(reenterPassword);
        backgroundPanel.add(reenterUsernameField);
        backgroundPanel.add(code);
        backgroundPanel.add(codeField);
        backgroundPanel.add(confirmButton);

        this.setSize(408, 380);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        new ChangePasswordFrame();
    }
}
