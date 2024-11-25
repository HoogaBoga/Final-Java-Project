package org.example.Buttons;

import org.example.Panels.DashBoardPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RefreshButton extends JButton {

    private BufferedImage image;


    public RefreshButton(){

        this.setBounds(257, 15, 15, 15 );

        try{
            image = ImageIO.read(Objects.requireNonNull(RefreshButton.class.getResource("/refresh-cw.png")));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }

        this.setContentAreaFilled(false);
    }

    @Override
    public void paintComponent(Graphics g){
        if(image != null){
            g.drawImage(image, 0,0, getWidth(), getHeight(), this);
        }

        super.paintComponent(g);
    }
}
