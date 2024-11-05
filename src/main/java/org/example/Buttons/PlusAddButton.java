package org.example.Buttons;

import org.example.Frames.AddMealFrame;
import org.example.Misc.OvalButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlusAddButton extends OvalButton implements ActionListener {

    private BufferedImage image;

    public PlusAddButton() {
        super(); // Default is oval/circle shape.

        setBorderThickness(0); // Oval buttons have some border by default.

        try {
            image = ImageIO.read(new File("Resources/AddImage.png")); // Replace with the path to your image.
        }
        catch (IOException e) {
            e.printStackTrace();
            image = null;
        }

        this.addActionListener(this);
    }

    @Override
    protected BufferedImage getBackgroundImage() {
        return image;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new AddMealFrame();
    }
}
