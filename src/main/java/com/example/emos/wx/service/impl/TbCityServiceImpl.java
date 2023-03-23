package com.example.emos.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.TbCity;
import com.example.emos.wx.service.TbCityService;
import com.example.emos.wx.db.dao.TbCityMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【tb_city(疫情城市列表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbCityServiceImpl extends ServiceImpl<TbCityMapper, TbCity>
    implements TbCityService{

}




