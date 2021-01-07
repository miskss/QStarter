package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;

import java.math.BigDecimal;

/**
 * @author peter
 * date 2020/7/30 11:10
 */
public class DoubleCellHandler extends DefaultCellHandler {

    @Override
    public Double readCell(Cell cellVal) {
        Object o = super.readCell(cellVal);
        if (o instanceof BigDecimal) {
            BigDecimal number = (BigDecimal) o;
            return number.doubleValue();
        } else {
            try {
                String string = String.valueOf(o);
                return Double.parseDouble(string);
            } catch (NumberFormatException e) {
                throw new CellValueParseException(String.format("第%d行%d列【%s】值类型不正确，只能是数字", cellVal.getAddress().getRow() + 1,
                        cellVal.getAddress().getColumn() + 1, cellVal.getText()
                ));
            }
        }
    }
}
