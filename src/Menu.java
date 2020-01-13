import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu{
    public static void SimpleJButton() {
        JFrame f = new JFrame("Button Example");
        JButton b = new JButton("Play");
        b.setBounds(100, 100, 140, 40);
        f.add(b);
        f.setSize(1000, 500);
        f.setLayout(null);
        f.setVisible(true);
    }
    public static void main() {
        JFrame f = new JFrame("JavaTutorial.net");
        //f.getContentPane().add(new JFrameGraphics());
        f.setSize(300, 300);


        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.setVisible(true);
    }

}