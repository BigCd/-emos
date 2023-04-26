package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbMeeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_meeting(会议表)】的数据库操作Mapper
* @createDate 2023-03-23 19:00:58
* @Entity com.example.emos.wx.db.dao.pojo.TbMeeting
*/
public interface TbMeetingMapper extends BaseMapper<TbMeeting> {
    public int insertMeeting(TbMeeting entity);

    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

    public boolean searchMeetingMembersInSameDept(String uuid);
    
    public int updateMeetingInstanceId(HashMap map);

    public HashMap searchMeetingById(int id);

    public ArrayList<HashMap> searchMeetingMembers(int id);

    public int updateMeetingInfo(HashMap param);

    public int deleteMeetingById(int id);

    public List<String> searchUserMeetingInMonth(HashMap param);

}




