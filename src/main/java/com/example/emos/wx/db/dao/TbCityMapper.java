package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbCity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【tb_city(疫情城市列表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:58
* @Entity com.example.emos.wx.db.dao.pojo.TbCity
*/
public interface TbCityMapper extends BaseMapper<TbCity> {
    public String searchCode(String city);
}




