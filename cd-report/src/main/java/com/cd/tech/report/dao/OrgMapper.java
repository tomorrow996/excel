package com.cd.tech.report.dao;

import com.cd.tech.report.model.Org;

import java.util.List;
import java.util.Map;

/**
 * Created by zc on 2017/4/20.
 */
public interface OrgMapper {
    void insertOrg(Org org);

    Org getOrg(Map paramMap);

    void batchInser(List<Org> orgList);
}
