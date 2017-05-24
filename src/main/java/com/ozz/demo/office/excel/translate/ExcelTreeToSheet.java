package com.ozz.demo.office.excel.translate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ozz.demo.office.excel.ExcelCellStyleUtil;
import com.ozz.demo.office.excel.ExcelUtil;
import com.ozz.demo.office.excel.format.ExcelFormat;

/**
 * @author ouzezh
 * 
 * @version $Rev$ $Date$
 */
public class ExcelTreeToSheet {
  private ExcelUtil excelUtil;
  private ExcelFormat excelFormat;
  private ExcelCellStyleUtil excelCellStyleUtil;

  // private Logger logger = Logger.getLogger(getClass());

  public <T> Sheet transferTreeToSheet(Workbook wb, String sheetName, List<T> treeList, List<String> props, String childProp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    // 标题样式
    List<CellStyle> headStyles = new ArrayList<CellStyle>();
    CellStyle headStyle = wb.createCellStyle();
    excelCellStyleUtil.setBorder(headStyle);
    excelCellStyleUtil.setFillForegroundColor(headStyle, IndexedColors.GREY_25_PERCENT.getIndex());
    headStyles.add(headStyle);
    headStyle = wb.createCellStyle();
    excelCellStyleUtil.setBorder(headStyle);
    excelCellStyleUtil.setFillForegroundColor(headStyle, IndexedColors.GREEN.getIndex());
    headStyles.add(headStyle);

    // 正文样式
    CellStyle bodyStyle = wb.createCellStyle();
    excelCellStyleUtil.setBorder(bodyStyle);

    // 创建Excel
    Sheet sheet = createSheet(wb, sheetName, treeList, props, childProp, headStyles, bodyStyle);
    return sheet;
  }

  private <T> Sheet createSheet(Workbook workbook, String sheetName, List<T> treeList, List<String> props, String childProp, List<CellStyle> headStyles,
      CellStyle bodyStyle) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Sheet sheet = workbook.createSheet(sheetName);

    sheet.createRow(0);
    for (T root : treeList) {
      createClassSheet(sheet, headStyles, bodyStyle, root, props, childProp, 1, true);
    }

    excelFormat.setBodyStyle(sheet, bodyStyle, 1);
    excelFormat.mergeRegion(sheet, 1);
    excelCellStyleUtil.createFreezePane(sheet, 0, 1);
    return sheet;
  }

  @SuppressWarnings("unchecked")
  private <T> void createClassSheet(Sheet sheet, List<CellStyle> headStyles, CellStyle bodyStyle, T model, List<String> props, String childProp,
      int level, boolean createNewRw) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    createClassMess(sheet, headStyles, bodyStyle, model, props, childProp, level, createNewRw);

    if (StringUtils.isEmpty(childProp)) {
      return;
    }

    Object value = getProp(model, childProp);
    List<T> children;
    if (value.getClass().isArray()) {
      children = (List<T>) Arrays.asList(value);
    } else if (value instanceof List) {
      children = (List<T>) value;
    } else {
      throw new RuntimeException("不支持的孩子类型(" + childProp + "): " + value.getClass().getName());
    }
    if (children != null && children.size() > 0) {
      for (int i = 0; i < children.size(); i++) {
        createClassSheet(sheet, headStyles, bodyStyle, children.get(i), props, childProp, level + 1, i > 0);
      }
    }
  }

  private <T> void createClassMess(Sheet sheet, List<CellStyle> headStyles, CellStyle bodyStyle, T model, List<String> props, String childProp,
      int level, boolean createNewRw) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Row row;
    if (createNewRw) {
      row = sheet.createRow(sheet.getLastRowNum() + 1);
    } else {
      row = sheet.getRow(sheet.getLastRowNum());
    }

    int model_class = props.size();
    int startIndex = (level - 1) * model_class;

    Object value;
    String strValue;
    for (int i = 0; i < props.size(); i++) {
      value = "get" + childProp.substring(0, 1).toUpperCase() + childProp.replaceFirst("^\\w", "");
      excelUtil.setCellValue(sheet, 0, startIndex + i, props.get(i));
      value = getProp(model, props.get(i));
      if (value == null) {
        strValue = null;
      } else if (value instanceof String) {
        strValue = (String) value;
      } else {
        strValue = value.toString();
      }
      row.createCell(startIndex + i).setCellValue(strValue);
    }

    CellStyle headStyle = headStyles.get((startIndex / model_class) % headStyles.size());
    for (int i = startIndex; i < startIndex + model_class; i++) {
      sheet.getRow(0).getCell(i).setCellStyle(headStyle);
    }
  }

  private <T> Object getProp(T model, String propName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Method method = model.getClass().getDeclaredMethod("get" + propName.substring(0, 1).toUpperCase() + propName.replaceFirst("^\\w", ""));
    return method.invoke(model);
  }

}
