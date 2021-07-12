package cn.enaium.name.obfuscator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Enaium
 */
public class StringUtil {
    public static final char[][] cha = new char[][]
            {
                    {'i', 'l', 'I', 'L', 'j', 'J'},
                    {'o', 'O', '〇'},
                    {'q', 'p', 'Q', 'P'},
                    {'s', 'S'},
                    {'ラ', 'う'},
                    {'よ', 'ょ'},
                    {'ゆ', 'ゅ'},
                    {'ㄙ', 'ム'},
                    {'ㄑ', 'く'},
                    {'一', '二', '三', '亖'},
                    {'ぃ', 'い', 'り', 'リ'},
                    {'シ', 'ツ', 'ソ', 'ン', 'ジ', 'ヅ', 'ゾ'},
                    {'へ', 'ㇸ', '乀'},
                    {'回', '茴', '佪', '徊'},
                    {'桂', '佳', '洼', '烓', '茥', '垚', '眭', '晆', '㤬', '䞨'},
                    {'水', '沝', '淼', '㵘'},
                    {'木', '林', '森'},
                    {'火', '炏', '炎', '焱', '欻', '燚'},
                    {'土', '圭', '茥', '洼', '桂', '烓', '垚', '㙓', '澆'},
                    {'鹅', '鵞'},
                    {'あ', 'お'},
                    {'メ', 'ノ'},
                    {'㬺', '幐'},
                    {'墫', '壿'},
                    {'鬬', '鬭'},
                    {'晚', '晩'},
                    {'凉', '凉'}
            };

    public static List<String> parseSrt(ArrayList<String> strings) {
        ArrayList<String> list = new ArrayList<String>();
        for (String string : strings) {
            if (string.matches("[0-9]+")) {
                continue;
            }

            if (string.contains("-->")) {
                continue;
            }

            if (string.contains("}")) {
                string = string.substring(string.lastIndexOf("}") + 1);
            }
            list.add(string.replace(" ", "") + "\n");
        }
        return list;
    }

    public static List<String> parseLrc(ArrayList<String> strings) {
        ArrayList<String> list = new ArrayList<String>();
        for (String string : strings) {
            list.add(string.substring(string.lastIndexOf("]") + 1) + "\n");
        }
        return list;
    }
}
