package com.cd.tech.report.model.vo;

public class SysAreaVo extends BaseVo {
    /**
     * 地域名称
     */
    private String name;

    /**
     * 地域级别：1-省/直辖市；2-区/地级市
     */
    private Byte level;

    /**
     * 父地域ID，省/直辖市的父地域ID为0
     */
    private Long parentId;

    /**
     * 1-正常；2-禁用
     */
    private Byte status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}