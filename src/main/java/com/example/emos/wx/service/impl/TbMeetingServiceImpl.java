package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbMeeting;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbMeetingService;
import com.example.emos.wx.db.dao.TbMeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【tb_meeting(会议表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbMeetingServiceImpl extends ServiceImpl<TbMeetingMapper, TbMeeting>
    implements TbMeetingService{
    @Resource
    private TbMeetingMapper tbMeetingMapper;
    @Override
    public void insertMeeting(TbMeeting entity) {
        //保存数据
        int row = tbMeetingMapper.insertMeeting(entity);
        if (row != 1) {
            throw new EmosException("会议添加失败");
        }
        //TODO 开启审批工作流

    }
}




