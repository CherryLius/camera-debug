package com.example.test.util;

/**
 * Created by Administrator on 2017/5/3.
 */

public class Utils {

    public static String getSectionIndexer(String string) {
        return getAbbreviation(string, '#');
    }

    public static String getAbbreviation(String string, char defaultAbbreviation) {
        if (string == null || string.isEmpty()) {
            return "" + defaultAbbreviation;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            sb.append(Char2Initial(string.charAt(i), defaultAbbreviation));
        }
        return sb.toString();
    }


    /**
     * get initial of char
     *
     * @param ch
     * @param defaultAbbreviation
     * @return Initial with upper size , or defaultAbbreviation
     */
    private static char Char2Initial(char ch, char defaultAbbreviation) {
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
        {
            return defaultAbbreviation;
        }

        int i = 1;
        while (i < table.length) {
            if (gb < table[i]) {
                i--;
                break;
            }
            i++;
        }
        return initialtable[i];
    }


    private static int gbValue(char ch) {
        String str = "" + ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int BEGIN = 45217;
    private static int END = 63486;
    /**
     * each item is the fist Chinese word of every initial consonant in GB2312
     * {i、u、v} is not among the table
     */
    private static char[] chartable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '击', '喀', '垃',
            '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '挖', '昔', '压', '匝'};

    private static char[] initialtable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I','J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U','V','W', 'X', 'Y', 'Z'};

    private static int[] table = new int[chartable.length + 1];
}
