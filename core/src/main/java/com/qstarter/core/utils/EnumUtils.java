package com.qstarter.core.utils;

import com.qstarter.core.enums.ConvertEnumFromVal;
import com.qstarter.core.enums.ErrorMessageEnum;
import com.qstarter.core.exceptions.SystemServiceException;

import java.util.Objects;

/**
 * 枚举转换工具类
 *
 * @author peter
 * date: 2019-09-09 08:56
 **/
public final class EnumUtils {


    /**
     * 将枚举值转换为枚举（该枚举类必须实现{@link ConvertEnumFromVal}接口）
     *
     * @param enumClazz 枚举类
     * @param value     值
     * @param <ATTR>    枚举类型
     * @param <TYPE>    值类型
     * @return ATTR
     */
    public static <ATTR extends Enum<ATTR> & ConvertEnumFromVal<TYPE>, TYPE> ATTR convertToEnum(Class<ATTR> enumClazz, TYPE value) {
        ATTR[] enumConstants = enumClazz.getEnumConstants();
        for (ATTR enumConstant : enumConstants) {
            if (Objects.equals(value, enumConstant.getVal())) {
                return enumConstant;
            }
        }
        throw new SystemServiceException(ErrorMessageEnum.NO_SUCH_ENUM_EXCEPTION, "no such " + enumClazz.getSimpleName() + " enum");
    }


}
