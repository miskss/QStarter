package com.qstarter.core.utils.excel;

import java.lang.annotation.*;

/**
 * excel 导出 时标记的列名
 *
 * @author peter
 * create: 2020-01-07 15:06
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ExcelColumn {

    /**
     * 列名
     */
    String value() default "";

    /**
     * 列的编号，从1开始，默认标记第0列为序号列
     */
    int columnIdx() default 1;

    /**
     * 对值需要特殊处理时
     */
    Class<? extends ExcelCellValueHandler> handler() default DefaultCellHandler.class;
}
