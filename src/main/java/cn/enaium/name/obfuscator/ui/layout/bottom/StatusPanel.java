package cn.enaium.name.obfuscator.ui.layout.bottom;

import cn.enaium.name.obfuscator.ui.MainGUI;

import javax.swing.*;

/**
 * @author Enaium
 */
public class StatusPanel extends JPanel {

    public static final JLabel STATUS_LABEL = new JLabel(MainGUI.NAME + " | " + MainGUI.VERSION + " | By Enaium");

    public static void reset() {
        STATUS_LABEL.setText(MainGUI.NAME + " | " + MainGUI.VERSION + " | By Enaium");
    }

    public StatusPanel() {
        setBorder(BorderFactory.createLoweredBevelBorder());
        add(STATUS_LABEL);
    }
}
