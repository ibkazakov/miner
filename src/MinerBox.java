import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public class MinerBox {
    private boolean isOpen;
    private boolean isMine;
    public int x;
    public int y;
    public boolean isOpenPretend = false;
    private int minesAround;
    private int flagState; // 0 - normal, 1 - flagged, 2 - ?
    private boolean isLeftClickEnabled;
    private boolean isRightClickEnabled;


    private MinerField minerField;

    private JButton openButton;
    private JLabel contentLabel;

    public static final int BOX_SIZE = 25;
    private static final Font LABEL_FONT = new Font(null, Font.BOLD,25);

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private static final Icon MINE_ICON = new ImageIcon(classLoader.getResource("assets/mine_icon.png"));
    private static final Icon MINE_BREAK_ICON = new ImageIcon(classLoader.getResource("assets/mine_break_icon.png"));
    private static final Icon FLAG_ICON = new ImageIcon(classLoader.getResource("assets/flag_icon.png"));
    private static final Icon WRONG_FLAG_ICON = new ImageIcon(classLoader.getResource("assets/wrong_flag_icon.png"));

    private static final Color BOX_COLOR = new Color(230, 230, 230);
    private static final Color BREAK_MINE_COLOR = new Color(255,70,70);
    private static final Color BORDERS_COLOR = Color.LIGHT_GRAY;
    private static final Color[] NUMBER_COLORS = new Color[] {
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            new Color(0,0,128),
            new Color(128, 0,0),
            new Color(0, 128, 0),
            Color.BLACK,
            Color.GRAY
    };

    public MinerBox(int x, int y) {
        this.x = x;
        this.y = y;

        openButton = new JButton();
        openButton.setBounds(x * BOX_SIZE, y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        openButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        openButton.setBackground(Color.LIGHT_GRAY);
        openButton.setFont(LABEL_FONT);
        openButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && isRightClickEnabled) {
                    setFlagState((flagState + 1) % 3);
                }
                if (SwingUtilities.isLeftMouseButton(e) && isLeftClickEnabled) {
                    if (!minerField.isFirstClicked()) {
                        minerField.firstClick(x, y);
                    }
                    open();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    minerField.getRefreshButton().fieldClicked();
                    mouseEntered(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    minerField.getRefreshButton().normal();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        contentLabel = new JLabel();
        contentLabel.setBounds(x * BOX_SIZE, y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        contentLabel.setBorder(BorderFactory.createMatteBorder(y == 0 ? 1 : 0, x == 0 ? 1 : 0,
                1,1, BORDERS_COLOR));
        contentLabel.setOpaque(true);
        contentLabel.setBackground(BOX_COLOR);
        contentLabel.setFont(LABEL_FONT);
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentLabel.setVerticalAlignment(JLabel.CENTER);
    }

    public void init() {
        isOpen = false;
        isOpenPretend = false;
        flagState = 0;
        isMine = false;
        minesAround = 0;
        contentLabel.setVisible(false);
        contentLabel.setIcon(null);
        contentLabel.setBackground(BOX_COLOR);
        contentLabel.setText("");
        openButton.setIcon(null);
        openButton.setText("");
        openButton.setVisible(true);
        isLeftClickEnabled = true;
        isRightClickEnabled = true;
    }

    public void simple_open() {
        isOpen = true;
        openButton.setVisible(false);
        contentLabel.setVisible(true);
    }

    public void open(){
        open_not_zero();
        if ((minesAround == 0) && (!isMine)) {
            minerField.putNearZeroToQueue(x, y);
            minerField.openQueueZeroBoxes();
        }
    }

    public void open_not_zero() {
        if (!isOpen) {
                if (isMine) {
                    contentLabel.setText("");
                    contentLabel.setIcon(MINE_BREAK_ICON);
                    contentLabel.setBackground(BREAK_MINE_COLOR);
                    minerField.fail();
                }
                simple_open();
                minerField.opened++;
                minerField.checkWin();
            }
    }

    public void fail_open() {
        if (!isOpen) {
            if ((isMine) && (flagState != 1)) {
                simple_open();
            }
            if ((!isMine) && (flagState == 1)) {
                contentLabel.setText(null);
                contentLabel.setIcon(WRONG_FLAG_ICON);
                simple_open();
            }
        }
        disable();
    }

    public void disable() {
        isLeftClickEnabled = false;
        isRightClickEnabled = false;
    }


    public void computeLabel() {
        if (!isMine) {
            if (minesAround != 0) {
                contentLabel.setText(Integer.toString(minesAround));
                contentLabel.setForeground(NUMBER_COLORS[minesAround - 1]);
            }
        }
        else {
            contentLabel.setText("");
            contentLabel.setIcon(MINE_ICON);
        }
    }

    public void setFlagState(int flagState) {
        if ((this.flagState != 1) && flagState == 1) {
            minerField.decreaseDisplay();
        }
        if ((this.flagState == 1) && (flagState != 1)) {
            minerField.increaseDisplay();
        }

        this.flagState = flagState;
        switch (flagState) {
            case 0:
                openButton.setText(null);
                openButton.setIcon(null);
                isLeftClickEnabled = true;
                break;
            case 1:
                openButton.setText(null);
                openButton.setIcon(FLAG_ICON);
                isLeftClickEnabled = false;
                break;
            case 2:
                openButton.setText("?");
                openButton.setIcon(null);
                isLeftClickEnabled = true;
                break;
        }
    }

    // getters\setters:

    public JButton getOpenButton() {
        return openButton;
    }

    public JLabel getContentLabel() {
        return contentLabel;
    }

    public void setMinerField(MinerField minerField) {
        this.minerField = minerField;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

}
