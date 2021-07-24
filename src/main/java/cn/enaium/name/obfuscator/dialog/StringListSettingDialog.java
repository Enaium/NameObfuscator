package cn.enaium.name.obfuscator.dialog;

import cn.enaium.name.obfuscator.setting.StringListSetting;

import javax.swing.*;
import java.awt.*;

/**
 * @author Enaium
 */
public class StringListSettingDialog extends Dialog {
    public StringListSettingDialog(StringListSetting setting) {
        super(setting.getName() + " | " + setting.getDescription());
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        var stringDefaultListModel = new DefaultListModel<String>();
        var jList = new JList<>(stringDefaultListModel);
        setting.getStringList().forEach(stringDefaultListModel::addElement);
        content.add(new JScrollPane(jList), BorderLayout.CENTER);
        JPanel control = new JPanel();
        control.setLayout(new GridLayout(1, 3));
        JTextField jTextField = new JTextField();
        control.add(jTextField);
        JButton add = new JButton("Add");
        add.addActionListener(e -> {
            if (!jTextField.getText().replace(" ", "").isEmpty()) {
                stringDefaultListModel.addElement(jTextField.getText());
                setting.getStringList().add(jTextField.getText());
            }
        });
        control.add(add);
        JButton remove = new JButton("Remove");
        remove.addActionListener(e -> {
            stringDefaultListModel.removeElement(jList.getSelectedValue());
            setting.getStringList().clear();
            for (Object o : stringDefaultListModel.toArray()) {
                setting.getStringList().add((String) o);
            }

        });
        control.add(remove);
        content.add(control, BorderLayout.NORTH);
        setContentPane(content);
    }
}
