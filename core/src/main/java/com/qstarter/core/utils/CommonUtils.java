package com.qstarter.core.utils;

import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

/**
 * @author peter
 * date: 2019-12-18 09:59
 **/
public enum CommonUtils {
    ;
    private static final Random random = new Random();


    public static Integer simpleRandomNumber(Integer minBound, Integer maxBound) {
        return random.nextInt(maxBound - minBound + 1) + minBound;
    }

    public static void main(String[] args) {
        int i = 100;
        while (i >= 0) {
            System.out.println(simpleRandomNumber(15, 16));
            i--;
        }
    }
    /**
     * 比较的两个版本号的大小(格式：xxx.xxx.xx.xx ...)
     *
     * @param oldVersion 旧版本号
     * @param newVersion 新版本号
     * @return newVersion > oldVersion :true; 否则为false
     */
    public static boolean compareAppVersion(String oldVersion, String newVersion) {

        String symbol = "\\.";

        //旧版本
        String[] ov = oldVersion.split(symbol);
        //新版本
        String[] nv = newVersion.split(symbol);

        //检测
        try {
            isAllNumber(ov);
        } catch (NumberFormatException e) {
            throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION,"oldVersion 必须全部为数字");
        }
        try {
            isAllNumber(nv);
        } catch (NumberFormatException e) {
            throw new SystemServiceException(ErrorMessageEnum.APP_VERSION_EXCEPTION,"newVersion 必须全部为数字");

        }
        //新版本的长度大于旧版本则默认为大于旧版本
        if (nv.length > ov.length) {
            return true;
        }
        int compare = getVersionNumberSum(nv).compareTo(getVersionNumberSum(ov));

        return compare > 0;

    }

    public static void isAllNumber(String[] args) throws NumberFormatException {
        Arrays.stream(args).forEach(Integer::parseInt);
    }


    private static BigDecimal getVersionNumberSum(String[] version) {
        int radix = 10;
        //最高位
        int maxUnit = version.length - 1;
        double total = 0.0;

        for (String aVersion : version) {
            int anInt = Integer.parseInt(aVersion);

            //该位的数字转化为小于10的数，再乘以10的最高位幂
            long pow = (long) Math.pow(radix, maxUnit);

            double v = anInt / Math.pow(radix, aVersion.length() - 1);
            total += v * pow;
            maxUnit--;
        }

        return new BigDecimal(total);
    }
}


