package org.example;

import javax.swing.*;
import java.awt.*;

public class SettingsButton extends JButton {

    public SettingsButton(){
        super("Settings   ");

        ImageIcon settingsIcon = new ImageIcon("Resources/SettingsGreen.png");

        this.setBounds(12, 147, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(settingsIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(8);
    }
}
