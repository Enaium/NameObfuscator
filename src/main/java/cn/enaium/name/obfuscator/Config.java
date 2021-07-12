package cn.enaium.name.obfuscator;

import cn.enaium.name.obfuscator.annotation.ExcludeUI;
import cn.enaium.name.obfuscator.setting.EnableSetting;
import cn.enaium.name.obfuscator.setting.Setting;
import cn.enaium.name.obfuscator.setting.StringListSetting;
import cn.enaium.name.obfuscator.setting.StringSetting;
import cn.enaium.name.obfuscator.util.MessageUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Enaium
 */
@SuppressWarnings("unchecked")
public class Config {
    public static final EnableSetting CLASS_NAME = new EnableSetting("Class name", "Obfuscate class name", true);
    public static final EnableSetting FIELD_NAME = new EnableSetting("Field name", "Obfuscate field name", true);
    public static final EnableSetting METHOD_NAME = new EnableSetting("Method name", "Obfuscate method name", true);
    public static final EnableSetting LOCAL_VARIABLE_NAME = new EnableSetting("Local variable Name", "Obfuscate local variable name", true);
    public static final EnableSetting ENABLE_FILTER_CLASS_NAME = new EnableSetting("Enable Filter class", "Enable Filter class", true);
    public static final StringListSetting FILTER_CLASS_NAME = new StringListSetting("Filter class", "Not Obfuscate class", Collections.singletonList("cn.enaium.example"));
    public static final EnableSetting REMOVE_SOURCE = new EnableSetting("Remove source", "Remove class source name", true);
    public static final EnableSetting REMOVE_LINE_NUMBER = new EnableSetting("Remove line number", "Remove class line number", true);
    @ExcludeUI
    public static final StringSetting INPUT_JAR = new StringSetting("Input Jar", "Obfuscate input path", "");
    @ExcludeUI
    public static final StringSetting OUTPUT_JAR = new StringSetting("Output Jar", "Obfuscate output path", "");
    @ExcludeUI
    public static final StringListSetting LIBRARIES = new StringListSetting("Libraries", "Obfuscate libraries", Collections.emptyList());


    private static final File path = new File(".", "config.json");

    public static void load() {
        try {
            if (!path.exists()) {
                return;
            }

            final var jsonObject = new Gson().fromJson(Files.readString(path.toPath()), JsonObject.class);
            for (Field field : Config.class.getFields()) {
                try {
                    final var o = field.get(null);
                    if (o instanceof Setting) {
                        final var name = ((Setting) o).getName();
                        if (jsonObject.has(name)) {
                            if (o instanceof EnableSetting) {
                                ((EnableSetting) o).setEnable(jsonObject.get(name).getAsBoolean());
                            } else if (o instanceof StringListSetting) {
                                ((StringListSetting) o).setStringList(new Gson().fromJson(new Gson().toJson(jsonObject.get(name)), ArrayList.class));
                            } else if (o instanceof StringSetting) {
                                ((StringSetting) o).setString(jsonObject.get(name).getAsString());
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    MessageUtil.error(e);
                }
            }

        } catch (IOException e) {
            MessageUtil.error(e);
        }
    }

    public static void save() {
        final var jsonObject = new JsonObject();
        for (Field field : Config.class.getFields()) {
            try {
                final var o = field.get(null);
                if (o instanceof Setting) {
                    final var name = ((Setting) o).getName();
                    if (o instanceof EnableSetting) {
                        jsonObject.addProperty(name, ((EnableSetting) o).getEnable());
                    } else if (o instanceof StringListSetting) {
                        List<String> stringList = ((StringListSetting) o).getStringList();
                        if (!stringList.isEmpty()) {
                            jsonObject.add(name, new Gson().fromJson(new Gson().toJson(stringList), JsonArray.class));
                        }
                    } else if (o instanceof StringSetting) {
                        jsonObject.addProperty(name, ((StringSetting) o).getString());
                    }
                }
            } catch (IllegalAccessException e) {
                MessageUtil.error(e);
            }
        }
        try {
            Files.write(path.toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            MessageUtil.error(e);
        }
    }
}
