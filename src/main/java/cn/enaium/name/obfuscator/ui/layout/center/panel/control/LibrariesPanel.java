package cn.enaium.name.obfuscator.ui.layout.center.panel.control;

import cn.enaium.name.obfuscator.Config;
import cn.enaium.name.obfuscator.util.JFileChooserUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Enaium
 */
public class LibrariesPanel extends JPanel {
    public LibrariesPanel() {
        setLayout(new BorderLayout());
        var stringDefaultListModel = new DefaultListModel<String>();
        var list = new JList<>(stringDefaultListModel);
        add(new JScrollPane(list), BorderLayout.CENTER);
        final var jPanel = new JPanel(new BorderLayout());
        final var add = new JButton("Add");
        final var remove = new JButton("Remove");
        jPanel.add(add, BorderLayout.NORTH);
        jPanel.add(remove, BorderLayout.SOUTH);
        add(jPanel, BorderLayout.EAST);

        Config.LIBRARIES.getStringList().forEach(stringDefaultListModel::addElement);

        add.addActionListener(e -> {
            final var show = JFileChooserUtil.show(JFileChooserUtil.Type.OPEN, JFileChooser.FILES_AND_DIRECTORIES);
            if (show != null) {
                stringDefaultListModel.addElement(show.getAbsolutePath());
                Config.LIBRARIES.getStringList().add(show.getAbsolutePath());
            }
        });

        remove.addActionListener(e -> {
            if (list.getSelectedValue() != null) {
                stringDefaultListModel.removeElement(list.getSelectedValue());
                var l = new ArrayList<String>();
                for (Object o : stringDefaultListModel.toArray()) {
                    l.add((String) o);
                }
                Config.LIBRARIES.setStringList(l);
            }
        });
    }
}
