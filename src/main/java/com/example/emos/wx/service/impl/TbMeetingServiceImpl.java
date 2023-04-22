package com.example.emos.wx.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.dao.TbUserMapper;
import com.example.emos.wx.db.pojo.TbMeeting;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbMeetingService;
import com.example.emos.wx.db.dao.TbMeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    @Resource
    private TbUserMapper tbUserMapper;

    @Value("${emos.code}")
    private String code;

    @Value("${workflow.url}")
    private String workflow;

    @Value("${emos.recieveNotify}")
    private String recieveNotify;

    @Override
    public void insertMeeting(TbMeeting entity) {
        //保存数据
        int row = tbMeetingMapper.insertMeeting(entity);
        if (row != 1) {
            throw new EmosException("会议添加失败");
        }
        // 开启审批工作流
        startMeetingWorkflow(entity.getUuid(), entity.getCreatorId().intValue(), entity.getDate(), entity.getStart());

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

    @Override
    public HashMap searchMeetingById(int id) {
        HashMap map = tbMeetingMapper.searchMeetingById(id);
        ArrayList<HashMap> list = tbMeetingMapper.searchMeetingMembers(id);
        map.put("members", list);
        return map;
    }

    @Override
    public void updateMeetingInfo(HashMap param) {
        int id = (int)param.get("id");
        String date = param.get("date").toString();
        String start = param.get("start").toString();
        String instanceId = param.get("instanceId").toString();

        //查询修改前的会议记录
        HashMap oldMeeting = tbMeetingMapper.searchMeetingById(id);
        String uuid = oldMeeting.get("uuid").toString();
        Integer creatorId = Integer.parseInt(oldMeeting.get("creatorId").toString());

        int row = tbMeetingMapper.updateMeetingInfo(param);//更新会议记录
        if(row!=1){
            throw new EmosException("会议更新失败");
        }

        //会议更新成功之后，删除以前的工作流
        JSONObject json = new JSONObject();
        json.set("instanceId",instanceId);
        json.set("reason","会议被修改");
        json.set("uuid",uuid);
        json.set("code",code);
        String url = workflow+"/workflow/deleteProcessById";
        HttpResponse resp = HttpRequest.post(url).header("content-type", "application/json").body(json.toString()).execute();
        if(resp.getStatus()!=200){
            log.error("删除工作流失败");
            throw new EmosException("删除工作流失败");
        }

        //创建新的工作流
        startMeetingWorkflow(uuid,creatorId,date,start);
    }

    public void startMeetingWorkflow(String uuid,int creatorId,String date,String start){
        HashMap info = tbUserMapper.searchUserInfo(creatorId);

        JSONObject json = new JSONObject();
        json.set("url",recieveNotify);
        json.set("uuid",uuid);
        json.set("openId",info.get("openId"));
        json.set("code",code);
        json.set("date",date);
        json.set("start",start);
        String[] roles = info.get("roles").toString().split(",");
        //如果不是总经理创建的会议
        if(!ArrayUtil.contains(roles,"总经理")){
            //查询总经理ID和同部门的经理的ID
            Integer managerId = tbUserMapper.searchDeptManagerId(creatorId);
            json.set("managerId",managerId);//部门经理ID
            Integer gmId = tbUserMapper.searchGmId();//总经理ID
            json.set("gmId",gmId);
            //查询会议员工是不是同一个部门
            boolean bool = tbMeetingMapper.searchMeetingMembersInSameDept(uuid);
            json.set("sameDept",bool);

        }
        String url = workflow+"/workflow/startMeetingProcess";
        //请求工作流接口，开启工作流
        HttpResponse response = HttpRequest.post(url).header("Content-Type", "application/json").body(json.toString()).execute();
        if(response.getStatus()==200){
            json = JSONUtil.parseObj(response.body());
            //如果工作流创建成功，就会更新会议状态
            String instanceId = json.getStr("instanceId");
            HashMap param = new HashMap();
            param.put("uuid",uuid);
            param.put("instanceId",instanceId);
            int row = tbMeetingMapper.updateMeetingInstanceId(param);//在会议记录中保存工作流实例的ID
            if(row != 1){
                throw new EmosException("保存会议工作流实例ID失败");
            }
        }
    }
}




