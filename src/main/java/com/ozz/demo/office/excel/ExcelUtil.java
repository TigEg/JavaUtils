package com.ozz.demo.office.excel;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ozz.demo.date.DateFormatUtil;
import com.ozz.demo.text.NumberFormatUtil;

public class ExcelUtil {
  private DateFormatUtil dateFormatUtil;
  private NumberFormatUtil numberFormatUtil;

  public Workbook open(File file) throws IOException {
    try(FileInputStream input = new FileInputStream(file);) {
      return open(input);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public Workbook open(InputStream input) {
    try {
      Workbook workbook = WorkbookFactory.create(input);
      return workbook;
    } catch (InvalidFormatException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void write(Workbook workbook, File file) {
    try (OutputStream out = new FileOutputStream(file)) {
      workbook.write(out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, Integer> getCellValueIndexMap(Sheet sheet) {
    return getCellValueIndexMap(sheet, 0);
  }

  public Map<String, Integer> getCellValueIndexMap(Sheet sheet, int rowIndex) {
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

  public String getCellStringValue(Row row, String colEnIndex) {
    return getCellStringValue(row, numberFormatUtil.parseEnglish(colEnIndex) - 1);
  }

  public String getCellStringValue(Row row, int colIndex) {
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
            result = dateFormatUtil.formatDate(date);
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

  public Date getCellDateValue(Row row, int colIndex) {
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

  public Cell setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
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

  private String getCellInfo(Cell cell) {
    return "sheet: " + cell.getSheet().getSheetName()
           + ", 行: "
           + (cell.getRowIndex() + 1)
           + ", 列:"
           + numberFormatUtil.formatEnglish(cell.getColumnIndex() + 1)
           + ", 值:"
           + cell.getNumericCellValue();
  }

  public void removeRow(Sheet sheet, int rowIndex) {
    int lastRowNum = sheet.getLastRowNum();
    sheet.removeRow(sheet.getRow(rowIndex));
    if (lastRowNum == sheet.getLastRowNum() && rowIndex < sheet.getLastRowNum()) {
      sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
    }
  }

  public void removeRepeat(Sheet sheet, int colIndex) {
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

  public Workbook create() {
    return new XSSFWorkbook();
  }
}
