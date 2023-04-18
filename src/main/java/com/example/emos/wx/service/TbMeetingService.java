package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbMeeting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_meeting(会议表)】的数据库操作Service
* @createDate 2023-03-23 19:00:59
*/
public interface TbMeetingService extends IService<TbMeeting> {
        public void insertMeeting(TbMeeting entity);

        public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

}
