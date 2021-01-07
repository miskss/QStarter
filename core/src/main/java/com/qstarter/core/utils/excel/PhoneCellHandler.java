package com.qstarter.core.utils.excel;

import com.qstarter.core.constant.CommonArgumentsValidConstant;
import org.dhatim.fastexcel.reader.Cell;

import java.util.regex.Pattern;

/**
 * @author peter
 * date 2020/7/30 14:28
 */
public class PhoneCellHandler extends StringCellHandler {

    @Override
    public String readCell(Cell cell) {
        String phone = super.readCell(cell);
        if (!Pattern.compile(CommonArgumentsValidConstant.PHONE_REGX).matcher(phone).matches()) {
            throw new CellValueParseException(String.format("第%d行%d列手机号【%s】格式不正确", cell.getAddress().getRow() + 1, cell.getAddress().getColumn() + 1, phone));
        }
        return phone;
    }
}
