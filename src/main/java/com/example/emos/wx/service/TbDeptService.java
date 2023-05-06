package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_dept】的数据库操作Service
* @createDate 2023-03-23 19:00:58
*/
public interface TbDeptService extends IService<TbDept> {
        public List<TbDept> searchAllDept();
        public int insertDept(String deptName);
        public void deleteDeptById(int id);
        public void updateDeptById(TbDept entity);



}
