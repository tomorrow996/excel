package com.cd.tech.report.service.excel.impl;

/**
 * Created by zc on 2017/4/10.
 */
public abstract class ExcelColHandler<T, V> {

    private String headerName;

    public ExcelColHandler(String headerName){
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public abstract void handleValue( T colValue, V bean, int lineNumber);
}
