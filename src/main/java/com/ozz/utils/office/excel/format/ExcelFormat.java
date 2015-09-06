package com.ozz.utils.office.excel.format;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.ozz.utils.office.excel.ExcelCellStyleUtil;
import com.ozz.utils.office.excel.ExcelUtil;

/**
 * 
 * 
 * @version $Rev$ $Date$
 */
public class ExcelFormat {

  public static void setBodyStyle(Sheet sheet, CellStyle bodyStyle, int startRow) {
    int lastRow = -1;
    int lastCol = -1;

    Row row;
    for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
      row = sheet.getRow(i);
      for (int j = row.getLastCellNum(); j >= 0; j--) {
        if (StringUtils.isNotEmpty(ExcelUtil.getCellStringValue(row, j))) {
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

  public static void mergeRegion(Sheet sheet, int startIndex) {
    Row row;
    String value;
    int endIndex;
    for (int i = startIndex; i <= sheet.getLastRowNum(); i++) {
      row = sheet.getRow(i);
      for (int j = 0; j <= row.getLastCellNum(); j++) {
        value = ExcelUtil.getCellStringValue(row, j);

        // 处理Cell，判断是否有Cell可以与它合并
        if (StringUtils.isNotEmpty(value)) {
          endIndex = -1;
          for (int c = i + 1; c <= sheet.getLastRowNum(); c++) {
            for (int jj = 0; jj <= j; jj++) {
              if (StringUtils.isNotEmpty(ExcelUtil.getCellStringValue(sheet.getRow(c), jj))) {
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
            ExcelCellStyleUtil.addMergedRegion(sheet, i, endIndex, j, j);
            ExcelCellStyleUtil.setAlignment(sheet.getRow(i).getCell(j).getCellStyle(), CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_TOP);
          }
        }

      }
    }
  }

}
