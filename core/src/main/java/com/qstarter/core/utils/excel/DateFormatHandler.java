package com.qstarter.core.utils.excel;


import com.google.common.base.Strings;
import com.qstarter.core.constant.CommonArgumentsValidConstant;
import com.qstarter.core.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author peter
 * date: 2020-01-08 10:01
 **/
@Slf4j
public class DateFormatHandler implements ExcelCellValueHandler<LocalDate> {

    @Override
    public String writeCell(Object value) {
        if (Objects.isNull(value)) return "";
        if (value instanceof LocalDateTime) {
            return TimeUtils.formatDateTime2String((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return TimeUtils.formatLocalDate((LocalDate) value);
        } else if (value instanceof Instant) {
            LocalDateTime time = TimeUtils.convert2LocalDateTime(((Instant) value));
            return TimeUtils.formatDateTime2String(time);
        } else if (value instanceof Date) {
            LocalDateTime time = TimeUtils.convert2LocalDateTime((Date) value);
            return TimeUtils.formatDateTime2String(time);
        }
        return value.toString();
    }

    @Override
    public LocalDate readCell(Cell cell) throws CellValueParseException {
        String cellVal = cell.getText();
        if (Strings.isNullOrEmpty(cellVal))
            throw new CellValueParseException(String.format("第%d行%d列日期【%s】数据格式不正确", cell.getAddress().getRow() + 1, cell.getAddress().getColumn() + 1, cellVal));
        try {
            if (cellVal.contains("-")) {
                return TimeUtils.parseLocalDate(cellVal);
            } else if (cellVal.contains("/")) {
                return TimeUtils.parseLocalDate(cellVal, "yyyy/MM/dd");
            } else if (cellVal.contains(".")) {
                return TimeUtils.parseLocalDate(cellVal, "yyyy.MM.dd");
            } else if (cellVal.contains("年")) {
                return TimeUtils.parseLocalDate(cellVal, "yyyy年MM月dd日");
            } else if (Pattern.matches(CommonArgumentsValidConstant.ALL_NUMBER_REGX, cellVal) && cellVal.length() == 8) {
                return TimeUtils.parseLocalDate(cellVal, "yyyyMMdd");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new CellValueParseException(String.format("第%d行%d列日期【%s】格式不正确", cell.getAddress().getRow() + 1, cell.getAddress().getColumn() + 1, cellVal));
    }

    public static void main(String[] args) {
        System.out.println(Pattern.matches("^[0-9]*$", "202001-00"));
    }
}
