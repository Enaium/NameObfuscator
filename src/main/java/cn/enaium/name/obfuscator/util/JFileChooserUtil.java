package cn.enaium.name.obfuscator.util;

import cn.enaium.name.obfuscator.NameObfuscator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * @author Enaium
 */
public class JFileChooserUtil {

    private JFileChooserUtil() {
        throw new IllegalAccessError("Utility");
    }

    public static File show(Type type) {
        return show(type, JFileChooser.FILES_ONLY);
    }

    public static File show(Type type, int mode) {
        return show(type, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".zip") || file.getName().endsWith(".jar") || file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Zip File(*.zip,*.jar)";
            }
        }, mode);
    }

    public static File show(Type type, FileFilter fileFilter) {
        return show(type, fileFilter, JFileChooser.FILES_ONLY);
    }

    public static File show(Type type, FileFilter fileFilter, int mode) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setFileSelectionMode(mode);
        jFileChooser.removeChoosableFileFilter(jFileChooser.getAcceptAllFileFilter());
        jFileChooser.addChoosableFileFilter(fileFilter);

        if (type.equals(Type.OPEN)) {
            jFileChooser.showOpenDialog(NameObfuscator.mainGUI);
        } else if (type.equals(Type.SAVE)) {
            jFileChooser.showSaveDialog(NameObfuscator.mainGUI);
        }

        return jFileChooser.getSelectedFile();
    }

    public enum Type {
        OPEN,
        SAVE
    }
}
