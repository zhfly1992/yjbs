package com.fx.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fx.commons.utils.enums.MStatus;
import com.fx.commons.utils.other.PasswordHelper;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.permi.Menu;
import com.fx.entity.cus.permi.Role;
import com.fx.service.cus.BaseUserService;
import com.fx.service.cus.permi.MenuService;
import com.fx.service.cus.permi.RoleService;

/**
 * 自定义realm
 */
@Component
public class MyAuthRealm extends AuthorizingRealm {

	/** 用户基类-服务 */
    @Autowired
    private BaseUserService baseUserSer;

    @Autowired
    private RoleService roleSer;

    @Autowired
    private MenuService menuSer;

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         * 1. 获取当前登录用户信息
         * 2. 获取当前用户关联的角色、权限等
         * 3. 将角色、权限封装到AuthorizationInfo对象中并返回
         */
    	BaseUser user = (BaseUser) SecurityUtils.getSubject().getPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色
        List<Role> roleList = roleSer.findUserRole(user.getUname());
        Set<String> roleSet = roleList.stream().map(Role::getName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限
        List<Menu> menuList = menuSer.findUserPermissions(user.getUname());
        Set<String> permSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permSet);

        return simpleAuthorizationInfo;
    }

    /**
     * 身份校验
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        /**
         * 1. 从token中获取输入的用户名
         * 2. 通过username查询数据库得到用户对象
         * 3. 调用Authenticator进行密码校验
         */

        // 获取用户名和密码
        String uname = (String) token.getPrincipal();
        BaseUser user = baseUserSer.findByUname(uname);

        if (user == null) {
            throw new UnknownAccountException(String.valueOf(MStatus.ACCOUNT_UNKNOWN.getInfo()));
        }else {
        	// 验证登录用户是单位登录、后台登录、个人用户登录
        	
        	
        }
        
        String password = new String((char[]) token.getCredentials());
        PasswordHelper passhelper = new PasswordHelper();
        String md5Pwd = passhelper.encryptPassword(password, user.getSalt());
        
        /**
         * 交给Shiro进行密码的解密校验
         * 调用SecurityUtils.getSubject().getPrincipal() 遇到类型转换问题，报错 String !=> User
         * 请参考这篇文章：{@link https://blog.csdn.net/qq_35981283/article/details/78634575}
         */
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                md5Pwd,
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
        
        return authenticationInfo;
    }
}
