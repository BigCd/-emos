package com.example.emos.wx.service;

import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.db.pojo.TbCheckin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_checkin(签到表)】的数据库操作Service
* @createDate 2023-03-23 19:00:58
*/
public interface TbCheckinService extends IService<TbCheckin> {
    public String validCanCheckIn(int userId, String date);

    public void checkin(HashMap param);


    public void checkin(CheckinForm form ,int userId,String path);

    public void createFaceModel(int userId, String path);

    public HashMap searchTodayCheckin(int userId);

    public long searchCheckinDays(int userId);

    public ArrayList<HashMap> searchWeekCheckin(HashMap param);
}
