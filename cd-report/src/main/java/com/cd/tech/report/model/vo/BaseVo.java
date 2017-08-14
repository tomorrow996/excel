package com.cd.tech.report.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zc on 2017/8/9.
 */
public class BaseVo implements java.io.Serializable {

    /**
     * default serializable id.
     */
    private static final long serialVersionUID = -1L;
    @JSONField(serialize = false)
    protected Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BaseVo) || ((BaseVo) obj).getId() == null) return false;
        return ((BaseVo) obj).getId().equals(this.id);
    }
}
