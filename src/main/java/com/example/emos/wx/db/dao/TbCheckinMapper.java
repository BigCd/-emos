package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbCheckin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_checkin(签到表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:58
* @Entity com.example.emos.wx.db.dao.pojo.TbCheckin
*/
public interface TbCheckinMapper extends BaseMapper<TbCheckin> {
    public Integer haveCheckin(HashMap param);

    public void insertFaceModelCheck(TbCheckin entity);


}




