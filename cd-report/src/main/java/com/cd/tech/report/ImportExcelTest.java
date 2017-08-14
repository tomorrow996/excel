package com.cd.tech.report;

import com.cd.tech.report.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cd.tech.report.service.ImportResult;
import com.cd.tech.report.service.excel.impl.UserExcelImporter;

import java.io.FileInputStream;

/**
 * Created by zc on 2017/4/11.
 */
public class ImportExcelTest {

    private static final Log log = LogFactory.getLog(ImportExcelTest.class);

    static ImportResult<User> readExcel(String fileName) throws Exception {
        FileInputStream excelFileInputStream = new FileInputStream(fileName);
        UserExcelImporter userExcelImporter = new UserExcelImporter();
        return userExcelImporter.importExcel(excelFileInputStream);
    }

    public static void main(String[] args) {
        try {
            ImportResult<User> userImportResult = readExcel("D:/user.xlsx");
            log.info(userImportResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
