package org.example.Buttons;

import org.example.Frames.AddMealFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class AddImageLabel extends JLabel {

    private File selectedImageFile = null;

    public AddImageLabel(){

        this.setText("Add Image +");
        this.setFont(AddMealFrame.INTER_FONT.deriveFont(12f));
        this.setForeground(new Color(0x9E9E9E));
        this.setPreferredSize(new Dimension(204, 35));
        this.setOpaque(true);
        this.setBackground(new Color(0xE3E1E1));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose an image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile(); // Store the file
            displayImage(selectedImageFile);
        }
    }

    private void displayImage(File file) {
        try {
            Image image = ImageIO.read(file);
            Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Scale as needed
            this.setIcon(new ImageIcon(scaledImage));
            this.setText(""); // Clear text after an image is uploaded
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add a getter for the selected image file
    public File getSelectedImageFile() {
        return selectedImageFile;
    }


}
