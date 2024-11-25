package org.example.Buttons;

import org.example.Frames.*;
import org.example.Misc.OvalButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class CloseChangeRoleFrame extends OvalButton implements ActionListener {

    private BufferedImage image;
    private ChangeRoleFrame changeRoleFrame;

    public CloseChangeRoleFrame(ChangeRoleFrame changeRoleFrame) {
        super(); // Default is oval/circle shape.
        this.changeRoleFrame = changeRoleFrame;

        setBorderThickness(0); // Oval buttons have some border by default.

        try {
            image = ImageIO.read(Objects.requireNonNull(CloseAddForgetEmail.class.getResource("/x.png"))); // Replace with the path to your image.
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
        if(changeRoleFrame != null){
            changeRoleFrame.dispose();
        }
    }
}

