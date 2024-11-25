package org.example.Buttons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FilterButton extends JButton{

    private BufferedImage image;

    public FilterButton(){

        this.setBounds(240, 15, 15, 15 );

        try{
            image = ImageIO.read(Objects.requireNonNull(FilterButton.class.getResource("/filter.png")));
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
