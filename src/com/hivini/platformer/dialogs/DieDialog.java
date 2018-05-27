package com.hivini.platformer.dialogs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class DieDialog extends JDialog {

    public DieDialog() {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(new URL("https://i.imgur.com/PjYL8HD.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(new JFrame(), "Sorry, you have died, thanks for playing!",
                "You died!", JOptionPane.INFORMATION_MESSAGE, icon);
    }
}
