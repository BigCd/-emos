package com.example.emos.wx;

import cn.hutool.core.util.StrUtil;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.db.dao.SysConfigMapper;
import com.example.emos.wx.db.pojo.SysConfig;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
@MapperScan("com.example.emos.wx.db.dao")
@ServletComponentScan
@Slf4j
@EnableAsync
public class EmosWxApiApplication {
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private SystemConstants constants;

    @Value("${emos.image-folder}")
    private String imageFolder;

    public static void main(String[] args) {
        SpringApplication.run(EmosWxApiApplication.class, args);
    }

    @PostConstruct
    public void init(){
        List<SysConfig> list = sysConfigMapper.selectAllParam();
        list.forEach(one->{
            String key = one.getParamKey();
            key = StrUtil.toCamelCase(key);
            String value = one.getParamValue();
            try {
                Field field = constants.getClass().getDeclaredField(key);
                field.set(constants,value);
            } catch (Exception e) {
                log.error("执行异常",e);
            }
        });
        new File(imageFolder).mkdirs();
    }

}
