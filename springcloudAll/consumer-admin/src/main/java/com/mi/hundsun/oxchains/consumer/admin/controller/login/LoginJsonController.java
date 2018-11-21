package com.mi.hundsun.oxchains.consumer.admin.controller.login;

import com.mi.hundsun.oxchains.consumer.admin.config.shiro.UsernamePasswordCaptchaToken;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.AdminInterface;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 登录相关操作
 */
@Log4j
@Controller
@RequestMapping(BaseController.BASE_URI)
public class LoginJsonController extends GenericController {

    @Resource
    private AdminInterface adminInterface;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultEntity login(String username,String password,String captcha) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);

        UsernamePasswordCaptchaToken token =new UsernamePasswordCaptchaToken(username, password,captcha);

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();

        //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
        //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
        //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
        log.info("对用户[" + username + "]进行登录验证..验证开始");
        currentUser.login(token);
        log.info("对用户[" + username + "]进行登录验证..验证通过");

        Admin admin = adminInterface.selectOne(new Admin(a -> {
            a.setName(username);
        }));
        if (admin == null || !(DigestUtil.md5Hex(password).equals(admin.getPassword()))){
            SecurityUtils.getSubject().logout();
            throw new AuthenticationException(username);
        }
        // 验证是否登录成功
        if (!currentUser.isAuthenticated()) {
            log.info("用户[" + username + "]登录认证未通过");
            throw new UnauthorizedException();
        }
        return resultEntity;
    }

}
