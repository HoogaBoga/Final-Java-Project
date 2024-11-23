package org.example.Frames;

import org.example.Buttons.CloseAddNewPass;
import org.example.Misc.UserSessionManager;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ChangePasswordFrame extends JFrame {

    public ChangePasswordFrame(){

        JLabel createNewPassword = new JLabel("Create New Password");
        JLabel newPassword = new JLabel("New Password");
        JLabel reenterPassword = new JLabel("Confirm Password");
        JLabel code = new JLabel("Code");
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton confirmButton = new JButton("Confirm");

        CloseAddNewPass closeAddNewPass = new CloseAddNewPass(this);

        closeAddNewPass.setPreferredSize(new Dimension(19, 19));
        
        createNewPassword.setBounds(75, 40, 400, 30);
        createNewPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        createNewPassword.setForeground(new Color(0x58A558));
        
        newPassword.setForeground(new Color(0x407340));
        newPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        newPassword.setBounds(45, 89, 150, 29);

        RoundedTextField newPasswordField = new RoundedTextField();

        newPasswordField.setBounds(175, 89, 200, 29);
        newPasswordField.setBackground(new Color(0xE9E5E5));
        newPasswordField.setForeground(Color.BLACK); // Text color
        newPasswordField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        newPasswordField.setOpaque(true); // Ensure it's opaque
        newPasswordField.setMargin(new Insets(0,10,0,0));
        newPasswordField.setPlaceholder("Enter New Password");

        reenterPassword.setBounds(20, 130, 400, 30);
        reenterPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        reenterPassword.setForeground(new Color(0x407340));
        
        RoundedTextField reenterPasswordField = new RoundedTextField();

        reenterPasswordField.setBounds(175, 130, 200, 29);
        reenterPasswordField.setBackground(new Color(0xE9E5E5));
        reenterPasswordField.setForeground(Color.BLACK); // Text color
        reenterPasswordField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        reenterPasswordField.setOpaque(true); // Ensure it's opaque
        reenterPasswordField.setMargin(new Insets(0,10,0,0));
        reenterPasswordField.setPlaceholder("Re-enter New Password");
        
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
            String newPasswordS = newPasswordField.getText().trim();
            String confirmPassword = reenterPasswordField.getText().trim();
            String resetCode = codeField.getText().trim();

            if (newPasswordS.isEmpty() || confirmPassword.isEmpty() || resetCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPasswordS.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate the reset token and update the password
            boolean success = UserSessionManager.updatePasswordWithToken(resetCode, newPasswordS);
            if (success) {
                JOptionPane.showMessageDialog(this, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
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
        topPanel.add(closeAddNewPass, BorderLayout.EAST);

        backgroundPanel.setLayout(null);

        backgroundPanel.add(createNewPassword);
        backgroundPanel.add(newPassword);
        backgroundPanel.add(newPasswordField);
        backgroundPanel.add(reenterPassword);
        backgroundPanel.add(reenterPasswordField);
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
