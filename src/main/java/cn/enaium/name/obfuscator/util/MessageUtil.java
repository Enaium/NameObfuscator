package cn.enaium.name.obfuscator.util;

import cn.enaium.name.obfuscator.ui.layout.bottom.StatusPanel;

import javax.swing.*;

/**
 * @author Enaium
 */
public class MessageUtil {

    private MessageUtil() {
        throw new IllegalAccessError("Utility");
    }

    public static void error(Throwable e, String message) {
        e.printStackTrace();
        StatusPanel.reset();
        JOptionPane.showMessageDialog(null, message.isEmpty() ? e : e + " " + message, "ERROR", JOptionPane.ERROR_MESSAGE);
        Thread.currentThread().interrupt();
    }

    public static void error(Throwable e) {
        error(e, "");
    }
}
