package cn.enaium.name.obfuscator.setting;

/**
 * @author Enaium
 */
public class StringSetting extends Setting{
    private String string;

    public StringSetting(String name, String description, String string) {
        super(name, description);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
