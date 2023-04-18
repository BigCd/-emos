package com.example.emos.wx.service.impl;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbMeeting;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbMeetingService;
import com.example.emos.wx.db.dao.TbMeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

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

    /**
     * 分页查找会议记录
     * @param param
     * @return
     */
    @Override
    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param) {
        ArrayList<HashMap> list = tbMeetingMapper.searchMyMeetingListByPage(param);
        String date = null;
        ArrayList resultList = new ArrayList();
        HashMap resultMap = null;
        JSONArray array = null;
        for (HashMap map : list) {
            String temp = map.get("date").toString();
            if (!temp.equals(date)) {
                date = temp;
                resultMap = new HashMap();
                resultList.add(resultMap);
                resultMap.put("date", date);
                array = new JSONArray();
                resultMap.put("list", array);
            }
            array.put(map);

        }

        return resultList;
    }
}




