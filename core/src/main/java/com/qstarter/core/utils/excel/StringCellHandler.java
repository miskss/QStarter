package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;

/**
 * @author peter
 * date 2020/7/30 11:12
 */
public class StringCellHandler extends DefaultCellHandler {
    @Override
    public String readCell(Cell cellVal) {
        if (cellVal == null || cellVal.getText() == null) return "";
        return cellVal.getText().trim();
    }
}
