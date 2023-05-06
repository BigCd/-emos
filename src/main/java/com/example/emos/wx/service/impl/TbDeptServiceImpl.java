package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.dao.TbUserMapper;
import com.example.emos.wx.db.pojo.TbDept;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbDeptService;
import com.example.emos.wx.db.dao.TbDeptMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_dept】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbDeptServiceImpl extends ServiceImpl<TbDeptMapper, TbDept>
    implements TbDeptService{

    @Resource
    private TbDeptMapper deptDao;

    @Resource
    private TbUserMapper userDao;

    @Override
    public List<TbDept> searchAllDept() {
        List<TbDept> list = deptDao.searchAllDept();
        return list;
    }

    @Override
    public int insertDept(String deptName) {
        int row = deptDao.insertDept(deptName);
        if(row != 1){
            throw new EmosException("部门添加失败");
        }
        return row;
    }

    @Override
    public void deleteDeptById(int id) {
        //查询部门是否有数据
        long count = userDao.searchUserCountInDept(id);
        if (count>0){
            throw new EmosException("部门中有员工,无法删除部门");
        }else{
            int row = deptDao.deleteDeptById(id);
            if (row != 1){
                throw new EmosException("部门删除失败");
            }
        }
    }

    @Override
    public void updateDeptById(TbDept entity) {
        int row = deptDao.updateDeptById(entity);
        if(row != 1){
            throw new EmosException("部门修改失败");
        }
    }
}




