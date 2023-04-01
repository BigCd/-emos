package com.example.emos.wx.config.shiro;

import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.service.TbCheckinService;
import com.example.emos.wx.service.TbUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;

    private TbUserService tbUserService;

    public boolean supports(AuthenticationToken token){
        return token instanceof OAuth2Token;
    }

    /**
     * 授权（验证权限时调用）
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //TODO查询用户的权限列表
        //TODO把权限列表添加到info对象中
        return info;
    }

    /**
     * 认证（登录时调用）
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //TODO 从令牌中获取userId，然后检测该账户是否被冻结。
        String accessToken = (String)token.getPrincipal();
        int userId = jwtUtil.getUserId(accessToken);
        TbUser user = tbUserService.searchById(userId);
        if(user==null){
            throw new LockedAccountException("账号已被锁定，请联系管理员");
        }
        //TODO 往info对象中添加用户信息、Token字符串
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo();
        return info;
    }
}
