package com.hivini.platformer.dialogs;

import com.hivini.platformer.GameManager;
import com.hivini.platformer.objects.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class VictoryDialog extends JDialog {

    public VictoryDialog() {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(new URL("https://i.imgur.com/vAOWMYe.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(new JFrame(), "You are awesome! Thanks for playing\n\nYour final score is: " + (GameManager.score * GameManager.getPlayer().getLives()),
                "You WON!", JOptionPane.INFORMATION_MESSAGE, icon);
    }

}
