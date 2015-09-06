package com.ozz.utils.office.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ozz.utils.date.DateFormatUtil;
import com.ozz.utils.text.NumberFormatUtil;

public class ExcelUtil {

  public static Workbook open(File file) {
    FileInputStream input = null;
    try {
      input = new FileInputStream(file);
      return open(input);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  public static Workbook open(InputStream input) {
    try {
      Workbook workbook = WorkbookFactory.create(input);
      return workbook;
    } catch (InvalidFormatException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void write(Workbook workbook, File file) {
    OutputStream out = null;
    try {
      out = new FileOutputStream(file);
      workbook.write(out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(out);
    }
  }

  public static Map<String, Integer> getCellValueIndexMap(Sheet sheet) {
    return getCellValueIndexMap(sheet, 0);
  }

  public static Map<String, Integer> getCellValueIndexMap(Sheet sheet, int rowIndex) {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    Row row = sheet.getRow(rowIndex);
    if (row == null) {
      return map;
    }

    for (int i = 0; i <= row.getLastCellNum(); i++) {
      String name = getCellStringValue(row, i);
      if (StringUtils.isEmpty(name)) {
        continue;
      }
      map.put(name, i);
    }
    return map;
  }

  public static String getCellStringValue(Row row, String colEnIndex) {
    return getCellStringValue(row, NumberFormatUtil.parseEnglish(colEnIndex) - 1);
  }

  public static String getCellStringValue(Row row, int colIndex) {
    if (row == null) {
      return null;
    }
    Cell cell = row.getCell(colIndex);
    if (cell == null) {
      return null;
    }

    String result = null;
    try {
      switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:// 数字类型
          if (DateUtil.isCellDateFormatted(cell)) {// 时间
            Date date = cell.getDateCellValue();
            result = DateFormatUtil.formatDate(date);
          } else {
            return String.valueOf(cell.getNumericCellValue()).replaceFirst("\\.0+", "");
          }
          break;
        case Cell.CELL_TYPE_STRING:// String类型
          result = cell.getStringCellValue();
          break;
        case Cell.CELL_TYPE_BLANK:
          result = "";
        default:
          result = "";
          break;
      }
    } catch (Exception e) {
      throw new RuntimeException("读取Excel错误, " + getCellInfo(cell), e);
    }
    if (result != null) {
      result = result.trim();
    }
    return result;
  }

  public static Date getCellDateValue(Row row, int colIndex) {
    if (row == null) {
      return null;
    }
    Cell cell = row.getCell(colIndex);
    if (cell == null) {
      return null;
    }

    try {
      return cell.getDateCellValue();
    } catch (Exception e) {
      throw new RuntimeException("读取Excel错误, " + getCellInfo(cell), e);
    }
  }

  public static Cell setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
    if (sheet.getRow(rowIndex) == null) {
      sheet.createRow(rowIndex);
    }
    if (sheet.getRow(rowIndex).getCell(colIndex) == null) {
      sheet.getRow(rowIndex).createCell(colIndex);
    }

    Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
    cell.setCellValue(value);
    return cell;
  }

  private static String getCellInfo(Cell cell) {
    return "sheet: " + cell.getSheet().getSheetName()
           + ", 行: "
           + (cell.getRowIndex() + 1)
           + ", 列:"
           + NumberFormatUtil.formatEnglish(cell.getColumnIndex() + 1)
           + ", 值:"
           + cell.getNumericCellValue();
  }

  public static void removeRow(Sheet sheet, int rowIndex) {
    int lastRowNum = sheet.getLastRowNum();
    sheet.removeRow(sheet.getRow(rowIndex));
    if (lastRowNum == sheet.getLastRowNum() && rowIndex < sheet.getLastRowNum()) {
      sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
    }
  }

  public static void removeRepeat(Sheet sheet, int colIndex) {
    Map<String, String> map = new HashMap<String, String>();
    String value;
    long timestamp = System.currentTimeMillis();
    for (int rowIndex = sheet.getLastRowNum(); rowIndex >= 0; rowIndex--) {
      value = getCellStringValue(sheet.getRow(rowIndex), colIndex);
      if (StringUtils.isEmpty(value)) {
        continue;
      }

      if (System.currentTimeMillis() - timestamp > 3000) {
        System.out.println("check repeat row " + rowIndex);
        timestamp = System.currentTimeMillis();
      }
      if (map.containsKey(value)) {
        removeRow(sheet, rowIndex);
      } else {
        map.put(value, value);
      }
    }
  }

  public static Workbook create() {
    return new XSSFWorkbook();
  }

}
