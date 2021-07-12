package cn.enaium.name.obfuscator.ui.layout.center;

import cn.enaium.name.obfuscator.ui.layout.center.panel.ControlPanel;
import cn.enaium.name.obfuscator.ui.layout.center.panel.SelectFilePanel;

import javax.swing.*;

/**
 * @author Enaium
 */
public class CenterLayout extends JSplitPane {
    public CenterLayout() {
        super(JSplitPane.VERTICAL_SPLIT, new SelectFilePanel(), new ControlPanel());
        setContinuousLayout(true);
        setOneTouchExpandable(true);
    }
}
