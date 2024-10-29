package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DashBoardButton extends JButton {

    public static final Font ACTOR_REGULAR_FONT = loadCustomFont();

    public static Font loadCustomFont(){
        try {
            File fontFile = new File("Resources/Actor-Regular.ttf");
            Font actor = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(actor);
            return actor;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
        }
    }

    public DashBoardButton(){
        super("Dashboard");

        ImageIcon dashBoardIcon = new ImageIcon("Resources/DashBoardGreen.png");

        Font actor = loadCustomFont();

        this.setBounds(12, 55, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(dashBoardIcon);
        this.setFont(actor.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));
    }
}
