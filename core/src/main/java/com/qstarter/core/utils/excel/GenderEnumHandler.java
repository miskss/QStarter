package com.qstarter.core.utils.excel;

import com.qstarter.core.enums.GenderEnum;
import org.apache.logging.log4j.util.Strings;
import org.dhatim.fastexcel.reader.Cell;

import java.util.Objects;

/**
 * Description:
 *
 * @author peter
 * CreateTime: 2020-11-07 16:17
 */
public class GenderEnumHandler implements ExcelCellValueHandler<GenderEnum> {
    @Override
    public String writeCell(Object value) {
        if (value == null) return "";
        return ((GenderEnum) value).getDescr();
    }

    @Override
    public GenderEnum readCell(Cell cell) {
        String text = cell.asString();

        if (Strings.isEmpty(text))
            throw new CellValueParseException(String.format("第%d行%d列性别不能为空", cell.getAddress().getRow() + 1, cell.getAddress().getColumn() + 1));


        for (GenderEnum value : GenderEnum.values()) {
            if (Objects.equals(value.getDescr(), text)) {
                return value;
            }
        }
        throw new CellValueParseException(String.format("第%d行%d列性别【%s】不正确", cell.getAddress().getRow() + 1, cell.getAddress().getColumn() + 1, text));
    }
}
