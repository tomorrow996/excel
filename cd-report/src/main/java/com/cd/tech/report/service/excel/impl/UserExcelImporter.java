package com.cd.tech.report.service.excel.impl;

import com.cd.tech.report.model.User;
import com.cd.tech.report.service.IExcelHeaders;

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
