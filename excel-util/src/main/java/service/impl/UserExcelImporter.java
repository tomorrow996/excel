package service.impl;

import model.User;
import service.IExcelHeaders;

/**
 * Created by zc on 2017/4/11.
 */
public class UserExcelImporter extends AbstractExcelImporter<User>{

    IExcelHeaders getHeaderHandlers() {
        return new UserExcelHeaders();
    }

    User newImportBean() {
        return new User();
    }
}
