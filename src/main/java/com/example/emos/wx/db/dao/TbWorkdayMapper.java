package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbWorkday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_workday】的数据库操作Mapper
* @createDate 2023-03-23 19:00:59
* @Entity com.example.emos.wx.db.dao.pojo.TbWorkday
*/
public interface TbWorkdayMapper extends BaseMapper<TbWorkday> {
    public Integer searchTodayIsWorkday();

    public ArrayList<String> searchWorkdayInRange(HashMap param);
}




