import javax.swing.*;
import java.awt.*;

public class Window
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(Window::playGame);
    }

    private static void playGame()
    {
        JFrame jf = new JFrame("Billiards");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(new Game());
        jf.pack();
        jf.setVisible(true);
    }
}
