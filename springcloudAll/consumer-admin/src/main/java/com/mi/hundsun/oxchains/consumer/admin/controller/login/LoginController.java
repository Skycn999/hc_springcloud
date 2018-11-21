package com.mi.hundsun.oxchains.consumer.admin.controller.login;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录相关操作
 */
@Log4j
@Controller
@RequestMapping(BaseController.BASE_URI)
public class LoginController extends BaseController {

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap modelMap, HttpServletResponse response) {
        //如果已经登录跳转到首页
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }
        response.addHeader("ADMIN_LOGIN_STATE", "0");
        return "login";
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

}
