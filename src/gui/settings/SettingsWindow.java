package gui.settings;

import gui.main_window.GameWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {

    private int gameWidth;
    private int gameHeight;
    private int gameMines;

    private JPanel mainPanel;

    private static final int MAX_WIDTH = 50;
    private static final int MAX_HEIGHT = 24;

    private JButton newGameButton = new JButton("New Game");
    private GameWindow gameWindow;

    private static final int MODE_HEIGHT = 30;
    private static final int MODE_WIDTH = 125;
    private static final int ROW_WIDTH = 70;

    private static final int TITLE_PANEL_HEIGHT = 30;


    private static final int ROWS = 3;
    private static final int COLUMNS = 3;

    private JPanel columnsTitlePanel = new JPanel();
    private JLabel[] titlePanelLabels = new JLabel[COLUMNS];

    private JPanel tablePanel = new JPanel();
    private JLabel[][] tablePanelLabels = new JLabel[ROWS][COLUMNS];

    private JPanel customModePanel = new JPanel();
    private JTextField[] customModeFields = new JTextField[COLUMNS];
    private static final int FIELD_RIGHT_PADDING = 5;

    private JPanel radioPanel = new JPanel();
    private JRadioButton beginnerRadioButton = new JRadioButton("Beginner", true);
    private JRadioButton intermediateRadioButton = new JRadioButton("Intermediate", false);
    private JRadioButton expertRadioButton = new JRadioButton("Expert", false);
    private JRadioButton customRadioButton = new JRadioButton("Custom", false);
    private ButtonGroup radioGroup = new ButtonGroup();

    private JPanel buttonPanel = new JPanel();
    private static final int BUTTON_LEFT_PADDING = 15;
    private static final int BUTTON_TOP_PADDING = 5;
    private static final int BUTTON_BOTTOM_PADDING = 5;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 115;
    private static final int BUTTON_PANEL_HEIGHT = BUTTON_TOP_PADDING + BUTTON_HEIGHT + BUTTON_BOTTOM_PADDING;

    private static final Color panelsColor = Color.LIGHT_GRAY;

    private static final Font labelsFont = null;

    private static final int[][] standardModes = new int[][] {
            {9, 9, 10},
            {16, 16, 40},
            {30, 16, 99}
    };


    public SettingsWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        // default settings is beginner
        gameWidth = standardModes[0][0];
        gameHeight = standardModes[0][1];
        gameMines = standardModes[0][2];

        setLayout(new GridLayout(1, 1));

        int windowWidth = MODE_WIDTH + ROW_WIDTH * COLUMNS;
        int windowHeight = TITLE_PANEL_HEIGHT + MODE_HEIGHT * ROWS + MODE_HEIGHT + BUTTON_PANEL_HEIGHT;

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        mainPanel.setLayout(null);

        // setSize(windowWidth, windowHeight);
        validate();
        // setResizable(false);
        setTitle("Game Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        columnsTitlePanel.setBounds(0,0, windowWidth, TITLE_PANEL_HEIGHT);
        columnsTitlePanel.setOpaque(true);
        columnsTitlePanel.setBackground(panelsColor);
        columnsTitlePanel.setLayout(null);
        // label init
        for(int i = 0; i < titlePanelLabels.length; i++) {
            titlePanelLabels[i] = new JLabel();
            titlePanelLabels[i].setBounds(MODE_WIDTH + i * ROW_WIDTH, 0, ROW_WIDTH, TITLE_PANEL_HEIGHT);
            columnsTitlePanel.add(titlePanelLabels[i]);
            titlePanelLabels[i].setFont(labelsFont);
            titlePanelLabels[i].setVisible(true);
        }
        titlePanelLabels[0].setText("Width");
        titlePanelLabels[1].setText("Height");
        titlePanelLabels[2].setText("Mines");
        mainPanel.add(columnsTitlePanel);

        tablePanel.setBounds(MODE_WIDTH, TITLE_PANEL_HEIGHT, ROW_WIDTH * ROWS, MODE_HEIGHT * COLUMNS);
        tablePanel.setOpaque(true);
        tablePanel.setBackground(null);
        tablePanel.setLayout(null);
        //label init
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                tablePanelLabels[i][j] = new JLabel();
                tablePanelLabels[i][j].setBounds(ROW_WIDTH * j, MODE_HEIGHT * i, ROW_WIDTH, MODE_HEIGHT);
                tablePanelLabels[i][j].setText(Integer.toString(standardModes[i][j]));
                tablePanel.add(tablePanelLabels[i][j]);
                tablePanelLabels[i][j].setFont(labelsFont);
                tablePanelLabels[i][j].setVisible(true);
            }
        }
        mainPanel.add(tablePanel);

        customModePanel.setBounds(MODE_WIDTH, TITLE_PANEL_HEIGHT + MODE_HEIGHT * COLUMNS,
                ROW_WIDTH * ROWS, MODE_HEIGHT);
        customModePanel.setOpaque(true);
        customModePanel.setBackground(null);
        customModePanel.setLayout(null);
        // init fields
        for(int i = 0; i < customModeFields.length; i++) {
            customModeFields[i] = new JTextField();
            customModeFields[i].setBounds(ROW_WIDTH * i,0, ROW_WIDTH - FIELD_RIGHT_PADDING, MODE_HEIGHT);
            customModePanel.add(customModeFields[i]);
            customModeFields[i].setEnabled(false);
            customModeFields[i].setVisible(true);
        }
        mainPanel.add(customModePanel);

        radioPanel.setBounds(0, TITLE_PANEL_HEIGHT, MODE_WIDTH, MODE_HEIGHT * COLUMNS + MODE_HEIGHT);
        radioPanel.setOpaque(true);
        radioPanel.setBackground(null);
        radioPanel.setLayout(null);
        // init radio buttons:
        radioGroup.add(beginnerRadioButton);
        radioGroup.add(intermediateRadioButton);
        radioGroup.add(expertRadioButton);
        radioGroup.add(customRadioButton);

        beginnerRadioButton.setBounds(0, 0, MODE_WIDTH, MODE_HEIGHT);
        intermediateRadioButton.setBounds(0, MODE_HEIGHT, MODE_WIDTH, MODE_HEIGHT);
        expertRadioButton.setBounds(0, MODE_HEIGHT * 2, MODE_WIDTH, MODE_HEIGHT);
        customRadioButton.setBounds(0, MODE_HEIGHT * 3, MODE_WIDTH, MODE_HEIGHT);

        radioPanel.add(beginnerRadioButton);
        radioPanel.add(intermediateRadioButton);
        radioPanel.add(expertRadioButton);
        radioPanel.add(customRadioButton);

        customRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (customRadioButton.isSelected()) {
                    for(int i = 0; i < customModeFields.length; i++) {
                        customModeFields[i].setEnabled(true);
                    }
                }
                else {
                    for(int i = 0; i < customModeFields.length; i++) {
                        customModeFields[i].setEnabled(false);
                    }
                }
            }
        });

        mainPanel.add(radioPanel);

        buttonPanel.setBounds(0, TITLE_PANEL_HEIGHT + MODE_HEIGHT * ROWS + MODE_HEIGHT,
                windowWidth, BUTTON_PANEL_HEIGHT);
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(panelsColor);
        buttonPanel.setLayout(null);
        buttonPanel.add(newGameButton);
        newGameButton.setBounds(BUTTON_LEFT_PADDING, BUTTON_TOP_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSettings();
                setVisible(false);
            }
        });
        buttonPanel.add(newGameButton);
        mainPanel.add(buttonPanel);
        newGameButton.setVisible(true);
        buttonPanel.setVisible(true);


        columnsTitlePanel.setVisible(true);
        newGameButton.setVisible(true);

        add(mainPanel);
        setResizable(false);
        pack();
        setVisible(false);
    }

    private void updateSettings() {
        if (beginnerRadioButton.isSelected()) {
            gameWidth = standardModes[0][0];
            gameHeight = standardModes[0][1];
            gameMines = standardModes[0][2];
        }
        if (intermediateRadioButton.isSelected()) {
            gameWidth = standardModes[1][0];
            gameHeight = standardModes[1][1];
            gameMines = standardModes[1][2];
        }
        if (expertRadioButton.isSelected()) {
            gameWidth = standardModes[2][0];
            gameHeight = standardModes[2][1];
            gameMines = standardModes[2][2];
        }
        if (customRadioButton.isSelected()) {
           gameWidth = readValue(customModeFields[0].getText(), gameWidth);
           gameHeight = readValue(customModeFields[1].getText(), gameHeight);
           gameMines = readValue(customModeFields[2].getText(), gameMines);
           gameWidth = Math.min(gameWidth, MAX_WIDTH);
           gameHeight = Math.min(gameHeight, MAX_HEIGHT);
           customModeFields[0].setText(Integer.toString(gameWidth));
           customModeFields[1].setText(Integer.toString(gameHeight));
           customModeFields[2].setText(Integer.toString(gameMines));
        }
        gameWindow.resetGameParameters(gameWidth, gameHeight, gameMines);
    }

    // returns the value itself if valueString is not a number, or value <= 0
    private int readValue(String valueString, int value) {
        try{
            int returnValue = Integer.parseInt(valueString);
            if (returnValue <= 0) {
                return value;
            }
            return returnValue;
        }
        catch (NumberFormatException e) {
            return value;
        }
    }
}
