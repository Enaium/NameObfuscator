package cn.enaium.name.obfuscator.ui.layout.center.panel.control;

import cn.enaium.name.obfuscator.util.JFileChooserUtil;
import cn.enaium.name.obfuscator.util.MessageUtil;
import cn.enaium.name.obfuscator.util.StringUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * @author Enaium
 */
public class StringPoolPanel extends JPanel {

    public static final JTextArea POOL = new JTextArea();

    public StringPoolPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(POOL), BorderLayout.CENTER);
        final var inputButton = new JButton("Input");
        add(inputButton, BorderLayout.EAST);

        inputButton.addActionListener(e -> {
            try {
                final var show = JFileChooserUtil.show(JFileChooserUtil.Type.OPEN, new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.getName().endsWith(".txt") || file.getName().endsWith(".srt") || file.getName().endsWith(".lrc") || file.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "String Pool(*.txt,*.srt,*.lrc)";
                    }
                });

                final var strings = new ArrayList<String>();

                if (show != null) {
                    for (String readAllLine : Files.readAllLines(new File(show.getAbsolutePath()).toPath())) {
                        if (!readAllLine.isEmpty()) {
                            strings.add(readAllLine.replace(" ", "").replace("\\", "").replaceAll("[\\s\\p{P}\\n\\r=+$<>^`~|0-9.*!@#%&()\",]", ""));
                        }
                    }

                    if (show.getName().endsWith(".txt")) {
                        strings.forEach(it -> POOL.append(it + "\n"));
                    } else if (show.getName().endsWith(".srt")) {
                        StringUtil.parseSrt(strings).forEach(POOL::append);
                    } else if (show.getName().endsWith(".lrc")) {
                        StringUtil.parseLrc(strings).forEach(POOL::append);
                    }
                }
            } catch (IOException exception) {
                MessageUtil.error(exception);
            }
        });

    }
}
