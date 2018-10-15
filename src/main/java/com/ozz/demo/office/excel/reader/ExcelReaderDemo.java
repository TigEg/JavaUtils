package com.ozz.demo.office.excel.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.ozz.demo.office.excel.reader.base.SimpleSheetContentsHandler;
import com.ozz.demo.office.excel.reader.base.SimpleXSSFSheetXMLHandler;

public class ExcelReaderDemo {
  public static void main(String[] args) throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
    List<Map<String, String>> data = new ExcelReaderDemo().parse(Paths.get("C:/Users/ouzezhou/Desktop/Temp/20170711/修改教师模板 (2).xlsx"));
    for(Map<String, String> row : data) {
      for(Entry<String, String> cell : row.entrySet()) {
        System.out.println(cell.getKey() + "=" +cell.getValue());
      }
    }
  }

  public List<Map<String, String>> parse(Path path) throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
    try (InputStream inStream = Files.newInputStream(path); OPCPackage pkg = OPCPackage.open(inStream);) {
      XSSFReader xssfReader = new XSSFReader(pkg);
      StylesTable styles = xssfReader.getStylesTable();
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg, false);

      try (InputStream sheetInputStream = xssfReader.getSheetsData().next();) {
        XMLReader sheetParser = SAXHelper.newXMLReader();
        SimpleSheetContentsHandler handler = new SimpleSheetContentsHandler();
        sheetParser.setContentHandler(new SimpleXSSFSheetXMLHandler(styles, strings, handler, false));
        sheetParser.parse(new InputSource(sheetInputStream));
        return handler.getData();
      }
    }
  }

}
