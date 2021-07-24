package cn.enaium.name.obfuscator.dialog;

import cn.enaium.name.obfuscator.NameObfuscator;

import javax.swing.*;

public class Dialog extends JFrame {
    public Dialog(String title) {
        super(title);
        setLocation(NameObfuscator.mainGUI.getLocation());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }
}
