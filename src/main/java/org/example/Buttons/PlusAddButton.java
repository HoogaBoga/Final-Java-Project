package org.example.Buttons;

import org.example.Frames.AddMealFrame;
import org.example.Frames.FigmaToCodeApp;
import org.example.Misc.OvalButton;
import org.example.Panels.DashBoardPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class PlusAddButton extends OvalButton implements ActionListener {

    private BufferedImage image;
    public DashBoardPanel panel;
    public PlusAddButton(DashBoardPanel panel) {
        super(); // Default is oval/circle shape.
        this.panel = panel;
        setBorderThickness(0); // Oval buttons have some border by default.

        try {
            image = ImageIO.read(Objects.requireNonNull(PlusAddButton.class.getResource("/AddImage.png"))); // Replace with the path to your image.
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
        new AddMealFrame(panel);
    }
}

