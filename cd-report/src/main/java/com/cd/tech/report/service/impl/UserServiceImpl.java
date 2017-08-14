package com.cd.tech.report.service.impl;

import com.cd.tech.report.dao.AreaMapper;
import com.cd.tech.report.dao.ByrConfigMapper;
import com.cd.tech.report.dao.OrgMapper;
import com.cd.tech.report.dao.UserMapper;
import com.cd.tech.report.model.Area;
import com.cd.tech.report.model.Org;
import com.cd.tech.report.model.User;
import com.cd.tech.report.service.ImportResult;
import com.cd.tech.report.service.UserService;
import com.cd.tech.report.service.excel.impl.UserExcelImporter;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zc on 2017/4/20.
 */
@Service("userservice")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    OrgMapper orgMapper;

    @Autowired
    ByrConfigMapper configMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String handleFile(MultipartFile file) throws IOException {

        StringBuilder result = new StringBuilder(" <br/> 城市信息错误: <br/>");
        StringBuilder registered = new StringBuilder("<br/> 已经注册用户: <br/>");

        ImportResult<User> userImportResult = new UserExcelImporter().importExcel(file);
        if (userImportResult.getErrorCount() > 5) {
            return userImportResult.getErrorMsg();
        }
        List<User> importBeanList = userImportResult.getImportBeanList();
        //batch insert
        writeIntoDataBase(result, importBeanList, registered);
        if (userImportResult.getErrorCount() > 0) {
            return userImportResult.getErrorMsg();
        }
        return result.toString() + registered.toString();
    }

    private void writeIntoDataBase(StringBuilder result, List<User> importBeanList, StringBuilder registered) {
        Map<String, Long> areaMap = getAllAreaMap();
        Map<String, Long> orgMap = Maps.newHashMap();
        //待入库数据
        List<Object> orgIdList = Lists.newArrayList();
        List<User> userList = Lists.newArrayList();
        List<Org> orgList = Lists.newArrayList();

        for (User user : importBeanList) {
            //TODO area 设置缓存
            Long areaId = areaMap.get(user.getProvinceName() + user.getCityName());
            if (areaId == null) {
                result.append(user.getOrgName() + ":" + user.getMobile() + ": 此用户城市信息错误" + "<br/>");
                return;
            }
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("name", user.getOrgName());
            paramMap.put("areaId", areaId.toString());
            Org org = orgMapper.getOrg(paramMap);
            if (org == null) {
                org = new Org(user.getOrgName(), areaId, user.getOrgType(), user.getRealName(), user.getMobile(), new Timestamp(new Date().getTime()));
                orgList.add(org);
            } else {
                orgMap.put(user.getOrgName(), org.getId());
            }

            User userData = userMapper.getUser(user.getMobile());
            if (userData == null) {
                userList.add(user);
            } else {
                registered.append(user.getOrgName() + ":" + user.getMobile() + "此用户已经注册过了" + "<br/>");
            }
        }
        orgMapper.batchInser(orgList);
        for (Org org : orgList) {
            orgIdList.add(org.getId());
        }
        for (User user : userList) {
            user.setOrgId(orgMap.get(user.getOrgName()));
        }
        userMapper.batchInsert(userList);
        configMapper.batchInsert(orgIdList);
    }

    private Map<String, Long> getAllAreaMap() {
        List<Area> areaVoList = areaMapper.getProvincesAndCitys();
        Map<String, Long> map = Maps.newHashMap();
        for(Area vo : MoreObjects.firstNonNull(areaVoList, Lists.<Area>newArrayList())){
            map.put(vo.getName()+vo.getCityName(), vo.getId());
        }
        return map;
    }
}
