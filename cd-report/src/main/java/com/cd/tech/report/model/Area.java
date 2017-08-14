package com.cd.tech.report.model;

/**
 * 地域信息表
 * Created by zc on 2016/8/16.
 */
public class Area {

	private static final long serialVersionUID = 4162025116414570228L;

    private Long id;
	private String name;
    private Byte level;           //地域级别：1-省/直辖市；2-区/地级市
    private Long parentId;       //父地域ID
    private Byte status;          //地域状态

    private String cityName;

    public Area() {
    }

    public Area(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Byte getLevel() {
        return level;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
