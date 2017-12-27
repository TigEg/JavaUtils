package com.ozz.demo.office.excel.reader.base;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;

import com.ozz.demo.office.excel.ExcelUtil;

/**
 * modify from org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
 * 
 * poi-ooxml 3.17
 */
public class SimpleXSSFSheetXMLHandler extends XSSFSheetXMLHandler {

  public SimpleXSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings,
      SheetContentsHandler sheetContentsHandler, boolean formulasNotResults) {
    super(styles, strings, sheetContentsHandler, formulasNotResults);
  }

  @Override
  String parseNmuber(StringBuffer value, DataFormatter formatter, short formatIndex, String formatString) {
    if("General".equals(formatString)) {
      return value.toString();
    } else {
      formatString = new ExcelUtil().getDateFormatString(formatString);
      return super.parseNmuber(value, formatter, formatIndex, formatString);
    }
  }
}
