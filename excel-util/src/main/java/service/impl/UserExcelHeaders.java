package service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exception.ServiceException;
import model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import service.IExcelHeaders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zc on 2017/4/11.
 */
public class UserExcelHeaders implements IExcelHeaders {

    private static Map<String, ExcelColHandler> excelHeaderMap = Maps.newHashMap();

    private static List<ExcelColHandler> headers = Lists.newArrayList();

    private static void nonNullCheck(Object sourceValue, int lineNumber, ExcelColHandler excelColHandler) {
        if (sourceValue == null || StringUtils.isBlank(sourceValue.toString()))
            throw new ServiceException("第" + lineNumber + "行:" + excelColHandler.getHeaderName() + " 不能为空！");
    }

    private static void numberCheck(String sourceValue, int lineNumber, ExcelColHandler excelColHandler) {
        if (!NumberUtils.isNumber(sourceValue))
            throw new ServiceException("第" + lineNumber + "行:" + excelColHandler.getHeaderName() + " 输入有误！请输入数字");
    }

    static {
        headers.add(new ExcelColHandler<String, User>("用户名") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setName(sourceValue.trim());
            }
        });

        headers.add(new ExcelColHandler<String, User>("工资") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                numberCheck(sourceValue, lineNumber, this);
                user.setSalary(new BigDecimal(sourceValue.trim()));
            }
        });

        headers.add(new ExcelColHandler<String, User>("邮箱") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                user.setEmail(sourceValue);
            }
        });

        for (ExcelColHandler header : headers) {
            excelHeaderMap.put(header.getHeaderName(), header);
        }
    }

    public ExcelColHandler getColHandler(String headerName) {
        return excelHeaderMap.get(headerName);
    }

}
