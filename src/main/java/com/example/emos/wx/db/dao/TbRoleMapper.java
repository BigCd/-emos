package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_role(角色表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:59
* @Entity com.example.emos.wx.db.dao.pojo.TbRole
*/
public interface TbRoleMapper extends BaseMapper<TbRole> {
    public ArrayList<HashMap> searchRoleOwnPermission(int id);

    public ArrayList<HashMap> searchAllPermission();

    public int insertRole(TbRole role);

    public int updateRolePermissions(TbRole role);
}




