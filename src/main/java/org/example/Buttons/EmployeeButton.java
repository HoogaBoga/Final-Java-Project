package org.example.Buttons;

import org.example.Frames.EmployeeFrame;
import org.example.Frames.FigmaToCodeApp;
import org.example.Frames.HomeFrame;

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
        this.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));
        this.setFocusable(false);

        // Add ActionListener
        this.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            new EmployeeFrame();
            parentFrame.dispose();
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
