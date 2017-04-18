package service.impl;

import com.google.common.collect.Lists;
import exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import service.IExcelHeaders;
import service.ImportResult;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zc on 2017/4/10.
 */
public abstract class AbstractExcelImporter<T> {

    public static Log log = LogFactory.getLog(AbstractExcelImporter.class);

    abstract IExcelHeaders getHeaderHandlers();

    abstract T newImportBean();

    public ImportResult<T> importExcel(MultipartFile file) throws IOException {
        return importExcel(file.getInputStream());
    }

    public ImportResult<T> importExcel(InputStream is) throws IOException {
        return getImportResult(getSheet(is));
    }

    private List<String> getExcelFileHeader(Sheet sheet) {
        Row row = sheet.getRow(sheet.getFirstRowNum());
        List<String> header = Lists.newArrayList();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null || StringUtil.isBlank(cell.getStringCellValue())) continue;
            header.add(cell.getStringCellValue());
        }
        return header;
    }

    private Sheet getSheet(InputStream is) throws IOException {
        try {
            Workbook workbook = getWorkBook(is);
            if (workbook == null) throw new ServiceException("请上传【.xls】或者【.xlsx】文件。");
            return workbook.getSheetAt(0);
        } finally {
            is.close();
        }
    }

    private ImportResult<T> getImportResult(Sheet sheet) {
        List<String> header = getExcelFileHeader(sheet);
        ImportResult<T> importResult = new ImportResult();
        StringBuilder errorMsg = new StringBuilder();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            if (isEmptyRow(sheet.getRow(i))) continue;
            if (importResult.getErrorCount() > 5) break;
            T bean = newImportBean();
            if (isImportThisRowError(sheet, header, errorMsg, i, bean)) {
                importResult.incrErrorCount(1);
            } else {
                importResult.getImportBeanList().add(bean);
                importResult.incrSuccCount(1);
            }
        }
        importResult.setErrorMsg(errorMsg.toString());
        return importResult;
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) return true;
        for (int j = 0; j < row.getLastCellNum(); j++) {
            String value = getStringValue(row.getCell(j), row.getRowNum(), j);
            if (StringUtils.isNotEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    private boolean isImportThisRowError(Sheet sheet, List<String> header, StringBuilder errorMsg, int lineNumber, T bean) {
        boolean thisRowError = false;
        IExcelHeaders headerHandlers = getHeaderHandlers();
        for (int colNum = 0; colNum < header.size(); colNum++) {
            try {
                ExcelColHandler colHandler = headerHandlers.getColHandler(header.get(colNum).trim());
                if (colHandler == null)
                    throw new ServiceException("请确保数据无误。不存在的表头【" + header.get(colNum) + "】");
                if (colNum >= sheet.getRow(lineNumber).getLastCellNum()) {
                    colHandler.handleValue("", bean, lineNumber);
                } else {
                    Cell cell = sheet.getRow(lineNumber).getCell(colNum);
                    colHandler.handleValue(getStringValue(cell, lineNumber, colNum), bean, lineNumber);
                }
            } catch (ServiceException e) {
                errorMsg.append(e.getMessage()).append("<br/>");
                thisRowError = true;
            }
        }
        return thisRowError;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String getStringValue(Cell cell, int rowNum, int colNum) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    try {
                        return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                    } catch (Exception e) {
                        throw new ServiceException("第 " + rowNum + " 行，第 " + colNum + " 列日期格式不正确，请输入【2016-01-01】格式日期");
                    }
                }
                return String.valueOf(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }

    public static Workbook getWorkBook( InputStream is) {
        try {
            return WorkbookFactory.create(is);
        } catch (IOException e) {
            log.error(e);
        } catch (InvalidFormatException e) {
            log.error(e);
        }
        return null;
    }

}
