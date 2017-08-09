package com.ozz.demo.office.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ozz.demo.date.DateFormatDemo;
import com.ozz.demo.text.NumberFormatUtil;

public class ExcelUtil {
  private DateFormatDemo dateFormatUtil;
  private NumberFormatUtil numberFormatUtil;

  public Workbook create() {
    return new XSSFWorkbook();
  }

  public Workbook open(Path path) throws IOException, EncryptedDocumentException, InvalidFormatException {
    try (InputStream input = Files.newInputStream(path)) {
      return open(input);
    }
  }

  public Workbook open(InputStream input) throws EncryptedDocumentException, InvalidFormatException, IOException {
    Workbook workbook = WorkbookFactory.create(input);
    return workbook;
  }

  public void write(Workbook workbook, Path path) throws FileNotFoundException, IOException {
    try (OutputStream out = Files.newOutputStream(path)) {
      workbook.write(out);
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
      if (cell.getCellTypeEnum() == CellType.NUMERIC) {// 数字类型
        if (DateUtil.isCellDateFormatted(cell)) {// 时间
          Date date = cell.getDateCellValue();
          result = dateFormatUtil.format(date, getDateFormatString(cell.getCellStyle().getDataFormatString()));
        } else {
          result = String.valueOf(cell.getNumericCellValue()).replaceFirst("\\.0+", "");
        }
      } else if (cell.getCellTypeEnum() == CellType.STRING) {// String类型
        result = cell.getStringCellValue();
      } else if (cell.getCellTypeEnum() == CellType.BLANK) {
        result = "";
      } else {
        result = "";
      }
    } catch (Exception e) {
      throw new RuntimeException("读取Excel错误, " + getCellInfo(cell), e);
    }
    if (result != null) {
      result = result.trim();
    }
    return result;
  }

  public String getDateFormatString(String formatString) {
    if ("reserved-0x1F".equals(formatString) || "m/d/yy".equals(formatString) || "m/d/yy".equals(formatString)) {
      return "yyyy-MM-dd";
    } else if ("m/d/yy h:mm".equals(formatString)) {
      return "yyyy-MM-dd h:mm";
    } else {
      throw new RuntimeException("解析excel错误:暂不支持此日期格式"+formatString);
    }
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

  public void loop(Workbook wb) {
    int sheetCount = wb.getNumberOfSheets();
    for (int i = 0; i < sheetCount; i++) {
      Sheet sheet = wb.getSheetAt(i);
      int rowCount = sheet.getPhysicalNumberOfRows();
      for (int j = 0; j < rowCount; j++) {
        Row row = sheet.getRow(j);
        int cellcount = row.getPhysicalNumberOfCells();
        for (int k = 0; k < cellcount; k++) {
          System.out.println("sheet:" + sheet.getSheetName() + ",row:" + (j + 1) + ",cell:" + numberFormatUtil.formatEnglish(k + 1)+" = " + getCellStringValue(row, k));
        }
      }
    }
  }
}
