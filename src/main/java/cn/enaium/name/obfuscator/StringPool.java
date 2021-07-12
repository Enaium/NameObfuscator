package cn.enaium.name.obfuscator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Enaium
 */
public class StringPool {
    private final List<String> strings = new ArrayList<>();
    private int index = 0;

    public StringPool(List<String> strings) {
        this.strings.addAll(new LinkedHashSet<>(strings));
        after();
    }

    public StringPool(String[] strings) {
        final ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, strings);
        this.strings.addAll(new LinkedHashSet<>(arrayList));
        after();
    }

    private void after() {
        strings.removeIf(String::isEmpty);
    }

    public boolean re = false;

    public String next() {
        if (index == strings.size()) {
            index = 0;
            re = true;
        }

        String s = strings.get(index);

        if (re) {
            s += RandomStringUtils.randomAlphabetic(10);
        }

        index++;
        return s;
    }

    public List<String> getStrings() {
        return strings;
    }
}
