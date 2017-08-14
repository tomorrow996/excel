package com.cd.tech.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 买家机构信息表
 */
public class Org {

    private Long id;
	private String name;            //机构名称
    private Long areaId;          //所属地域ID
    private int type;             //机构类型
    private String contact;         //联系人
    private String telephone;       //联系电话
    private Date createTime;       //创建时间

    public Org() {
	}

    public Org(String name, Long areaId, int type, String contact, String telephone, Date createTime) {
        this.name = name;
        this.areaId = areaId;
        this.type = type;
        this.contact = contact;
        this.telephone = telephone;
        this.createTime = createTime;
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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
