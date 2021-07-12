package cn.enaium.name.obfuscator.ui.layout.center.panel;

import cn.enaium.name.obfuscator.Config;
import cn.enaium.name.obfuscator.util.JFileChooserUtil;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * @author Enaium
 */
public class SelectFilePanel extends JPanel {

    private static final JLabel INPUT_JAR_LABEL = new JLabel("Input Jar:");
    private static final JLabel OUTPUT_JAR_LABEL = new JLabel("Output Jar:");
    public static final JTextField INPUT_JAR_TEXT_FIELD = new JTextField();
    public static final JTextField OUTPUT_JAR_TEXT_FIELD = new JTextField();
    private static final JButton INPUT_JAR_BUTTON = new JButton("...");
    private static final JButton OUTPUT_JAR_BUTTON = new JButton("...");

    public SelectFilePanel() {
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        INPUT_JAR_TEXT_FIELD.setText(Config.INPUT_JAR.getString());
        OUTPUT_JAR_TEXT_FIELD.setText(Config.OUTPUT_JAR.getString());


        INPUT_JAR_TEXT_FIELD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Config.INPUT_JAR.setString(INPUT_JAR_TEXT_FIELD.getText());
            }
        });

        OUTPUT_JAR_TEXT_FIELD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Config.OUTPUT_JAR.setString(INPUT_JAR_TEXT_FIELD.getText());
            }
        });

        INPUT_JAR_BUTTON.addActionListener(e -> {
            final File show = JFileChooserUtil.show(JFileChooserUtil.Type.OPEN);
            if (show != null) {
                final var input = show.getAbsoluteFile().getPath();
                final var output = show.getParent() + File.separator + show.getName().substring(0, show.getName().lastIndexOf(".")) + "-Obf" + show.getName().substring(show.getName().lastIndexOf("."));
                INPUT_JAR_TEXT_FIELD.setText(input);
                Config.INPUT_JAR.setString(input);

                if (OUTPUT_JAR_TEXT_FIELD.getText().isEmpty()) {
                    Config.OUTPUT_JAR.setString(output);
                    OUTPUT_JAR_TEXT_FIELD.setText(output);
                }
            }
        });

        OUTPUT_JAR_BUTTON.addActionListener(e -> {
            final File show = JFileChooserUtil.show(JFileChooserUtil.Type.SAVE);
            if (show != null) {
                OUTPUT_JAR_TEXT_FIELD.setText(show.getAbsoluteFile().getPath());
                Config.OUTPUT_JAR.setString(show.getAbsoluteFile().getPath());
            }
        });

        GridBagConstraints inputLabelGBC = new GridBagConstraints();
        inputLabelGBC.gridx = 0;
        inputLabelGBC.gridy = 0;
        add(INPUT_JAR_LABEL, inputLabelGBC);

        GridBagConstraints inputFieldTextGBC = new GridBagConstraints();
        inputFieldTextGBC.fill = GridBagConstraints.HORIZONTAL;
        inputFieldTextGBC.gridx = 1;
        inputFieldTextGBC.gridy = 0;
        add(INPUT_JAR_TEXT_FIELD, inputFieldTextGBC);

        GridBagConstraints inputButtonTextGBC = new GridBagConstraints();
        inputButtonTextGBC.gridx = 2;
        inputButtonTextGBC.gridy = 0;
        add(INPUT_JAR_BUTTON, inputButtonTextGBC);

        GridBagConstraints outputLabelGBC = new GridBagConstraints();
        outputLabelGBC.gridx = 0;
        outputLabelGBC.gridy = 1;
        add(OUTPUT_JAR_LABEL, outputLabelGBC);

        GridBagConstraints outputFieldTextGBC = new GridBagConstraints();
        outputFieldTextGBC.fill = GridBagConstraints.HORIZONTAL;
        outputFieldTextGBC.gridx = 1;
        outputFieldTextGBC.gridy = 1;
        add(OUTPUT_JAR_TEXT_FIELD, outputFieldTextGBC);

        GridBagConstraints outputButtonTextGBC = new GridBagConstraints();
        outputButtonTextGBC.gridx = 2;
        outputButtonTextGBC.gridy = 1;
        add(OUTPUT_JAR_BUTTON, outputButtonTextGBC);
    }
}
