package cn.enaium.name.obfuscator.setting;

import java.util.List;

/**
 * @author Enaium
 */
public class StringListSetting extends Setting {
    private List<String> stringList;

    public StringListSetting(String name, String description, List<String> stringList) {
        super(name, description);
        this.stringList = stringList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
