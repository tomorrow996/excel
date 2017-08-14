package com.cd.tech.report.dao;

import java.util.List;

/**
 * Created by zc on 2017/4/24.
 */
public interface ByrConfigMapper {
    void insertConfig(Long orgId);

    void batchInsert(List<Object> orgIdList);
}
