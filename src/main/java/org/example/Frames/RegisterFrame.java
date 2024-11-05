package org.example.Frames;

import org.example.Buttons.FilterButton;
import org.example.Buttons.RegisterButton;
import org.example.TextFields.RoundedPassWordField;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class RegisterFrame extends JFrame {

    public RegisterFrame(){

        JLabel backButton = new JLabel("Back");

        ImageIcon registerLogo = new ImageIcon(Objects.requireNonNull(RegisterFrame.class.getResource("/RegisterLogo.png")));
        
        String[] roles = {"  Manager", "  Employee", "  Select Role"};
        
        JComboBox rolesBox = new JComboBox<>(roles);

        JLabel registerLogoImg = new JLabel();
        JLabel createAccount = new JLabel("Create Account");
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel roleLabel = new JLabel("Role");

        registerLogoImg.setBounds(163, 14, registerLogo.getIconWidth(), registerLogo.getIconHeight());
        registerLogoImg.setIcon(registerLogo);

        createAccount.setBounds(107, 121, 211, 50);
        createAccount.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(24f));
        createAccount.setForeground(new Color(0x58A558));

        usernameLabel.setForeground(new Color(0x407340));
        usernameLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        usernameLabel.setBounds(74, 189, 96, 29);

        RoundedTextField usernameField = new RoundedTextField();

        usernameField.setBounds(175, 189, 210, 29);
        usernameField.setBackground(new Color(0xE9E5E5));
        usernameField.setForeground(Color.BLACK); // Text color
        usernameField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        usernameField.setOpaque(true); // Ensure it's opaque
        usernameField.setMargin(new Insets(0,10,0,0));
        usernameField.setPlaceholder("Enter Username");

        passwordLabel.setForeground(new Color(0x407340));
        passwordLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        passwordLabel.setBounds(74, 233, 96, 29);

        RoundedPassWordField passWordField = new RoundedPassWordField();

        passWordField.setBounds(175, 233, 210, 29);
        passWordField.setBackground(new Color(0xE9E5E5));
        passWordField.setForeground(Color.BLACK); // Text color
        passWordField.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        passWordField.setOpaque(true); // Ensure it's opaque
        passWordField.setMargin(new Insets(0,10,0,0));
        passWordField.setPlaceholder("Enter Password");

        roleLabel.setForeground(new Color(0x407340));
        roleLabel.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(15f));
        roleLabel.setBounds(74, 277, 96, 29);

        rolesBox.setBounds(175, 277, 210, 27);
        rolesBox.setBackground(Color.WHITE);
        rolesBox.setForeground(new Color(0x58A558)); // Text color
        rolesBox.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(16f));
        rolesBox.setSelectedItem("  Select Role");
        rolesBox.setFocusable(false);
        rolesBox.setEnabled(true);
        rolesBox.setOpaque(true); // Ensure it's opaque


        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(RegisterFrame.class.getResource("/Register.png")));
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };

        RegisterButton registerButton = new RegisterButton(usernameField, passWordField, rolesBox, this);

        backButton.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(11, 339, 33, 18);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterFrame.this.dispose();
            }
        });

        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(new Dimension(408, 363));
        backgroundPanel.add(registerLogoImg);
        backgroundPanel.add(createAccount);
        backgroundPanel.add(usernameLabel);
        backgroundPanel.add(usernameField);
        backgroundPanel.add(passwordLabel);
        backgroundPanel.add(passWordField);
        backgroundPanel.add(roleLabel);
        backgroundPanel.add(rolesBox);
        backgroundPanel.add(registerButton);
        backgroundPanel.add(backButton);


        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }
}
