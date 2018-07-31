import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
/*
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 1200);
        window.setResizable(false);
        window.setLayout(null);

        MinerField minerField = new MinerField(30, 20, 50);
        minerField.initRefresh();

        window.add(minerField.getButtonsPanel());
        minerField.getButtonsPanel().setBounds(40, 40, minerField.getButtonsPanel().getWidth(), minerField.getButtonsPanel(). getHeight());


        window.setVisible(true);
*/

    GameWindow game = new GameWindow(30, 16, 99);

/*
    DifferentRandom diff = new DifferentRandom(10);
    diff.initRefresh();
    for(int i = 0; i < 10; i++) {
        System.out.println(diff.nextValue());
    }
*/
    }
}
