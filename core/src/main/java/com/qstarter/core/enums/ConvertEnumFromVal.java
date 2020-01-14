package com.qstarter.core.enums;

/**
 * 将值转换为枚举类型的接口
 *
 * @param <TYPE> 值类型
 */
public interface ConvertEnumFromVal<TYPE> {
    TYPE getVal();
}
