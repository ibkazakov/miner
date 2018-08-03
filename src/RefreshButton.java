import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButton extends JButton {
    private static final int BUTTON_SIZE = 45;
    private JPanel refreshPanel;
    private MinerField minerField;
    private boolean fieldClickEnable = true;

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private static final Icon win_icon = new ImageIcon(classLoader.getResource("assets/win_icon.png"));
    private static final Icon fail_icon = new ImageIcon(classLoader.getResource("assets/fail_icon.png"));
    private static final Icon click_icon = new ImageIcon(classLoader.getResource("assets/click_icon.png"));
    private static final Icon normal_icon = new ImageIcon(classLoader.getResource("assets/normal_icon.png"));

    public RefreshButton(JPanel refreshPanel, MinerField minerField) {
        this.refreshPanel = refreshPanel;
        this.minerField = minerField;
        this.setBounds((refreshPanel.getWidth() - BUTTON_SIZE) / 2,
                (refreshPanel.getHeight() - BUTTON_SIZE) / 2,
                BUTTON_SIZE, BUTTON_SIZE);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setHorizontalAlignment(JButton.CENTER);
        this.setVerticalAlignment(JButton.CENTER);
        this.setIcon(normal_icon);

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        refreshPanel.add(this);
    }

    public void normal() {
        if (fieldClickEnable) {
            this.setIcon(normal_icon);
        }
    }

    public void fieldClicked() {
        if (fieldClickEnable) {
            this.setIcon(click_icon);
        }
    }

    public void win() {
        this.setIcon(win_icon);
        fieldClickEnable = false;
    }

    public void fail() {
        this.setIcon(fail_icon);
        fieldClickEnable = false;
    }

    private void refresh() {
        fieldClickEnable = true;
        this.setIcon(normal_icon);
        minerField.initRefresh();
    }

}
