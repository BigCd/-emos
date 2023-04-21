package com.example.emos.wx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.dao.TbDeptMapper;
import com.example.emos.wx.db.pojo.MessageEntity;
import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.TbUserService;
import com.example.emos.wx.db.dao.TbUserMapper;
import com.example.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* @author Administrator
* @description 针对表【tb_user(用户表)】的数据库操作Service实现
* @createDate 2023-03-23 19:00:59
*/
@Service
@Slf4j
@Scope("prototype")
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser>
    implements TbUserService{

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Resource
    private TbUserMapper tbUserMapper;

    @Autowired
    private MessageTask messageTask;

    @Resource
    private TbDeptMapper tbDeptMapper;

    /**
     * 获取到微信OpenId
     * @param code
     * @return
     */
    private String getOpenId(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登陆凭证错误");
        }
        return openId;
    }

    /**
     * 注册超级管理员
     * @param registerCode
     * @param code
     * @param nickname
     * @param photo
     * @return
     */
    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        //如果邀请码是000000，代表是超级管理员
        if (registerCode.equals("000000")) {
            //查询超级管理员帐户是否已经绑定
            boolean bool = tbUserMapper.haveRootUser();
            if (!bool) {
                //把当前用户绑定到ROOT帐户
                String openId = getOpenId(code);
                HashMap param = new HashMap();
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                tbUserMapper.insert(param);
                int id = tbUserMapper.searchIdByOpenId(openId);



                MessageEntity entity = new MessageEntity();
                entity.setSenderId(0);
                entity.setSenderName("系统消息");
                entity.setUuid(IdUtil.simpleUUID());
                entity.setMsg("欢迎您注册成为超级管理员，请及时更新你的员工个人信息。");
                entity.setSendTime(new Date());
                messageTask.sendAsync(id + "", entity);
                return id;
            }else {
                //如果root已经绑定了，就抛出异常
                throw new EmosException("无法绑定超级管理员账号");
            }
        }
        //TODO 此处还有其他判断内容
        else{
            return 0;
        }
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions=tbUserMapper.searchUserPermissions(userId);
        return permissions;
    }

    /**
     *  用户在Emos登陆页面点击登陆按钮，
     *  然后小程序把临时授权字符串提交给后端Java系统。
     *  后端Java系统拿着临时授权字符串换取到openid，
     *  我们查询用户表中是否存在这个openid。
     *  如果存在，意味着该用户是已注册用户，可以登录。
     *  如果不存在，说明该用户尚未注册，目前还不是我们的员工，所以禁止登录。
     * @param code
     * @return
     */
    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = tbUserMapper.searchIdByOpenId(openId);
        if(id==null){
            throw new EmosException("账户不存在");
        }
        //从消息队列中接受消息，转移到消息表
        messageTask.receiveAysnc(id+"");
        return id;
    }

    /**
     * 查询用户信息，然后判断用现在是在职还是离职状态。
     * 如果是在职状态，那就可以创建认证对象，反之就抛出异常
     * @param userId
     * @return
     */
    @Override
    public TbUser searchById(int userId) {
        TbUser user = tbUserMapper.searchById(userId);
        return user;
    }

    /**
     * 那天入职的
     * @param userId
     * @return
     */
    @Override
    public String searchUserHiredate(int userId) {
        String hiredate = tbUserMapper.searchUserHiredate(userId);
        return hiredate;
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        HashMap map = tbUserMapper .searchUserSummary(userId);
        return map;
    }

    @Override
    public ArrayList<HashMap> searchUserGroupByDept(String keyword) {
        ArrayList<HashMap> list_1 = tbDeptMapper.searchDeptMembers(keyword);
        ArrayList<HashMap> list_2 = tbUserMapper.searchUserGroupByDept(keyword);
        for (HashMap map_1:list_1) {
            long deptId = (Long)map_1.get("id");
            ArrayList members = new ArrayList();
            for(HashMap map_2 : list_2){
                long id = (Long)map_2.get("deptId");
                if(deptId == id){
                    members.add(map_2);
                }
            }
            map_1.put("members",members);
        }
        return list_1;
    }

    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        ArrayList<HashMap> list = tbUserMapper.searchMembers(param);
        return list;
    }


}




