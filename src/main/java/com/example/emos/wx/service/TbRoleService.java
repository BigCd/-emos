package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_role(角色表)】的数据库操作Service
* @createDate 2023-03-23 19:00:59
*/
public interface TbRoleService extends IService<TbRole> {
    public ArrayList<HashMap> searchRoleOwnPermission(int id);

    public ArrayList<HashMap> searchAllPermission();

    public void insertRole(TbRole role);

    public void updateRolePermissions(TbRole role);
}
