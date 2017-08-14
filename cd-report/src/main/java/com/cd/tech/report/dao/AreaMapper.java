package com.cd.tech.report.dao;

import com.cd.tech.report.model.Area;

import java.util.List;

/**
 * Created by zc on 2017/4/20.
 */
public interface AreaMapper {
    Long getId(String name);
    List<Area> getProvincesAndCitys();
}
