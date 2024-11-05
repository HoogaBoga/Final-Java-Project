package org.example.Buttons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FilterButton extends JButton implements ActionListener {

    private BufferedImage image;

    public FilterButton(){

        this.setBounds(337, 15, 15, 15 );

        try{
            image = ImageIO.read(new File("Resources/filter.png"));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }

        this.setContentAreaFilled(false);
        this.addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        if(image != null){
            g.drawImage(image, 0,0, getWidth(), getHeight(), this);
        }

        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
