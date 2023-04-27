package com.example.emos.wx.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbRole;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbRoleService;
import com.example.emos.wx.db.dao.TbRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
* @author Administrator
* @description 针对表【tb_role(角色表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:59
*/
@Service
public class TbRoleServiceImpl extends ServiceImpl<TbRoleMapper, TbRole>
    implements TbRoleService{

    @Resource
    private TbRoleMapper tbRoleMapper;


    @Override
    public ArrayList<HashMap> searchRoleOwnPermission(int id) {
        ArrayList<HashMap> list =  tbRoleMapper.searchRoleOwnPermission(id);
        list = handleDate(list);
         return list;
    }

    @Override
    public ArrayList<HashMap> searchAllPermission() {
        ArrayList<HashMap> list = tbRoleMapper.searchAllPermission();
        list = handleDate(list);
        return list;
    }

    @Override
    public void insertRole(TbRole role) {
        int row = tbRoleMapper.insertRole(role);
        if(row != 1){
            throw  new EmosException("添加角色失败");
        }

    }

    @Override
    public void updateRolePermissions(TbRole role) {
        int row = tbRoleMapper.updateRolePermissions(role);
        if(row != 1){
            throw  new EmosException("修改角色失败");
        }
    }

    /**
     * 将查询结果按照模块名称分组
     * @param list
     * @return
     */
    private ArrayList<HashMap> handleDate(ArrayList<HashMap> list) {
        ArrayList permsList = new ArrayList();
        ArrayList actionList = new ArrayList();
        HashSet set = new HashSet();
        HashMap data = new HashMap();

        for(HashMap map : list){
            long permissionId = (Long)map.get("id");
            String moduleName = (String)map.get("actionName");
            String actionName = (String)map.get("actionName");
            String selected = map.get("selected").toString();

            if(set.contains(moduleName)){
                JSONObject json = new JSONObject();
                json.set("id",permissionId);
                json.set("actionName",actionName);
                json.set("selected",selected.equals("1")?true:false);
                actionList.add(json);
            }else {
                set.add(moduleName);
                data = new HashMap();
                data.put("moduleName",moduleName);
                actionList = new ArrayList();
                JSONObject json = new JSONObject();
                json.set("id",permissionId);
                json.set("actionName",actionName);
                json.set("selected",selected.equals(1)?true:false);
                actionList.add(json);
                data.put("action",actionList);
                permsList.add(data);
            }

        }
        return permsList;
    }
}




