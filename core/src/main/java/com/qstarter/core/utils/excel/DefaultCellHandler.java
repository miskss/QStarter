package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;

/**
 * @author peter
 * date 2020/7/30 11:05
 */
public class DefaultCellHandler implements ExcelCellValueHandler<Object> {
    @Override
    public String writeCell(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public Object readCell(Cell cellVal) {
        if (cellVal == null) return null;
        switch (cellVal.getType()) {
            case STRING:
                return cellVal.asString().trim();
            case BOOLEAN:
                return cellVal.asBoolean();
            case NUMBER:
                return cellVal.asNumber();
            case ERROR:
            case FORMULA:
            case EMPTY:
            default:
                return null;
        }
    }
}
