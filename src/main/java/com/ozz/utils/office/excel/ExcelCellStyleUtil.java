package com.ozz.utils.office.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelCellStyleUtil {

  // IndexedColors.GREY_25_PERCENT.getIndex()
  public static void setFillForegroundColor(CellStyle cs, short color) {
    cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
    cs.setFillForegroundColor(color);
  }

  // CellStyle.VERTICAL_TOP, CellStyle.ALIGN_CENTER
  public static void setAlignment(CellStyle cs, short alignment, short verticalAlignment) {
    cs.setAlignment(alignment);// 水平
    cs.setVerticalAlignment(verticalAlignment);// 垂直
  }

  public static void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
    CellRangeAddress regin = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
    sheet.addMergedRegion(regin);
  }

  public static void setBorder(CellStyle cs) {
    cs.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
    cs.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
    cs.setBorderTop(CellStyle.BORDER_THIN);// 上边框
    cs.setBorderRight(CellStyle.BORDER_THIN);// 右边框
  }

  public static void createFreezePane(Sheet sheet, int i, int j) {
    sheet.createFreezePane(i, j);
  }

  public static void createFreezeTopRow(Sheet sheet) {
    sheet.createFreezePane(0, 1);
  }
}
