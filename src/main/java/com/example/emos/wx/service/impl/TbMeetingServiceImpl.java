package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.pojo.TbMeeting;
import com.example.emos.wx.service.TbMeetingService;
import com.example.emos.wx.db.daoper.TbMeetingMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【tb_meeting(会议表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbMeetingServiceImpl extends ServiceImpl<TbMeetingMapper, TbMeeting>
    implements TbMeetingService{

}




