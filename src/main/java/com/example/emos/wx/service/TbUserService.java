package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
* @author Administrator
* @description 针对表【tb_user(用户表)】的数据库操作Service
* @createDate 2023-03-23 19:00:59
*/
public interface TbUserService extends IService<TbUser> {
    public int registerUser(String registerCode,String code,String nickname,String photo);

    public Set<String> searchUserPermissions(int userId);

    public Integer login(String code);

    public TbUser searchById(int userId);

    public String searchUserHiredate(int userId);

}
