package com.fx.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.fx.entity.cus.BaseUser;

/**
 * 控制器
 */
@Component
public class BaseController {
	protected static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }

    public Map<String, Object> getToken() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", getSession().getId());
        return map;
    }
    
    /**
	 * 获取-登录用户基类对象
	 */
    protected static BaseUser getCurrentUser() {
        return (BaseUser) getSubject().getPrincipal();
    }
    
    /**
     * 获取-登录用户uuid（即sessionid）
     */
    protected static String getUUID() {
        return getSubject().getSession().getId().toString();
    }
    
}
