import model.User;
import service.ImportResult;
import service.impl.UserExcelImporter;

import java.io.FileInputStream;

/**
 * Created by zc on 2017/4/11.
 */
public class ImportExcel {
    static ImportResult<User> readExcel(String fileName) throws Exception {
        FileInputStream excelFileInputStream = new FileInputStream(fileName);
        UserExcelImporter userExcelImporter = new UserExcelImporter();
        return userExcelImporter.importExcel(excelFileInputStream);
    }

    public static void main(String[] args) {
        try {
            ImportResult<User> userImportResult = readExcel("D:/user.xlsx");
            System.out.println(userImportResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
