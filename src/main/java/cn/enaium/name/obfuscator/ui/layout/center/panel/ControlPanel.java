package cn.enaium.name.obfuscator.ui.layout.center.panel;

import cn.enaium.name.obfuscator.ui.layout.center.panel.control.LibrariesPanel;
import cn.enaium.name.obfuscator.ui.layout.center.panel.control.SettingPanel;
import cn.enaium.name.obfuscator.ui.layout.center.panel.control.StringPoolPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class ControlPanel extends JPanel {
    public ControlPanel() {
        final var jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Setting", new SettingPanel());
        jTabbedPane.addTab("String pool", new StringPoolPanel());
        jTabbedPane.addTab("Libraries", new LibrariesPanel());
        setLayout(new BorderLayout());
        add(jTabbedPane, BorderLayout.CENTER);
    }
}
