package cn.enaium.name.obfuscator.ui;

import cn.enaium.name.obfuscator.ui.layout.bottom.BottomLayout;
import cn.enaium.name.obfuscator.ui.layout.bottom.StatusPanel;
import cn.enaium.name.obfuscator.ui.layout.center.CenterLayout;
import cn.enaium.name.obfuscator.util.MessageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Enaium
 */
public class MainGUI extends JFrame {

    public static final String NAME = "NameObfuscator";
    public static final String VERSION = MainGUI.class.getPackage().getImplementationVersion();

    public MainGUI() throws HeadlessException {
        setTitle(MainGUI.NAME + " | " + MainGUI.VERSION + " | By Enaium");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        add(new CenterLayout(), BorderLayout.CENTER);
        add(new BottomLayout(), BorderLayout.SOUTH);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu help = new JMenu("Help");
        JMenuItem contact = new JMenuItem("Contact");
        contact.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Enaium/NameObfuscator"));
            } catch (IOException | URISyntaxException ioException) {
                MessageUtil.error(ioException);
            }
        });
        help.add(contact);
        jMenuBar.add(help);
        setJMenuBar(jMenuBar);

    }
}
