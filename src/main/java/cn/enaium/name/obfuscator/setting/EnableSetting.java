package cn.enaium.name.obfuscator.setting;

/**
 * @author Enaium
 */
public class EnableSetting extends Setting {
    private boolean enable;

    public EnableSetting(String name, String description, boolean enable) {
        super(name, description);
        this.enable = enable;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
