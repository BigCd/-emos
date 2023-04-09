package com.example.emos.wx.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.db.dao.*;
import com.example.emos.wx.db.pojo.TbCheckin;
import com.example.emos.wx.db.pojo.TbHolidays;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbCheckinService;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
* @author Administrator
* @description 针对表【tb_checkin(签到表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:58
*/
@Service
public class TbCheckinServiceImpl extends ServiceImpl<TbCheckinMapper, TbCheckin>
    implements TbCheckinService{
    @Autowired
    private SystemConstants systemConstants;
    @Resource
    private TbHolidaysMapper  tbHolidaysMapper;
    @Resource
    private TbWorkdayMapper tbWorkdayMapper;
    @Resource
    private TbCheckinMapper tbCheckinMapper;

    @Resource
    private TbFaceModelMapper faceModelDao;

    @Value("${emos.face.checkinUrl}")
    private String checkinUrl;

    private TbCityMapper tbCityMapper;

    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean bool_1 = tbHolidaysMapper.searchTodayIsHolidays()!=null ?true :false;
        boolean bool_2 = tbWorkdayMapper.searchTodayIsWorkday()!=null ?true :false;
        String type = "工作日";
        if(DateUtil.date().isWeekend()){
            type="节假日";
        }
        if(bool_1){
            type ="节假日";
        } else if (bool_2) {
            type = "工作日";
        }

        if(type.equals("节假日")){
            return "节假日不需要考勤";
        }else{
            DateTime now = DateUtil.date();
            String start = DateUtil.today()+" "+systemConstants.attendanceStartTime;
            String end = DateUtil.today()+" "+systemConstants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);
            if(now.isBefore(attendanceStart)){
                return "没有到上班考勤开始时间";
            } else if (now.isAfter(attendanceEnd)) {
                return "超过了上班考勤结束时间";
            }else {
                HashMap map = new HashMap();
                map.put("userId",userId);
                map.put("date",date);
                map.put("start",start);
                map.put("end",end);
                boolean bool = tbCheckinMapper.haveCheckin(map)!=null ? true : false;
                return bool ? "今日已经考勤，不用重复考勤":"可以考勤";
            }

        }


    }

    @Override
    public void checkin(CheckinForm form ,int userId,String path) {
        //判断签到
        Date d1 = DateUtil.date();//当前时间
        Date d2 = DateUtil.parse(DateUtil.today()+" "+ systemConstants.attendanceTime);
        Date d3 = DateUtil.parse(DateUtil.today()+" "+ systemConstants.attendanceEndTime);
        int status = 1;
        if(d1.compareTo(d2)<=0){
            status = 1;//正常签到
        } else if (d1.compareTo(d2)>0 && d1.compareTo(d3)<0) {
            status = 2;//迟到
        }
        //查询签到人的人脸模型数据

        String faceModel = faceModelDao.searchFaceModel(userId);

        if(faceModel == null){
            throw new EmosException("不存在人脸模型");
        }else{
            HttpRequest request = HttpUtil.createPost(checkinUrl);
            request.form("photo", FileUtil.file(path),"targetModel","faceModel");
            HttpResponse response = request.execute();
            if(response.getStatus()!=200){
                log.error("人脸识别服务异常");
                throw new EmosException("人脸识别服务异常");
            }
            String body = response.body();
            if("无法识别出人脸".equals(body)||"照片中存在多张人脸".equals(body)){
                throw new EmosException(body);
            }else if("False".equals(body)){
                throw new EmosException("签到无效，非本人签到");
            } else if ("True".equals(body)) {
                int risk = 1;
                String city= form.getCity();
                String district=  form.getDistrict();
                String address= form.getAddress();
                String country= form.getCountry();
                String province= form.getProvince();
                //查询城市简称
                if(form.getCity()!=null && form.getCity().length()>0 && form.getDistrict().length() > 0){
                    String code = tbCityMapper.searchCode(form.getCity());
                    //查询地区风险
                    try{
                        String url = "http://m." + code + ".bendibao.com/news/yqdengji/?qu=" + form.getDistrict();
                        Document document = Jsoup.connect(url).get();
                        Elements elements = document.getElementsByClass(" list-detail");
                        for (Element one:elements){
                            String result = one.text().split(" ")[1];
                            if ("高风险".equals(result)) {
                                risk = 3;
                                //发送告警邮件
                            } else if ("中风险".equals(result)) {
                                risk = risk < 2 ? 2 : risk;
                            }
                        }

                    } catch (IOException e) {
                        log.error("执行异常",e);
                        throw new EmosException("获取风险等级失败");
                    }
                }
                //保存签到记录
                TbCheckin entity=new TbCheckin();
                entity.setUserId(userId);
                entity.setAddress(address);
                entity.setCountry(country);
                entity.setProvince(province);
                entity.setCity(city);
                entity.setDistrict(district);
                entity.setStatus((Integer) status);
                entity.setRisk(risk);
                //String转Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(DateUtil.today());
                    entity.setDate(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                entity.setCreateTime(d1);
                tbCheckinMapper.insert(entity);
            }
        }
    }

    public void checkinFist(HashMap param) {
        //判断签到
        Date d1 = DateUtil.date();//当前时间
        Date d2 = DateUtil.parse(DateUtil.today()+" "+ systemConstants.attendanceTime);
        Date d3 = DateUtil.parse(DateUtil.today()+" "+ systemConstants.attendanceEndTime);
        int status = 1;
        if(d1.compareTo(d2)<=0){
            status = 1;//正常签到
        } else if (d1.compareTo(d2)>0 && d1.compareTo(d3)<0) {
            status = 2;//迟到
        }
        //查询签到人的人脸模型数据
        int userId = (Integer) param.get("userId");

        String faceModel = faceModelDao.searchFaceModel(userId);

        if(faceModel == null){
            throw new EmosException("不存在人脸模型");
        }else{
            String path = (String) param.get("path");
            HttpRequest request = HttpUtil.createPost(checkinUrl);
            request.form("photo", FileUtil.file(path),"targetModel","faceModel");
            HttpResponse response = request.execute();
            if(response.getStatus()!=200){
                log.error("人脸识别服务异常");
                throw new EmosException("人脸识别服务异常");
            }
            String body = response.body();
            if("无法识别出人脸".equals(body)||"照片中存在多张人脸".equals(body)){
                throw new EmosException(body);
            }else if("False".equals(body)){
                throw new EmosException("签到无效，非本人签到");
            } else if ("True".equals(body)) {
                //TODO 这里要获取签到地区新冠疫情风险等级
                //TODO 保存签到记录
            }
        }
    }
}




