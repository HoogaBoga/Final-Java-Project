package org.example.Buttons;

import org.example.Frames.FigmaToCodeApp;
import org.example.Frames.HomeFrame;
import org.example.Panels.DashBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ManagerButton extends JButton implements ActionListener {

    private JFrame parentFrame;
    private int managerCode = 1108;

    public ManagerButton(JFrame parentFrame) {
        super("Manager");
        this.parentFrame = parentFrame;

        // Button settings
        this.setBounds(30, 197, 251, 28);
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
            String manager = JOptionPane.showInputDialog("Please enter the manager code: ");

            if (manager == null || manager.trim().isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Manager Code is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!manager.equals(String.valueOf(managerCode))) {
                JOptionPane.showMessageDialog(parentFrame, "Incorrect Manager Code. Registration failed.", "Authorization Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            new HomeFrame();
            parentFrame.dispose();

        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
