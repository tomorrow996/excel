package com.cd.tech.report.constant;

/**
 * Created by zc on 2017/4/20.
 */
public enum OrgTypeEnum {
    /**
     * 机构类型
     */
    RESEARCH(1, "科研机构"), DISTRIBUTOR(2, "经销商"), TERMINAL(3, "终端企业"), OTHER(4, "其它");

    public final int value;
    public final String description;

    OrgTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public static int getValue(String description) {
        for (OrgTypeEnum c : OrgTypeEnum.values()) {
            if (c.getDescription().equals(description)) {
                return c.value;
            }
        }
        return OrgTypeEnum.RESEARCH.getValue();
    }

    public String getDescription() {
        return description;
    }
}
