package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【tb_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:59
* @Entity com.example.emos.wx.db.dao.pojo.TbUser
*/
public interface TbUserMapper extends BaseMapper<TbUser> {
    public boolean haveRootUser();
    public int insert(HashMap param);

    public Integer searchIdByOpenId(String openId);

    public Set<String> searchUserPermissions(int userId);

    public TbUser searchById(int userId);

    public HashMap searchNameAndDept(int userId);

    public String searchUserHiredate(int userId);

    public HashMap searchUserSummary(int userId);

    public ArrayList<HashMap> searchUserGroupByDept(String keyword);

    public ArrayList<HashMap> searchMembers(List param);

    public HashMap searchUserInfo(int userId);
    public int searchDeptManagerId(int id);

    public int searchGmId();


}




