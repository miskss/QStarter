package com.qstarter.core.utils.excel;

import org.dhatim.fastexcel.reader.Cell;
import org.springframework.util.StringUtils;

/**
 * Description:
 *
 * @author peter
 * CreateTime: 2020-11-14 21:48
 */
public class NotBlankHandler extends StringCellHandler {
    @Override
    public String readCell(Cell cellVal) {
        String text = super.readCell(cellVal);
        if (!StringUtils.hasText(text)) {
            throw new CellValueParseException(String.format("第%d行%d列单元格的值不能为空", cellVal.getAddress().getRow() + 1, cellVal.getAddress().getColumn() + 1));
        }
        return text;
    }
}
