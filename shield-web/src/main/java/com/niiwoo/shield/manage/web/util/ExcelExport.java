package com.niiwoo.shield.manage.web.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/6/24.
 */
@Getter
@Setter
@Slf4j
public class ExcelExport {
    HttpServletResponse response;
    // 文件名
    private String fileName;
    //标题行名称列表
    private String[] titleNames;
    //字段名称列表
    private String[] columnNames;
    //字段类型列表
    private String[] columnTypes;

    //表头字体
    private String titleFontType = "Arial Unicode MS";
    //表头背景色
    private String titleBackColor = "C1FBEE";
    //表头字号
    private short titleFontSize = 12;
    //添加自动筛选的列 如 A:M
    private String address = "";
    //正文字体
    private String contentFontType = "Arial Unicode MS";
    //正文字号
    private short contentFontSize = 12;
    //Float类型数据小数位
    private String floatDecimal = ".00";
    //Double类型数据小数位
    private String doubleDecimal = ".00";
    //设置列的公式
    private String colFormula[] = null;

    DecimalFormat floatDecimalFormat = new DecimalFormat(floatDecimal);
    DecimalFormat doubleDecimalFormat = new DecimalFormat(doubleDecimal);

    private HSSFWorkbook workbook = null;

    public ExcelExport(HttpServletResponse response, String fileName, String[] titleNames,
                       String[] columnNames, String[] columnTypes) {
        this.response = response;
        this.fileName = fileName;
        this.titleNames = titleNames;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        workbook = new HSSFWorkbook();
    }

    /**
     * 写excel.
     *
     * @param sheetName 标签页名称
     * @param dataList  数据集合
     */
    public void wirteExcel(String sheetName, List<Map<String, Object>> dataList) {
        //添加添加标签页
        Sheet sheet = workbook.createSheet(sheetName);
        //写EXCEL标题行
        writeTitleRow(sheet);
        //写数据行
        writeDataRows(sheet, dataList);
    }

    /**
     * 写标题行
     *
     * @param sheet 标签页
     */
    private void writeTitleRow(Sheet sheet) {
        //创建标题行
        Row titleRow = sheet.createRow(0);
        //设置样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, titleFontSize);
        titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);

        for (int i = 0; i < titleNames.length; i++) {
            sheet.autoSizeColumn(i);
            Cell cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleNames[i]);
        }
    }

    /**
     * 写数据行
     *
     * @param sheet 标签页
     */
    private void writeDataRows(Sheet sheet, List<Map<String, Object>> dataList) {
        //有数据时
        if (dataList != null && dataList.size() > 0) {
            //设置样式
//            HSSFCellStyle titleStyle = workbook.createCellStyle();
//            titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, titleFontSize);
//            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);

            if (columnNames != null && columnNames.length > 0) {
                for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
                    Map<String, Object> dataMap = dataList.get(rowIndex - 1);     //获得数据对象
                    Row dataRow = sheet.createRow(rowIndex);

                    for (int i = 0; i < columnNames.length; i++) {
                        String columnName = columnNames[i].toString().trim();
                        //字段名不为空
                        if (StringUtils.isNotEmpty(columnName)) {
                            //字段值
                            Object columnValue = dataMap.get(columnName);
                            Cell cell = dataRow.createCell(i);
                            switch (columnTypes[i]) {
                                case "int":
                                    cell.setCellValue((Integer) columnValue);
                                    break;
                                case "long":
                                    cell.setCellValue((Long) columnValue);
                                    break;
                                case "float":
                                    cell.setCellValue(floatDecimalFormat.format(columnValue));
                                    break;
                                case "double":
                                    cell.setCellValue(doubleDecimalFormat.format(columnValue));
                                    break;
                                case "Date":
                                    cell.setCellValue(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format((Date) columnValue));
                                    break;
                                default:
                                    cell.setCellValue(columnValue == null ? null : String.valueOf(columnValue));
                                    break;
                            }

                        }
                    }
                }

            }
        }
    }

    //输出EXCEL文件流
    public void writeEnd() {
        OutputStream out = null;
        try {
            //设置响应头为EXCEL输出流
            out = response.getOutputStream();
            fileName = fileName + ".xls";
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes(), "ISO8859-1"));
            workbook.write(out);
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error("error", e);
            }
        }

    }

    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     *
     * @param style 保证style统一
     * @param color 颜色：66FFDD
     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    private CellStyle setColor(CellStyle style, String color, short index) {
        if (StringUtils.isNotEmpty(color)) {
            //转为RGB码
            int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制
            int g = Integer.parseInt((color.substring(2, 4)), 16);
            int b = Integer.parseInt((color.substring(4, 6)), 16);
            //自定义cell颜色
            HSSFPalette palette = workbook.getCustomPalette();
            palette.setColorAtIndex(index, (byte) r, (byte) g, (byte) b);

//            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(index);
        }
        return style;
    }

    /**
     * 设置字体并加外边框
     *
     * @param style 样式
     * @param style 字体名
     * @param style 大小
     * @return
     */
    private CellStyle setFontAndBorder(CellStyle style, String fontName, short size) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
        font.setBold(true);
        style.setFont(font);
//        style.setBorderBottom(CellStyle.BORDER_THIN); //下边框
//        style.setBorderLeft(CellStyle.BORDER_THIN);//左边框
//        style.setBorderTop(CellStyle.BORDER_THIN);//上边框
//        style.setBorderRight(CellStyle.BORDER_THIN);//右边框
        return style;
    }

}