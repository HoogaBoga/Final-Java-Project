package org.example.Buttons;

import org.example.Frames.FigmaToCodeApp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class DashBoardButton extends JButton{

    public static final Font ACTOR_REGULAR_FONT = loadCustomFont();

    public static Font loadCustomFont() {
        try (InputStream is = DashBoardButton.class.getResourceAsStream("/Actor-Regular.ttf")) {
            if (is == null) {
                System.err.println("Font file not found in resources.");
                return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
            }
            Font actorRegular = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(actorRegular);
            return actorRegular;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
        }
    }

    public DashBoardButton(CardLayout cardLayout, JPanel cardPanel, String panelName){
        super("Dashboard");

        ImageIcon dashBoardIcon = new ImageIcon(Objects.requireNonNull(FigmaToCodeApp.class.getResource("/DashBoardGreen.png")));

        Font actor = loadCustomFont();

        this.setBounds(12, 55, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(dashBoardIcon);
        this.setFont(actor.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.addActionListener(e -> cardLayout.show(cardPanel, panelName));
    }

}
