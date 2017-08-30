package com.cd.tech.report.util.text;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dennislee
 * Date: 13-12-5
 * Time: 上午11:25
 * 提供常用的全角、半角、非打印字符判断以及转换。暂不支持日文和其他拉丁文。
 */
public class CharactorUtils {
    public static final Map<Character, Character> alphaToChinese;
    public static final Map<Character, Character> chineseToAlpha;
    private static final String MOBILE_REG = "(^|[^0-9a-zA-Z])(1[34578][0-9]|14[57])[0-9]{8}([^0-9a-zA-Z]|$)";
    private static final String EMAIL_REG = "(^|[^0-9a-zA-Z])[0-9a-zA-Z_.]+@[0-9a-zA-Z]+\\.(com|COM|net|NET|cn|CN|org|ORG)";
    private static final String CARD_REG = "(^|[^0-9a-zA-Z])(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|8[12])[0-9]{4}(19[0-9]{2}|200[0-9]|201[0-4])(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]([^0-9a-zA-Z\\r\\n]|$)";
    private static final Pattern mobile_prefetch = Pattern.compile(MOBILE_REG);
    private static final Pattern card_prefetch = Pattern.compile(CARD_REG);
    private static final Pattern email_prefetch = Pattern.compile(EMAIL_REG);

    static {
        Map<Character, Character> map = new HashMap<Character, Character>();
        map.put(',', '，');
        map.put('.', '。');
        map.put('?', '？');
        map.put('!', '！');
        map.put('<', '《');
        map.put('>', '》');
        map.put('_', (char) 8212);
        map.put((char) 8211, (char) 8212);
        map.put((char) 8213, (char) 8212);
        map.put((char) 45, (char) 8212);
        map.put('[', '【');
        map.put(']', '】');
        map.put(':', '∶');
        alphaToChinese = Collections.unmodifiableMap(map);

        map = new HashMap<Character, Character>();
        map.put('，', ',');
        map.put('。', '.');
        map.put('？', '?');
        map.put('！', '!');
        map.put('《', '<');
        map.put('》', '>');
        map.put((char) 95, (char) 45);
        map.put((char) 8211, (char) 45);
        map.put((char) 8213, (char) 45);
        map.put((char) 8212, (char) 45);
        map.put('【', '[');
        map.put('】', ']');
        map.put('∶', ':');
//
//        for (Map.Entry<Character, Character> e : alphaToChinese.entrySet()) {
//            map.put(e.getValue(), e.getKey());
//        }
        chineseToAlpha = Collections.unmodifiableMap(map);

    }

    /**
     * 是否是半角字符，仅支持判断半角的符号、英文字母和数字，日文暂不支持
     *
     * @param c
     * @return
     */
    public static boolean isSBC(char c) {
        return (c >= 32 && c <= 127);
    }

    /**
     * 是否全角字符，仅支持判断半角的符号、英文字母和数字，日文暂不支持
     *
     * @param c
     * @return
     */
    public static boolean isDBC(char c) {
        return c == 12288 || (c >= 33 + 65248 && c <= 127 + 65248);
    }

    /**
     * 将全角转换为半角
     *
     * @param c
     * @return
     */
    public static char toSBC(char c) {
        if (c == 12288) {
            return 32;
        }
        return (char) ((int) c - 65248);
    }

    /**
     * 是否是不可打印字符，如全角半角的空格、tab、回车、换行
     *
     * @param c
     * @return
     */
    public static boolean isBlank(char c) {
        return Character.isWhitespace(c);
    }

    /**
     * 是否可转换的英文符号
     *
     * @param c
     * @return
     */
    public static boolean isAlphaSymbol(char c) {
        return alphaToChinese.containsKey(c);
    }


    /**
     * 英文符号转中文
     *
     * @param c
     * @return
     */
    public static char alphaToChinese(char c) {
        return alphaToChinese.get(c);
    }

    /**
     * 中文字符转英文
     *
     * @param c
     * @return
     */
    public static char chineseToAlpha(char c) {
        return chineseToAlpha.get(c);
    }

    /**
     * 是否可转换的中文符号
     *
     * @param c
     * @return
     */
    public static boolean isChineseSymbol(char c) {
        return chineseToAlpha.containsKey(c);
    }

    /**
     * 讲半角转化为全角
     *
     * @param c
     * @return
     */
    public static char toDBC(char c) {
        if (c == 32) return 12288;
        else return (char) (c + 65248);
    }


    public static String hidenMobileCommon(String str) {
        if (StringUtils.isNotBlank(str)) {
            final Matcher m = mobile_prefetch.matcher(str);
            while (m.find()) {
                String key = m.group();
                int len = key.length();
                str = str.replace(key, key.substring(0, len - 8) + "****" + key.substring(len - 4, len));
            }
        }
        return str;
    }

    public static String hidenCardCommon(String str) {
        if (StringUtils.isNotBlank(str)) {
            final Matcher m = card_prefetch.matcher(str);
            while (m.find()) {
                String key = m.group();
                int len = key.length();
                str = str.replace(key, key.substring(0, len - 8) + "****" + key.substring(len - 4, len));
            }
        }
        return str;
    }


    public static String hideEmailCommon(String str) {
        if (StringUtils.isNotBlank(str)) {
            final Matcher m = email_prefetch.matcher(str);
            while (m.find()) {
                String key = m.group();
                int len = key.length();
                int start = key.indexOf("@");
                String newKey = "****" + key.substring(start + 1, len);
                str = str.replace(key, newKey);
            }
        }
        return str;
    }


    public static void main(String... args) {
//        System.out.println(isBlank((char) 0));
        String strMobile1 = " \"13997556733\", ";
        String strMobile2 = "13997556733";
        String strMobile3 = "a13997556733";
        String strMobile4 = " fmt not found by phone=13136541519, retry=true, sms=温馨提示：自2014年4月15日零时起，至2014年12月31日24时止，除阿里本地身份证居民以外，其他人员需开具边境通行证方可进入阿里地区。如有疑问，请咨询原出票地。";
        String strMobile5 = "";
        String strMobile6 = null;

        String strEmail1 = " tos:[13991030309@139.com,1@139.com,feafeaf@aa.com";
        String strEmail2 = "1@139.com";


        String strCard1 = "pl.getPnr:262  2-pltemp :NPassenger{name='陈志辉', cardNum='33062319790407019X', cardType='NI', eticketNum='781-2472878408'},eticketNum=781-2472878408";
        String strCard2 = "15250219570925022x";

//        System.out.println(hidenMobileCommon(strMobile1));
//        System.out.println(hidenMobileCommon(strMobile2));
//        System.out.println(hidenMobileCommon(strMobile3));
        System.out.println(hidenMobileCommon(strMobile4));
//        System.out.println(hidenMobileCommon(strMobile5));
//        System.out.println(hidenMobileCommon(strMobile6));
//        System.out.println(hideEmailCommon(strEmail1));
//        System.out.println(hideEmailCommon(strEmail2));
        System.out.println(hidenCardCommon(strCard1));
        System.out.println(hidenCardCommon(strCard2));
    }
}
