package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;
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




}




