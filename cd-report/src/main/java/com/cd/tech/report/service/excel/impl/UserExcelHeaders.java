package com.cd.tech.report.service.excel.impl;

import com.cd.tech.report.constant.OrgTypeEnum;
import com.cd.tech.report.util.encoder.PwdEncoder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.cd.tech.report.exception.ServiceException;
import com.cd.tech.report.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.cd.tech.report.service.IExcelHeaders;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zc on 2017/4/11.
 */
public class UserExcelHeaders implements IExcelHeaders<User> {

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

    /**
     * 手机号正则表达式
     */
    public static final String REGEX_MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";

    private static void checkMobile(String sourceValue, int lineNumber, ExcelColHandler excelColHandler) {
        if ( !Pattern.compile(REGEX_MOBILE).matcher(sourceValue.toString().trim()).matches()) {
            System.out.println(sourceValue);
            throw new ServiceException("第" + lineNumber + "行:" + excelColHandler.getHeaderName() + " 输入有误！请输入正确的手机号");
        }
    }

    static {

        headers.add(new ExcelColHandler<String, User>("客户名称") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setOrgName(sourceValue.trim());
            }
        });

        headers.add(new ExcelColHandler<String, User>("联系人") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setRealName(sourceValue.trim());
            }
        });

        headers.add(new ExcelColHandler<String, User>("手机号") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
//                checkMobile(sourceValue, lineNumber, this);
//                user.setMobile(sourceValue.trim());
                checkMobile(String.valueOf(Double.valueOf(sourceValue.trim()).longValue()), lineNumber, this);
                user.setMobile(String.valueOf(Double.valueOf(sourceValue.trim()).longValue()));
            }
        });

        headers.add(new ExcelColHandler<String, User>("省") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setCityName(sourceValue.trim());
            }
        });

        headers.add(new ExcelColHandler<String, User>("市") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setCityName(sourceValue.trim());
            }
        });

        headers.add(new ExcelColHandler<String, User>("机构类型") {
            @Override
            public void handleValue(String sourceValue, User user, int lineNumber) {
                nonNullCheck(sourceValue, lineNumber, this);
                user.setOrgType(OrgTypeEnum.getValue(sourceValue.trim()));
            }
        });

        for (ExcelColHandler header : headers) {
            excelHeaderMap.put(header.getHeaderName(), header);
        }
    }

    public void afterProperty(int LineNum, User user) {
        user.setType(6);
        user.setIsApproved(1);
        if (StringUtils.isNotEmpty(user.getMobile())) {
            user.setPassword(PwdEncoder.getMd5Encoder().encodePassword(user.getMobile().substring(5)));
        }
        if (StringUtils.isNotEmpty(user.getOrgName()) && StringUtils.isNotEmpty(user.getRealName())) {
            int seperateIndex = user.getOrgName().length() - user.getRealName().length();
            if (seperateIndex < 0) return;
            if (user.getOrgName().substring(seperateIndex).equals(user.getRealName())) {
                user.setOrgName(user.getOrgName().substring(0, seperateIndex));
            }
        }
    }

    public ExcelColHandler getColHandler(String headerName) {
        return excelHeaderMap.get(headerName);
    }


}
