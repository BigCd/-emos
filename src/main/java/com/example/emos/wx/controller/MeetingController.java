package com.example.emos.wx.controller;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.SearchMyMeetingListByPageForm;
import com.example.emos.wx.service.TbMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/meeting")
@Api("会议模块网络接口")
public class MeetingController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TbMeetingService tbMeetingService;

    @PostMapping("/searchMyMeetingListByPage")
    @ApiOperation("查询会议列表分页数据")
    public R searchMyMeetingListByPage(@Valid @RequestBody SearchMyMeetingListByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length = form.getLength();
        long start = (page - 1) * length;
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("start", start);
        map.put("length", length);
        ArrayList list = tbMeetingService.searchMyMeetingListByPage(map);
        return R.ok().put("result", list);
    }
}
