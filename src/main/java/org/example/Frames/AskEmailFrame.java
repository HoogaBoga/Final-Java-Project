package org.example.Frames;

import org.example.Buttons.CloseAddForgetEmail;
import org.example.Misc.UserSessionManager;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AskEmailFrame extends JFrame {

    public AskEmailFrame(){

        JLabel forgot = new JLabel("Forgot");
        JLabel forgotPassword = new JLabel("Password?");
        JLabel emailLabel = new JLabel("Email");
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel noEmail = new JLabel("Email was not found. Please try again.");
        JButton confirmButton = new JButton("Confirm");
        
        CloseAddForgetEmail closeAddForgetEmail = new CloseAddForgetEmail(this);

        closeAddForgetEmail.setPreferredSize(new Dimension(19, 19));

        noEmail.setForeground(Color.RED);
        noEmail.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(12f));
        noEmail.setBounds(130, 180, 250, 30);
        noEmail.setVisible(false);


        forgot.setBounds(20, 10, 177, 52);
        forgot.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        forgot.setForeground(new Color(0x58A558));

        forgotPassword.setBounds(20, 40, 177, 52);
        forgotPassword.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        forgotPassword.setForeground(new Color(0x58A558));

        emailLabel.setForeground(new Color(0x407340));
        emailLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        emailLabel.setBounds(69, 150, 96, 29);

        RoundedTextField emailField = new RoundedTextField();

        emailField.setBounds(120, 150, 227, 29);
        emailField.setBackground(new Color(0xE9E5E5));
        emailField.setForeground(Color.BLACK); // Text color
        emailField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        emailField.setOpaque(true); // Ensure it's opaque
        emailField.setMargin(new Insets(0,10,0,0));
        emailField.setPlaceholder("Enter Email");

        confirmButton.setBounds(130, 260, 162, 29);
        confirmButton.setBackground(new Color(0x987284));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));

        confirmButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = UserSessionManager.sendPasswordResetToken(email);
            if (success) {
                JOptionPane.showMessageDialog(this, "A reset token has been sent to your email.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new ChangePasswordFrame();
                this.dispose();
            } else {
                noEmail.setVisible(true);
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
        topPanel.add(closeAddForgetEmail, BorderLayout.EAST);

        backgroundPanel.setLayout(null);

        backgroundPanel.add(forgot);
        backgroundPanel.add(forgotPassword);
        backgroundPanel.add(emailLabel);
        backgroundPanel.add(emailField);
        backgroundPanel.add(noEmail);
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
        new AskEmailFrame();
    }
}
