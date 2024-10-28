package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class EmployeeButton extends JButton implements ActionListener {

    private JFrame parentFrame;

    public EmployeeButton(JFrame parentFrame) {
        super("Employee");
        this.parentFrame = parentFrame;

        // Button settings
        this.setBounds(30, 247, 251, 28);
        this.setBackground(new Color(0xFFFFFF));
        this.setForeground(new Color(0x987284));
        this.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        this.setFocusable(false);

        // Add ActionListener
        this.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            new HomeFrame();
            parentFrame.dispose();
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
