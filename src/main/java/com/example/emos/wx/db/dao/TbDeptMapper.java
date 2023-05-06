package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_dept】的数据库操作Mapper
* @createDate 2023-03-23 19:00:58
* @Entity com.example.emos.wx.db.dao.pojo.TbDept
*/
public interface TbDeptMapper extends BaseMapper<TbDept> {
    public ArrayList<HashMap> searchDeptMembers(String keyword);

    public List<TbDept> searchAllDept();

    public int insertDept(String deptName);

    public int deleteDeptById(int id);

    public int updateDeptById(TbDept entity);

}




