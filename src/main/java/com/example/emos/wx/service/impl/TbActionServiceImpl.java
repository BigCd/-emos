package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbAction;
import com.example.emos.wx.service.TbActionService;
import com.example.emos.wx.db.dao.TbActionMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【tb_action(行为表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbActionServiceImpl extends ServiceImpl<TbActionMapper, TbAction>
    implements TbActionService{

}



