package com.qstarter.app.utils;

import com.google.common.base.Strings;

/**
 * @author peter
 * date: 2019-11-29 14:43
 **/
public class FormatStringUtils {

    public static String formatBankCarNum(String cardNum) {

        if (Strings.isNullOrEmpty(cardNum)) return cardNum;

        char[] chars = cardNum.toCharArray();

        char[] newChar = new char[cardNum.length()];

        for (int i = 0; i < chars.length; i++) {
            if (i > 7 && i < chars.length - 4) {
                newChar[i] = '*';
            } else {
                newChar[i] = chars[i];
            }
        }
        return String.valueOf(newChar);
    }

}
