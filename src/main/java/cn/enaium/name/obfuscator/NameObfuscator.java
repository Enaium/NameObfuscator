package cn.enaium.name.obfuscator;

import cn.enaium.name.obfuscator.ui.MainGUI;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 * @author Enaium
 */
public class NameObfuscator {

    public static MainGUI mainGUI;

    public static void main(String[] args) {
        Config.load();
        FlatDarculaLaf.setup();
        mainGUI = new MainGUI();
        mainGUI.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(Config::save));
    }
}
