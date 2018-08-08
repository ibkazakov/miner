package gui.main_window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindowMenu extends JMenuBar {

    private static final Font menuFont = new Font("Verdana", Font.PLAIN, 14);

    private JMenu menu = new JMenu("Minesweeper");

    private GameWindow gameWindow;

    public GameWindowMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        menu.setFont(menuFont);
        add(menu);


        JMenuItem newGameMenuItem = new JMenuItem("New game");
        newGameMenuItem.setFont(menuFont);
        menu.add(newGameMenuItem);

        JMenuItem settingMenuItem = new JMenuItem("Settings");
        settingMenuItem.setFont(menuFont);
        menu.add(settingMenuItem);

        menu.addSeparator();

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setFont(menuFont);
        menu.add(quitMenuItem);


        // listeners:

        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindowMenu.this.gameWindow.refreshGame();
            }
        });

        settingMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindowMenu.this.gameWindow.showSettings();
            }
        });

        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
