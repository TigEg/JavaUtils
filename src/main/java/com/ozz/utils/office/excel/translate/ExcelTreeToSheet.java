package com.ozz.utils.office.excel.translate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ozz.utils.office.excel.ExcelCellStyleUtil;
import com.ozz.utils.office.excel.ExcelUtil;
import com.ozz.utils.office.excel.format.ExcelFormat;

/**
 * @author ouzezh
 * 
 * @version $Rev$ $Date$
 */
public class ExcelTreeToSheet {

    // private static Logger logger = Logger.getLogger(TreeToSheet.class);

    public static <T> Sheet transferTreeToSheet(Workbook wb, String sheetName, List<T> treeList, List<String> props, String childProp) {
        // 标题样式
        List<CellStyle> headStyles = new ArrayList<CellStyle>();
        CellStyle headStyle = wb.createCellStyle();
        ExcelCellStyleUtil.setBorder(headStyle);
        ExcelCellStyleUtil.setFillForegroundColor(headStyle, IndexedColors.GREY_25_PERCENT.getIndex());
        headStyles.add(headStyle);
        headStyle = wb.createCellStyle();
        ExcelCellStyleUtil.setBorder(headStyle);
        ExcelCellStyleUtil.setFillForegroundColor(headStyle, IndexedColors.GREEN.getIndex());
        headStyles.add(headStyle);

        // 正文样式
        CellStyle bodyStyle = wb.createCellStyle();
        ExcelCellStyleUtil.setBorder(bodyStyle);

        // 创建Excel
        Sheet sheet = createSheet(wb, sheetName, treeList, props, childProp, headStyles, bodyStyle);
        return sheet;
    }

    private static <T> Sheet createSheet(Workbook workbook, String sheetName, List<T> treeList, List<String> props, String childProp, List<CellStyle> headStyles, CellStyle bodyStyle) {
        Sheet sheet = workbook.createSheet(sheetName);

        sheet.createRow(0);
        for (T root : treeList) {
            createClassSheet(sheet, headStyles, bodyStyle, root, props, childProp, 1, true);
        }

        ExcelFormat.setBodyStyle(sheet, bodyStyle, 1);
        ExcelFormat.mergeRegion(sheet, 1);
        ExcelCellStyleUtil.createFreezePane(sheet, 0, 1);
        return sheet;
    }

    @SuppressWarnings("unchecked")
    private static <T> void createClassSheet(Sheet sheet, List<CellStyle> headStyles, CellStyle bodyStyle, T model, List<String> props, String childProp, int level, boolean createNewRw) {
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

    private static <T> void createClassMess(Sheet sheet, List<CellStyle> headStyles, CellStyle bodyStyle, T model, List<String> props, String childProp, int level, boolean createNewRw) {
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
            ExcelUtil.setCellValue(sheet, 0, startIndex + i, props.get(i));
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

    private static <T> Object getProp(T model, String propName) {
        Object value;
        try {
            Method method = model.getClass().getDeclaredMethod("get" + propName.substring(0, 1).toUpperCase() + propName.replaceFirst("^\\w", ""));
            value = method.invoke(model);
            return value;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
