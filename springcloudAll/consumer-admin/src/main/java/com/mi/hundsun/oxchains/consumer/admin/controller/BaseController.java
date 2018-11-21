package com.mi.hundsun.oxchains.consumer.admin.controller;

import com.mi.hundsun.oxchains.consumer.admin.service.system.MenuInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import com.mi.hundsun.oxchains.base.common.annotation.SysLog;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.model.system.MenuStateModel;
import com.mi.hundsun.oxchains.consumer.admin.service.RedisService;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 页面转跳类controller基础实现
 */
@Slf4j
public class BaseController {

    protected static final String ADMIN_TEMPLATE_ROOT = "";
    /**
     * 接口根路径
     */
    public static final String BASE_URI = "/s";

    @Autowired
    protected RedisService redisService;
    @Autowired
    private MenuInterface menuInterface;

    private String path = "";

    /**
     * 手工设置选中菜单
     *
     * @param path
     */
    public void setMenuPath(String path) {
        this.path = path;
    }

    protected String ok(String template) {
        String path = ADMIN_TEMPLATE_ROOT + template;
        log.info(path);
        return path;
    }


    /**
     * 过滤表单
     */
//    @InitBinder
//    public void initBinder(WebDataBinder binder, WebRequest request) {
//        myWebBindingInitializer.initBinder(binder, request);
//    }
    protected String getAdminTemplate(String template) {
        log.info(ADMIN_TEMPLATE_ROOT + template);
        return ADMIN_TEMPLATE_ROOT + template;
    }

    /**
     * 管理员用户名
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("adminName")
    public String getAdminName() {
        return AdminSessionHelper.getAdminName();
    }

    /**
     * 管理员组
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("adminGroupName")
    public String getAdminGroupName() {
        return AdminSessionHelper.getAdminGroup();
    }

    /**
     * 管理员头像URl
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("adminAvatarUrl")
    public String getAdminAvatarUrl() {
        String avatarUrl = AdminSessionHelper.getAdminAvatarUrl();
        if (StrUtil.isBlank(avatarUrl)) avatarUrl = null;
        return avatarUrl;
    }

    /**
     * 后台菜单
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("adminMainMenu")
    public List<MenuModel> getMenu() {
        List<MenuModel> menuList = AdminSessionHelper.getAdminMenu();
        return menuList;
    }

    /**
     * 后台菜单选中状态
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("menuState")
    public MenuStateModel getMenuStateVo() {
        if (StrUtil.isBlank(path)) {
            // FIXME: 2017/8/22 临时使用,需要重新整合左侧菜单栏选中效果
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String url = request.getRequestURL().toString();
            String root = adminRoot();
            if (url.contains(root)) url = url.substring(root.length(), url.length());

            return menuInterface.getMenuState(url);

        }
        return menuInterface.getMenuState(path);
    }

    /**
     * 前台根路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("webRoot")
    public String webRoot() {
        return redisService.get(ConfigNID.ADMIN_SEVER_URL);
    }

    /**
     * 前台根路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("copyRight")
    public String copyRight() {
        return redisService.get(ConfigNID.COPY_RIGHT);
    }

    /**
     * 网站名称
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("webName")
    public String webName() {
        return redisService.get(ConfigNID.WEB_NAME);
    }

    /**
     * 后台根路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("adminRoot")
    public String adminRoot() {
        return redisService.get(ConfigNID.ADMIN_SEVER_URL);
    }

    /**
     * 文件服务器路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("fileRoot")
    public String fileRoot() {
        return redisService.get(ConfigNID.IMAGE_SEVER_URL);
    }

    /**
     * 后台JS路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("jsRoot")
    public String jsRoot() {
        return redisService.get(ConfigNID.STATIC_SERVER_URL) + "static/js/";
    }

    /**
     * 后台CSS路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("cssRoot")
    public String cssRoot() {
        return redisService.get(ConfigNID.STATIC_SERVER_URL) + "static/css/";
    }
    /**
     * 后台图片资源路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("imgRoot")
    public String imgRoot() {
        return redisService.get(ConfigNID.STATIC_SERVER_URL) + "static/img/";
    }

    /**
     * 后台插件路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("pluginsRoot")
    public String pluginsRoot() {
        return redisService.get(ConfigNID.STATIC_SERVER_URL) + "static/plugins/";
    }

    /**
     * 公共资源路径
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("publicRoot")
    public String publicRoot() {
        return redisService.get(ConfigNID.STATIC_SERVER_URL) + "public/";
    }


    /**
     * 后台顶部代办事项提醒
     *
     * @return
     */
    @SysLog(false)
    @ModelAttribute("countPrompt")
    public HashMap<String, Long> countPrompt() {
        HashMap<String, Long> map = new HashMap<>();
        // 消息数量
        long msgCount = 0L;

        return map;
    }

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({UnavailableSecurityManagerException.class})
    public String unavailableSecurityManagerException(UnavailableSecurityManagerException e) {
        return "redirect:/";
    }

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public String exception(Exception e) {
        e.printStackTrace();
        log.error(e.toString(), e);
        return "redirect:/";
    }


}
