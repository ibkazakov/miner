import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private int width;
    private int height;
    private int mines;
    private JPanel refreshPanel;
    private MinerField minerField;
    private JPanel fieldPanel;
    private RefreshButton refreshButton;

    private JPanel mainPanel;

    private DigitalDisplay minesDisplay;
    private TimeDigitalDisplay timeDisplay;

    private SettingsWindow settingsWindow = new SettingsWindow(this);


    private static final int LEFT_MARGIN = 5;
    private static final int RIGHT_MARGIN = 5;
    private static final int TOP_MARGIN = 5;
    private static final int BOTTOM_MARGIN = 5;
    private static final int BEETWEEN_MARGIN = 5;

    private static final int MIN_WIDTH = 225;
    private static final int REFRESH_HEIGHT = 70;

    private static final int DISPLAY_PADDING = 5;

   // private static final int MENU_BAR_HEIGHT = 21; // standart menuBar Height

    private static final Font menuFont = new Font("Verdana", Font.PLAIN, 14);

    public GameWindow(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;

        setLocation(0, 0);

        int refreshPanelWidth = Math.max(MIN_WIDTH, width * MinerBox.BOX_SIZE);
        int windowWidth = LEFT_MARGIN + refreshPanelWidth + RIGHT_MARGIN;
        int windowHeight = TOP_MARGIN + REFRESH_HEIGHT
                + BEETWEEN_MARGIN + height * MinerBox.BOX_SIZE + BOTTOM_MARGIN;
        int fieldWidthPosition = LEFT_MARGIN + Math.max(0, MIN_WIDTH - width * MinerBox.BOX_SIZE) / 2;
        int fieldHeightPosition = TOP_MARGIN + REFRESH_HEIGHT + BEETWEEN_MARGIN;

        refreshPanel = new JPanel();
        refreshPanel.setLayout(null);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        mainPanel.setLayout(null);

        // setResizable(false);
        // setSize(windowWidth, windowHeight);
        // validate();
        setTitle("Java MineSweeper");
        setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        refreshPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        refreshPanel.setBounds(LEFT_MARGIN, TOP_MARGIN, refreshPanelWidth, REFRESH_HEIGHT);
        mainPanel.add(refreshPanel);

        minerField = new MinerField(width, height, mines);
        fieldPanel = minerField.getButtonsPanel();
        fieldPanel.setBounds(fieldWidthPosition, fieldHeightPosition,
                width * MinerBox.BOX_SIZE, height * MinerBox.BOX_SIZE);
        mainPanel.add(fieldPanel);


        refreshButton = new RefreshButton(refreshPanel, minerField);
        minerField.setRefreshButton(refreshButton);

        minesDisplay = new DigitalDisplay(refreshPanel,
                DISPLAY_PADDING);
        refreshPanel.add(minesDisplay);
        minerField.setMinersDisplay(minesDisplay);

        timeDisplay = new TimeDigitalDisplay(refreshPanel,
                refreshPanelWidth - minesDisplay.getWidth() - DISPLAY_PADDING);
        refreshPanel.add(timeDisplay);
        minerField.setTimeDisplay(timeDisplay);

        minerField.initRefresh();


        // menu making menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Minesweeper");
        menu.setFont(menuFont);
        menuBar.add(menu);

        JMenu about = new JMenu("About");
        about.setFont(menuFont);
        menuBar.add(about);

        JMenuItem newGameMenuItem = new JMenuItem("New game");
        newGameMenuItem.setFont(menuFont);
        menu.add(newGameMenuItem);

        JMenuItem settingMenuItem = new JMenuItem("Settings");
        settingMenuItem.setFont(menuFont);
        menu.add(settingMenuItem);

        menu.addSeparator();

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setFont(menuFont);
        menu.add(saveMenuItem);

        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setFont(menuFont);
        menu.add(loadMenuItem);

        menu.addSeparator();

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setFont(menuFont);
        menu.add(quitMenuItem);

        setJMenuBar(menuBar);

        // listeners:

        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshButton.refresh();
            }
        });

        settingMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsWindow.setLocation(0, 0);
                settingsWindow.setVisible(true);
            }
        });


        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // end menu making
        menuBar.setVisible(true);
        refreshPanel.setVisible(true);
        fieldPanel.setVisible(true);
        refreshButton.setVisible(true);
        minesDisplay.setVisible(true);
        timeDisplay.setVisible(true);
        setLocationRelativeTo(null);
        add(mainPanel);
        setResizable(false);
        pack();
        setVisible(true);
    }

    public void resetGameParameters(int width, int height, int mines) {
        setVisible(false);
        this.width = width;
        this.height = height;
        this.mines = mines;

        // padding parameters
        int refreshPanelWidth = Math.max(MIN_WIDTH, width * MinerBox.BOX_SIZE);
        int windowWidth = LEFT_MARGIN + refreshPanelWidth + RIGHT_MARGIN;
        int windowHeight = TOP_MARGIN + REFRESH_HEIGHT
                + BEETWEEN_MARGIN + height * MinerBox.BOX_SIZE + BOTTOM_MARGIN;
        int fieldWidthPosition = LEFT_MARGIN + Math.max(0, MIN_WIDTH - width * MinerBox.BOX_SIZE) / 2;
        int fieldHeightPosition = TOP_MARGIN + REFRESH_HEIGHT + BEETWEEN_MARGIN;

        // total resize
        mainPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        // validate();

        //refresh panel resize
        refreshPanel.setBounds(LEFT_MARGIN, TOP_MARGIN, refreshPanelWidth, REFRESH_HEIGHT);
        minesDisplay.resize();
        timeDisplay.setDisplayPadding(refreshPanelWidth - minesDisplay.getWidth() - DISPLAY_PADDING);
        timeDisplay.resize();
        refreshButton.resize();

        // Delete field panel
        mainPanel.remove(fieldPanel);
        //new minerfield
        minerField = new MinerField(width, height, mines);
        fieldPanel = minerField.getButtonsPanel();
        fieldPanel.setBounds(fieldWidthPosition, fieldHeightPosition,
                width * MinerBox.BOX_SIZE, height * MinerBox.BOX_SIZE);
        mainPanel.add(fieldPanel);
        // minerfield binds
        minerField.setRefreshButton(refreshButton);
        minerField.setMinersDisplay(minesDisplay);
        minerField.setTimeDisplay(timeDisplay);
        refreshButton.setMinerField(minerField);
        // init game
        minerField.initRefresh();
        pack();
        setVisible(true);
    }
}
