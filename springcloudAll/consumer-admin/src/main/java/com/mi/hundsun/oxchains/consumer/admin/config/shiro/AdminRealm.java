package com.mi.hundsun.oxchains.consumer.admin.config.shiro;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.po.system.AdminRole;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.consumer.admin.service.RedisService;
import com.mi.hundsun.oxchains.consumer.admin.service.system.*;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.consumer.admin.exception.CaptchaException;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by dqw on 2015/11/3.
 */
@Configuration
public class AdminRealm extends AuthorizingRealm {

    protected final Logger L = Logger.getLogger(AdminRealm.class);

    @Resource
    private AdminInterface adminInterface;
    @Resource
    private RoleMenuInterface roleMenuInterface;
    @Resource
    private RedisService redisService;
    @Resource
    private MenuInterface menuInterface;
    @Resource
    private AdminRoleInterface adminRoleInterface;
    @Resource
    private RoleInterface roleInterface;

    public String getName() {
        return "adminRealm";
    }

    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordCaptchaToken;
    }

    /**
     * 为当前登录的Subject授予角色和权限
     * 经测试:本例中该方法的调用时机为需授权资源被访问时
     * 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
     * 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
     * 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()
        String name = (String) principals.getPrimaryPrincipal();

        // 从数据库中获取登录用户
        Admin model = new Admin();
        model.setName(name);
        Admin admin = adminInterface.selectOne(model);
        if (admin == null) {
            throw new AuthorizationException();
        }
        List<AdminRole>  adminRoles = adminRoleInterface.select(new AdminRole(ar->{
            ar.setAdminId(admin.getId());
            ar.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(adminRoles ==null ||adminRoles.size()<1 ){
            throw new AuthorizationException();
        }
        AdminRole adminRole1 = adminRoles.get(0);
        // 获取登录用户的权限
        Set<String> permissionsSet = roleMenuInterface.findPermissionByRoleId(adminRole1.getRoleId());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        authorizationInfo.setStringPermissions(permissionsSet);

        //若该方法什么都不做直接返回null的话,就会导致任何用户访问时都会自动跳转到unauthorizedUrl指定的地址
        return authorizationInfo;
    }

    /**
     * 认证(登录时调用)
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 获取基于用户名和密码的令牌
        // 实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的
        // 两个token的引用都是一样的
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken;
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        String captcha = token.getCaptcha();

        // 验证用户输入的验证码
        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
        // 是否开通万能验证码
        Boolean superValidate = redisService.get(ConfigNID.SUPER_VALIDATE_OPEN, Boolean.class);
        // 开通万能验证码，可使用万能验证码
        if (superValidate != null && superValidate && "9999".equals(captcha)) {

        }else {
            if (null == captcha || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                throw new CaptchaException();
            }
        }

        Admin admin = adminInterface.selectOne(new Admin(a -> a.setName(username)));
        if (admin == null) {
            throw new AuthenticationException(username);
        }

        List<AdminRole>  adminRoles = adminRoleInterface.select(new AdminRole(ar->{
            ar.setAdminId(admin.getId());
            ar.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(adminRoles ==null ||adminRoles.size()<1 ){
            throw new AuthorizationException();
        }
        AdminRole adminRole = adminRoles.get(0);

        List<MenuModel> menuList = menuInterface.findPermiMenuList(adminRole.getRoleId(), 0);

        Role role = roleInterface.selectOne(new Role(r->{
            r.setId(adminRole.getRoleId());
            r.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(role == null){
            throw new AuthorizationException();
        }
        AdminSessionHelper.setAdminId(admin.getId());
        AdminSessionHelper.setAdminName(admin.getName());
        AdminSessionHelper.setAdminAvatarUrl(admin.getAvatarUrl());
        AdminSessionHelper.setAdminGroup(role.getName());
        AdminSessionHelper.setAdminMenu(menuList);
        AdminSessionHelper.setCurrAdmin(admin);

        // 此处无需比对,比对的逻辑Shiro会做,我们只需返回一个和令牌相关的正确的验证信息
        // 说白了就是第一个参数填登录用户名,第二个参数填合法的登录密码
        // 这样一来,在随后的登录页面上就只有这里指定的用户和密码才能通过验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username,
                password,
                getName()
        );
        return authenticationInfo;

    }


    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
