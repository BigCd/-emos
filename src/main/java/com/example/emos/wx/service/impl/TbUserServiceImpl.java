package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.service.TbUserService;
import com.example.emos.wx.db.dao.TbUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【tb_user(用户表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:59
*/
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser>
    implements TbUserService{

}




