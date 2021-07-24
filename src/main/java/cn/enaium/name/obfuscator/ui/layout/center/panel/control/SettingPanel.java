package cn.enaium.name.obfuscator.ui.layout.center.panel.control;

import cn.enaium.name.obfuscator.Config;
import cn.enaium.name.obfuscator.annotation.ExcludeUI;
import cn.enaium.name.obfuscator.dialog.StringListSettingDialog;
import cn.enaium.name.obfuscator.setting.EnableSetting;
import cn.enaium.name.obfuscator.setting.Setting;
import cn.enaium.name.obfuscator.setting.StringListSetting;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * @author Enaium
 */
public class SettingPanel extends JPanel {
    public SettingPanel() {
        setLayout(new BorderLayout());
        final var jPanel = new JPanel();
        final var gridLayout = new GridLayout(0, 2);
        jPanel.setLayout(gridLayout);
        jPanel.setPreferredSize(new Dimension(512, 256));
        for (Field field : Config.class.getFields()) {
            if (field.isAnnotationPresent(ExcludeUI.class)) continue;
            try {
                final var o = field.get(null);
                if (o instanceof Setting) {
                    var name = ((Setting) o).getName();
                    var description = ((Setting) o).getDescription();
                    if (o instanceof EnableSetting) {
                        final var jCheckBox = new JCheckBox(name);
                        jCheckBox.setSelected(((EnableSetting) o).getEnable());
                        jCheckBox.addActionListener(e -> ((EnableSetting) o).setEnable(jCheckBox.isSelected()));
                        jPanel.add(jCheckBox);
                        jPanel.add(new JLabel(description));
                    } else if (o instanceof StringListSetting) {
                        JButton set = new JButton("Set");
                        set.addActionListener(e -> {
                            new StringListSettingDialog((StringListSetting) o);
                        });
                        jPanel.add(set);
                        jPanel.add(new JLabel(description));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        final var jScrollPane = new JScrollPane(jPanel);
        add(jScrollPane, BorderLayout.CENTER);
    }
}
