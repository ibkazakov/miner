package field;

import gui.refresh_panel.displays.DigitalDisplay;
import gui.refresh_panel.RefreshButton;
import gui.refresh_panel.displays.TimeDigitalDisplay;
import logic.DifferentRandom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;

import javax.swing.*;
import java.util.Queue;

public class MinerField {
    private MinerBox[][] field;
    private int width;
    private int height;
    private int mines;

    private boolean isFirstClicked = false;
    private boolean isFail = false;

    private int unsettedMines;

    private JPanel buttonsPanel;
    private RefreshButton refreshButton;
    private DigitalDisplay minersDisplay;
    private TimeDigitalDisplay timeDisplay;

    private Queue<MinerBox> openQueue;

    public int opened = 0;

    private DifferentRandom differentRandom;

    public MinerField(int width, int height, int mines) {
        field = new MinerBox[width][height];
        this.width = width;
        this.height = height;
        this.mines = Math.min(mines, width * height - 1);
        this.unsettedMines = this.mines;

        this.openQueue = new ArrayDeque<MinerBox>(width * height);
        this.differentRandom = new DifferentRandom(width * height);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(null);
        buttonsPanel.setSize(width * MinerBox.BOX_SIZE, height * MinerBox.BOX_SIZE);
        // buttonsPanel.setOpaque(true);
        // buttonsPanel.setBackground(new Color(195, 198, 203));

        buttonsPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    refreshButton.fieldClicked();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    refreshButton.normal();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j] = new MinerBox(i, j);
                buttonsPanel.add(field[i][j].getOpenButton());
                buttonsPanel.add(field[i][j].getContentLabel());
            }
        }
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public void setRefreshButton(RefreshButton refreshButton) {
        this.refreshButton = refreshButton;
    }

    public void setMinersDisplay(DigitalDisplay minersDisplay) {
        this.minersDisplay = minersDisplay;
        this.minersDisplay.setValue(mines);
    }

    public void setTimeDisplay(TimeDigitalDisplay timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public RefreshButton getRefreshButton() {
        return refreshButton;
    }

    public boolean isFirstClicked() {
        return isFirstClicked;
    }

    // I: init field:
    private void clear() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j].init();
                field[i][j].setMinerField(this);
            }
        }
    }


    // setting min(differentRandom.remain(), unsettedMines). remain = 0 after.
    private void distributeMines() {
        while (differentRandom.remain() != 0) {
            int randomValue = differentRandom.nextValue();
            int mineWidth = randomValue % width;
            int mineHeight = randomValue / width;
            if (unsettedMines != 0) {
                field[mineWidth][mineHeight].setMine(true);
                unsettedMines--;
            }
        }
    }

    public void firstClick(int x, int y) {
        differentRandom.initRefresh();
        //exceptions
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0) && (j >= 0) && (i < width) && (j < height)) {
                    differentRandom.exceptValue(i + j * width);
                }
            }
        }


        //regular mines setting
        distributeMines();

        // resetting
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0) && (j >= 0) && (i < width) && (j < height) && !((i == x) && (j == y))) {
                    differentRandom.resetValue(i + j * width);
                }
            }
        }

        //surplus mines setting(around clicked box, if they are so many)

        distributeMines();

        calculateArounds();
        isFirstClicked = true;
        timeDisplay.start();
    }

    private int minesAround(int k, int l) {
        int minesCounter = 0;
        for(int i = k - 1; i <= k + 1; i++) {
            for(int j = l - 1; j <= l + 1; j++) {
                if (!((i == k) && (j == l))) { //no central box
                    if ((i >= 0) && (j >= 0) && (i < width) && (j < height)) { //box (i, j) must exist!
                        if (field[i][j].isMine()) {
                            minesCounter++;
                        }
                    }
                }
            }
        }
        return minesCounter;
    }

    // + labels init
    private void calculateArounds() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j].setMinesAround(minesAround(i, j));
                field[i][j].computeLabel();
            }
        }
    }

    public void initRefresh() {
        clear();
        opened = 0;
        isFirstClicked = false;
        isFail = false;
        unsettedMines = mines;
        minersDisplay.setValue(mines);
        timeDisplay.initRefresh();
        refreshButton.forceNormal();
    }

    // II open func:

    //NO RECURSION!

    public void putNearZeroToQueue(int x, int y) {
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                if (!((i == x) && (j == y))) {
                    if ((i >= 0) && (j >= 0) && (i < width) && (j < height)) {
                        if (!field[i][j].isOpen() && !field[i][j].isOpenPretend && !field[i][j].isMine()) {
                            openQueue.add(field[i][j]);
                            field[i][j].isOpenPretend = true;
                        }
                    }
                }
            }
        }
    }

    public void openQueueZeroBoxes() {
        MinerBox boxToOpen = openQueue.poll();
        while (boxToOpen != null) {
            if (boxToOpen.getMinesAround() == 0) {
                putNearZeroToQueue(boxToOpen.x, boxToOpen.y);
            }
            boxToOpen.simple_open();
            opened++;
            boxToOpen = openQueue.poll();
        }
        checkWin();
    }

    public void checkWin() {
        if ((opened + mines == width * height) && (!isFail)) {
            win();
        }
    }

    public void win() {
        timeDisplay.stop();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if (!field[i][j].isOpen()) {
                    field[i][j].setFlagState(1);
                    field[i][j].disable();
                }
            }
        }
        refreshButton.win();
    }

    public void fail() {
        timeDisplay.stop();
        isFail = true;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                 field[i][j].fail_open();
            }
        }
        refreshButton.fail();
    }

    public void increaseDisplay() {
        minersDisplay.incrementValue();
    }

    public void decreaseDisplay() {
        minersDisplay.decrementValue();
    }

    private void openAround(int x, int y) {
        for(int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0) && (j >= 0) && (i < width) && (j < height)) {
                    if ((!field[i][j].isOpen()) && (field[i][j].getFlagState() != 1)) {
                        field[i][j].open();
                    }
                }
            }
        }
    }

    private int aroundFlags(int x, int y) {
        int flags = 0;
        for(int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0) && (j >= 0) && (i < width) && (j < height)) {
                    if ((!field[i][j].isOpen()) && (field[i][j].getFlagState() == 1)) {
                        flags++;
                    }
                }
            }
        }
        return flags;
    }

    public void middleClick(int x, int y) {
        if (field[x][y].getMinesAround() == aroundFlags(x, y)) {
            openAround(x, y);
        }
    }

}
