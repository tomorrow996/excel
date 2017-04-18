package service;

import service.impl.ExcelColHandler;

/**
 * Created by zc on 2017/4/10.
 */
public interface IExcelHeaders {
    ExcelColHandler getColHandler(String headerName);
}
