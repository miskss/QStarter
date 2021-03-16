package com.qstarter.core.utils;

import com.qstarter.core.utils.excel.ExcelCellValueHandler;
import com.qstarter.core.utils.excel.ExcelColumn;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter
 * date: 2019-12-24 10:30
 **/
@Slf4j
public final class ExcelUtils {
    private ExcelUtils() {
    }

    /**
     * @param response  响应流
     * @param excelName 表格名称
     * @param data      数据列表
     * @param clazz     数据的Class
     * @param <T>       数据泛型
     * @return {@link Workbook}
     * @throws IOException            excel对象创建时
     * @throws IllegalAccessException 对象数据读取错误时
     */
    public static <T> Workbook exportExcel(HttpServletResponse response, String excelName, Collection<T> data, Class<T> clazz) throws IOException, IllegalAccessException {
        try (OutputStream os = response.getOutputStream()) {
            setResponse(response, excelName);
            return exportExcel(os, excelName, data, clazz);
        }
    }

    public static <T> Workbook exportExcel(HttpServletResponse response, String excelName, SheetDataHandler handler, T data) throws IOException {
        try (OutputStream os = response.getOutputStream()) {
            setResponse(response, excelName);
            return exportExcel(os, excelName, handler, data);
        }
    }

    public static <T> Workbook exportExcel(OutputStream os, String excelName, SheetDataHandler handler, T data) throws IOException {
        try {
            Workbook wb = new Workbook(os, excelName, "1.0");
            Worksheet ws = wb.newWorksheet("Sheet 1");
            handler.handle(ws, data);
            wb.finish();
            return wb;
        } finally {
            os.close();
        }
    }

    public static <T> Workbook exportExcel(OutputStream os, String excelName, Collection<T> data, Class<T> clazz) throws IOException, IllegalAccessException {
        try {
            Workbook wb = new Workbook(os, excelName, "1.0");
            Worksheet ws = wb.newWorksheet("Sheet 1");
            Field[] declaredFields = getAllFields(clazz);
            List<ExcelColumn> collect = Arrays.stream(declaredFields).map(field -> field.getAnnotation(ExcelColumn.class))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(ExcelColumn::columnIdx))
                    .collect(Collectors.toList());

            if (collect.isEmpty()) throw new IncompleteAnnotationException(ExcelColumn.class, clazz.getSimpleName());
            int rowIdx = 0;
            //表头行
            for (int i = 0; i < collect.size(); i++) {
                if (i == 0) {
                    ws.value(rowIdx, i, "序号");
                }
                ExcelColumn excelColumn = collect.get(i);
                ws.value(rowIdx, excelColumn.columnIdx(), excelColumn.value());
            }
            //从第二行开始
            rowIdx++;
            for (T datum : data) {
                ws.value(rowIdx, 0, rowIdx);
                final int currentRow = rowIdx;
                for (Field field : declaredFields) {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation == null) continue;
                    int columnIdx = annotation.columnIdx();
                    boolean flag = field.isAccessible();
                    if (!flag) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(datum);

                    Class<? extends ExcelCellValueHandler> handler = annotation.handler();

                    if (!Objects.equals(handler.getSimpleName(), ExcelCellValueHandler.class.getSimpleName())) {
                        ExcelCellValueHandler valueHandler = null;
                        try {
                            valueHandler = handler.newInstance();

                        } catch (InstantiationException e) {
                            log.error("获取单元格值处理{}实例失败", handler.getSimpleName());
                            log.error(e.getMessage(), e);
                        }
                        if (valueHandler != null) {
                            ws.value(currentRow, columnIdx, valueHandler.writeCell(value));
                        } else {
                            ws.value(currentRow, columnIdx, value);
                        }
                    } else {
                        ws.value(currentRow, columnIdx, value);
                    }
                    field.setAccessible(flag);
                }
                //跳转到下一行数据
                rowIdx++;
            }
            wb.finish();
            return wb;
        } finally {
            os.close();
        }
    }

    @FunctionalInterface
    public interface SheetDataHandler<T> {
        /**
         * 处理表格数据（将数据写入表格）
         * ex：
         * ws.value(0, 0, "This is a string in A1");
         * ws.value(0, 1, 1123);
         * ws.value(0, 2, 1234);
         * ws.value(0, 3, 123456L);
         * ws.value(0, 4, 1.234);
         *
         * @param ws   表格sheet
         * @param data 实际数据
         */
        void handle(Worksheet ws, T data);
    }

    private static void setResponse(HttpServletResponse response, String excelName) {
        response.setHeader("Content-disposition", "attachment;filename="
                + new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + ".xlsx");
        response.setCharacterEncoding("utf-8");
        // 由于导出格式是excel的文件，设置导出文件的响应头部信息
        response.setContentType("application/vnd.ms-excel");
    }

    private static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[0]);
    }

    private static List<Field> getAllFieldsList(final Class<?> cls) {
        Assert.isTrue(cls != null, "The class must not be null");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
}
