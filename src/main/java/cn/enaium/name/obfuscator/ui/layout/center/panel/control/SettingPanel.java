package cn.enaium.name.obfuscator.ui.layout.center.panel.control;

import cn.enaium.name.obfuscator.Config;
import cn.enaium.name.obfuscator.annotation.ExcludeUI;
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
                        DefaultComboBoxModel<String> stringDefaultComboBoxModel = new DefaultComboBoxModel<>();
                        final var stringJComboBox = new JComboBox<>(stringDefaultComboBoxModel);
                        stringJComboBox.setName(name);
                        ((StringListSetting) o).getStringList().forEach(stringDefaultComboBoxModel::addElement);
                        final var stringListPanel = new JPanel();
                        stringListPanel.setLayout(new GridLayout(1, 4));
                        stringListPanel.add(stringJComboBox);
                        final var jTextField = new JTextField();
                        stringListPanel.add(jTextField);

                        final var add = new JButton("Add");
                        final var remove = new JButton("Remove");
                        add.addActionListener(e -> {
                            if (!jTextField.getText().isEmpty()) {
                                stringDefaultComboBoxModel.addElement(jTextField.getText());
                                ((StringListSetting) o).getStringList().add(jTextField.getText());
                            }
                        });

                        remove.addActionListener(e -> {
                            if (stringJComboBox.getModel().getSelectedItem() != null) {
                                stringDefaultComboBoxModel.removeElement(stringJComboBox.getModel().getSelectedItem());
                                ((StringListSetting) o).getStringList().remove(stringJComboBox.getModel().getSelectedItem());
                            }
                        });

                        stringListPanel.add(add);
                        stringListPanel.add(remove);

                        jPanel.add(stringListPanel);
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
