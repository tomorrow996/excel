package com.cd.tech.report.util.text.impl;

import com.cd.tech.report.util.text.CharactorUtils;
import com.cd.tech.report.util.text.Normalize;
import org.springframework.stereotype.Service;

/**
 * Created by dennislee on 13-12-6.
 * 简单的归一化，去除空格、全角转半角，英文符号转中文
 */
@Service
public class BaseNormalize implements Normalize {
    public String normalize(CharSequence charSequence) {
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); i++) {
            char c = charSequence.charAt(i);
//            if (CharactorUtils.isBlank(c)) {
//                continue;
//            }
            if (CharactorUtils.isDBC(c)) {
                c = CharactorUtils.toSBC(c);
            }
            //英文转中文字符
            if (CharactorUtils.isAlphaSymbol(c)) {
                c = CharactorUtils.alphaToChinese(c);
            }
            /*if (Character.isLowerCase(c)) {
                c = Character.toUpperCase(c);
            }*/
            stringBuilder.append(c);
        }
        stringBuilder.trimToSize();
        return stringBuilder.toString();
    }


    public String normalizeForSmsFilter(CharSequence charSequence, boolean dropBlank) {
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); i++) {
            char c = charSequence.charAt(i);
            if (dropBlank) {
                if (CharactorUtils.isBlank(c)) {
                    continue;
                }
            }
            if (CharactorUtils.isDBC(c)) {
                c = CharactorUtils.toSBC(c);
            }
            //中文转英文字符
            if (CharactorUtils.isChineseSymbol(c)) {
                c = CharactorUtils.chineseToAlpha(c);
            }
            if (Character.isLowerCase(c)) {
                c = Character.toUpperCase(c);
            }
            stringBuilder.append(c);
        }
        stringBuilder.trimToSize();
        return stringBuilder.toString();
    }

    public static void main(String[] arg) {
        char c1 = '-';
        char c2 = '－';
        char c3 = '—';
        char c4 = '_';

        System.out.println((int) c1);
        System.out.println((int) c2);
        System.out.println((int) c3);
        System.out.println((int) c4);
        //        map.put((char) 8211, (char) 8212);
//        map.put((char) 8213, (char) 8212);
//        map.put((char) 45, (char) 8212);
//        map.put((char) 95, (char) 8212);
    }
}
