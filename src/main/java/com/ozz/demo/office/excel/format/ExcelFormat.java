package com.ozz.demo.office.excel.format;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.ozz.demo.office.excel.ExcelCellStyleUtil;
import com.ozz.demo.office.excel.ExcelUtil;

/**
 * 
 * 
 * @version $Rev$ $Date$
 */
public class ExcelFormat {
  private ExcelCellStyleUtil excelCellStyleUtil;
  private ExcelUtil excelUtil;

  public void setBodyStyle(Sheet sheet, CellStyle bodyStyle, int startRow) {
    int lastRow = -1;
    int lastCol = -1;

    Row row;
    for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
      row = sheet.getRow(i);
      for (int j = row.getLastCellNum(); j >= 0; j--) {
        if (StringUtils.isNotEmpty(excelUtil.getCellStringValue(row, j))) {
          if (lastCol < j) {
            lastCol = j;
          }
          if (lastRow < i) {
            lastRow = i;
          }
          break;
        }
      }
    }

    for (int i = startRow; i <= lastRow; i++) {
      row = sheet.getRow(i);
      for (int j = lastCol; j >= 0; j--) {
        if (row.getCell(j) == null) {
          row.createCell(j);
        }
        row.getCell(j).setCellStyle(bodyStyle);
      }
    }

  }

  public void mergeRegion(Sheet sheet, int startIndex) {
    Row row;
    String value;
    int endIndex;
    for (int i = startIndex; i <= sheet.getLastRowNum(); i++) {
      row = sheet.getRow(i);
      for (int j = 0; j <= row.getLastCellNum(); j++) {
        value = excelUtil.getCellStringValue(row, j);

        // 处理Cell，判断是否有Cell可以与它合并
        if (StringUtils.isNotEmpty(value)) {
          endIndex = -1;
          for (int c = i + 1; c <= sheet.getLastRowNum(); c++) {
            for (int jj = 0; jj <= j; jj++) {
              if (StringUtils.isNotEmpty(excelUtil.getCellStringValue(sheet.getRow(c), jj))) {
                endIndex = c - 1;
              }
            }
            if (endIndex >= 0) {
              break;
            }
          }
          if (endIndex < 0) {
            endIndex = sheet.getLastRowNum();
          }

          if (endIndex > i) {
            excelCellStyleUtil.addMergedRegion(sheet, i, endIndex, j, j);
            excelCellStyleUtil.setAlignment(sheet.getRow(i).getCell(j).getCellStyle(), HorizontalAlignment.LEFT, VerticalAlignment.TOP);
          }
        }

      }
    }
  }

}
