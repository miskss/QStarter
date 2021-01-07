package com.qstarter.core.utils.excel;

import com.qstarter.core.utils.TimeUtils;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * excel 读取和导出的工具类
 *
 * @author peter
 * date 2020/7/30 10:04
 */
public enum ExcelUtils {
    ;

    public static void main(String[] args) {
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(TimeUtils.convert2Instant(LocalDateTime.now().plusDays(1)).toEpochMilli());
        System.out.println(TimeUtils.convert2Instant(LocalDateTime.now().plusDays(2)).toEpochMilli());
        System.out.println(TimeUtils.convert2Instant(LocalDateTime.now().plusDays(30)).toEpochMilli());
    }

    /**
     * 读excel
     */
    public final static class Reader {

        /**
         * 读取第一个sheet的数据
         *
         * @param file   excel
         * @param filter 行过滤器
         * @param clazz  读取的数据存储类类对象
         * @param <T>    数据存储类
         * @return 数据集合
         */
        public static <T> Collection<T> readerFirstSheet(MultipartFile file, Predicate<Row> filter, Class<T> clazz) throws CellValueParseException {
            return reader(file, filter, clazz, 0);
        }

        /**
         * 读取excel 的sheet
         *
         * @param file       excel
         * @param filter     行过滤器
         * @param clazz      读取的数据存储类类对象
         * @param sheetIndex sheet 的序号（从0开始）
         * @param <T>        数据存储类
         * @return 数据集合
         * @throws CellValueParseException 数据解析异常
         */
        public static <T> Collection<T> reader(MultipartFile file, Predicate<Row> filter, Class<T> clazz, int sheetIndex) throws CellValueParseException {
            List<Field> list = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(ExcelColumn.class)).collect(Collectors.toList());
            if (list.isEmpty()) {
                throw new IncompleteAnnotationException(ExcelColumn.class, clazz.getSimpleName());
            }
            try (InputStream is = file.getInputStream(); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                Optional<Sheet> wbSheet = wb.getSheet(sheetIndex);
                if (!wbSheet.isPresent()) return Collections.emptyList();
                Sheet sheet = wbSheet.get();
                try (Stream<Row> rows = sheet.openStream()) {
                    return rows
                            .filter(filter == null ? o -> true : filter)
                            .map(r -> {
                                if (r.getPhysicalCellCount() == 0) return null;
                                T obj;
                                try {
                                    obj = clazz.newInstance();
                                } catch (InstantiationException | IllegalAccessException e) {
                                    throw new CellValueParseException(String.format("获取%s实例失败", clazz.getName()), e);
                                }
                                for (Field field : list) {
                                    readCell(r, obj, field);
                                }
                                return obj;
                            }).filter(Objects::nonNull).collect(Collectors.toList());
                } catch (IOException e) {
                    throw new CellValueParseException(e.getMessage(), e);
                }
            } catch (IOException e) {
                throw new CellValueParseException(e.getMessage(), e);
            }
        }
    }

    private static <T> void readCell(Row r, T obj, Field field) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
        int i = annotation.columnIdx();
        //获取值，并设置值
        try {
            ExcelCellValueHandler handler = annotation.handler().newInstance();
            field.set(obj, handler.readCell(r.getCell(i)));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CellValueParseException(String.format("第%d行%d列【%s】数据格式不正确", r.getRowNum(), i, r.getCell(i).getText()), e);
        }
        field.setAccessible(accessible);
    }

    public final static class Writer {

        public static <T> Workbook exportMultiSheetExcel(HttpServletResponse response, String excelName, Map<String, List<T>> data, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
            try (OutputStream os = response.getOutputStream()) {
                setResponse(response, excelName);
                return exportExcel(os, excelName, data, clazz);
            }
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
        public static <T> Workbook exportExcel(HttpServletResponse response, String excelName, Collection<T> data, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
            try (OutputStream os = response.getOutputStream()) {
                setResponse(response, excelName);
                return exportExcel(os, excelName, data, clazz);
            }
        }

        public static <T> Workbook exportExcel(OutputStream os, String excelName, Map<String, List<T>> map, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
            try {
                Workbook wb = new Workbook(os, excelName, "1.0");
                Field[] declaredFields = getAllFields(clazz);
                List<ExcelColumn> collect = Arrays.stream(declaredFields).map(field -> field.getAnnotation(ExcelColumn.class))
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(ExcelColumn::columnIdx))
                        .collect(Collectors.toList());
                if (collect.isEmpty())
                    throw new IncompleteAnnotationException(ExcelColumn.class, clazz.getSimpleName());
                for (Map.Entry<String, List<T>> entry : map.entrySet()) {
                    Collection<T> data = entry.getValue();
                    Worksheet ws = wb.newWorksheet(entry.getKey());
                    int rowIdx = 0;
                    //表头行
                    for (int i = 0; i < collect.size(); i++) {
                        ws.width(i, 10);
                        if (i == 0) {
                            ws.value(rowIdx, i, "序号");
                            ws.width(i, 5);
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

                            ExcelColumn annotation = getExcelColumn(field);
                            if (annotation == null) continue;
                            int columnIdx = annotation.columnIdx();
                            boolean flag = field.isAccessible();
                            if (!flag) {
                                field.setAccessible(true);
                            }
                            Object value = field.get(datum);

                            Class<? extends ExcelCellValueHandler> handler = annotation.handler();

                            if (!Objects.equals(handler.getSimpleName(), ExcelCellValueHandler.class.getSimpleName())) {
                                ExcelCellValueHandler valueHandler;
                                valueHandler = handler.newInstance();
                                ws.value(currentRow, columnIdx, valueHandler.writeCell(value));
                            } else {
                                ws.value(currentRow, columnIdx, value);
                            }
                            field.setAccessible(flag);
                        }
                        //跳转到下一行数据
                        rowIdx++;
                    }
                }
                wb.finish();
                return wb;
            } finally {
                os.close();
            }
        }

        private static ExcelColumn getExcelColumn(Field field) {
            return field.getAnnotation(ExcelColumn.class);
        }

        public static <T> Workbook exportExcel(OutputStream os, String excelName, Collection<T> data, Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
            try {
                Workbook wb = new Workbook(os, excelName, "1.0");
                Worksheet ws = wb.newWorksheet("Sheet 1");
                Field[] declaredFields = getAllFields(clazz);
                List<ExcelColumn> collect = Arrays.stream(declaredFields).map(field -> field.getAnnotation(ExcelColumn.class))
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(ExcelColumn::columnIdx))
                        .collect(Collectors.toList());

                if (collect.isEmpty())
                    throw new IncompleteAnnotationException(ExcelColumn.class, clazz.getSimpleName());
                int rowIdx = 0;
                //表头行
                for (int i = 0; i < collect.size(); i++) {
                    ws.width(i, 10);
                    if (i == 0) {
                        ws.value(rowIdx, i, "序号");
                        ws.width(i, 5);
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
                        ExcelColumn annotation = getExcelColumn(field);
                        if (annotation == null) continue;
                        int columnIdx = annotation.columnIdx();
                        boolean flag = field.isAccessible();
                        if (!flag) {
                            field.setAccessible(true);
                        }
                        Object value = field.get(datum);

                        Class<? extends ExcelCellValueHandler> handler = annotation.handler();

                        if (!Objects.equals(handler.getSimpleName(), ExcelCellValueHandler.class.getSimpleName())) {
                            ExcelCellValueHandler valueHandler;
                            valueHandler = handler.newInstance();
                            ws.value(currentRow, columnIdx, valueHandler.writeCell(value));
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

        public static void setResponse(HttpServletResponse response, String excelName) {
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
}
