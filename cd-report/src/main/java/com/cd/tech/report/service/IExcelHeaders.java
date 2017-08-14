package com.cd.tech.report.service;

import com.cd.tech.report.service.excel.impl.ExcelColHandler;

/**
 * Created by zc on 2017/4/10.
 */
public interface IExcelHeaders<T> {
    ExcelColHandler getColHandler(String headerName);
    void afterProperty(int LineNum, T bean);
}
