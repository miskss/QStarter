package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;

import java.math.BigDecimal;

/**
 * int 类型
 *
 * @author peter
 * date 2020/7/30 11:02
 */
public class IntegerCellHandler extends DefaultCellHandler {

    @Override
    public Integer readCell(Cell cellVal) {
        Object o = super.readCell(cellVal);
        if (o == null) return null;
        if (o instanceof BigDecimal) {
            BigDecimal number = (BigDecimal) o;
            if (number.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) < 0 || number.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new CellValueParseException(String.format("第%d行%d列【%s】值必须在 1~%d 之间", cellVal.getAddress().getRow() + 1,
                        cellVal.getAddress().getColumn() + 1, cellVal.getText(), Integer.MAX_VALUE
                ));
            }
            return number.intValue();
        } else {
            try {
                String string = String.valueOf(o);
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                throw new CellValueParseException(String.format("第%d行%d列【%s】值类型不正确", cellVal.getAddress().getRow() + 1,
                        cellVal.getAddress().getColumn() + 1, cellVal.getText()
                ));
            }
        }
    }
}
