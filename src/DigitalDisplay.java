import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DigitalDisplay extends JLabel {
    private int value = 0;
    private JPanel refreshPanel;

    private static Font DIGITAL_FONT;

    private static final int DISPLAY_WIGHT = 6*13;
    private static final int DISPLAY_HEIGHT = 3*13;
    private static final float FONT_SIZE = 4.0f*13;

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private StringBuilder displayStringBuilder = new StringBuilder(3);

    static {
        try {
            DIGITAL_FONT = Font.createFont(Font.TRUETYPE_FONT,
                    classLoader.getResourceAsStream("assets/digital_font.ttf"))
                    .deriveFont(FONT_SIZE);
        }
        catch (Exception e) {

        }
    }

    public DigitalDisplay(JPanel refreshPanel, int displayPadding) {
        this.refreshPanel = refreshPanel;
        setBounds(displayPadding, (refreshPanel.getHeight() - DISPLAY_HEIGHT)/ 2,
                DISPLAY_WIGHT, DISPLAY_HEIGHT);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setOpaque(true);
        setBackground(Color.BLACK);
        setForeground(Color.RED);
        setFont(DIGITAL_FONT);
        setText("000");
    }


    public int getValue() {
        return value;
    }


    // form 000 to 999 positive values. If value > 999, we display 999
    // form -01 to -99 negative values. If value < -99, we display value % 100.
    public void setValue(int value) {
        this.value = value;
        value = Math.min(999, value);
        displayStringBuilder.setLength(0);
        if (value >= 0) {
            for(int i = 0; i < 3; i++) {
                int currentDigit = value % 10;
                displayStringBuilder.append(currentDigit);
                value = value / 10;
            }
        }
        else {
            value = -value;
            for(int i = 0; i < 2; i++) {
                int currentDigit = value % 10;
                displayStringBuilder.append(currentDigit);
                value = value / 10;
            }
            displayStringBuilder.append('-');
        }
        displayStringBuilder.reverse();
        setText(displayStringBuilder.toString());
    }

    public void incrementValue() {
        setValue(++value);
    }

    public void decrementValue() {
        setValue(--value);
    }

}