package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbHolidays;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【tb_holidays(节假日表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:58
* @Entity com.example.emos.wx.db.dao.pojo.TbHolidays
*/
public interface TbHolidaysMapper extends BaseMapper<TbHolidays> {

    public Integer searchTodayIsHolidays();



}




