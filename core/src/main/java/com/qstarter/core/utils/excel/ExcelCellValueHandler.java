package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;

/**
 * 处理 excel 单元格的值
 *
 * @author peter
 * create: 2020-01-08 09:55
 **/
public interface ExcelCellValueHandler<T> {
    /**
     * 往excel中写值
     *
     * @param value Object 对象中成员的值
     * @return String
     */
    String writeCell(Object value);

    /**
     * 读取excel值
     *
     * @param cellVal cell 单元格值
     * @return T
     */
    T readCell(Cell cellVal);


}
