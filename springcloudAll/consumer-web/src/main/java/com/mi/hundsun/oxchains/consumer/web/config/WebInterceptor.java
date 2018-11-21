package com.mi.hundsun.oxchains.consumer.web.config;

import com.mi.hundsun.oxchains.base.common.utils.SpringContextUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
import com.mi.hundsun.oxchains.base.common.utils.ToolDateTime;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 默认拦截器 Created by fengting on 2016/10/21.
 */
@Slf4j
@Component
public class WebInterceptor implements HandlerInterceptor {

    public static String COOKIE_SESSION_ID = "cookie:user:sessionId:";
    private static String CACHE_UUID_ID = "cache:user:uuid:";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BussinessException {
//        ConfigureService configureService = SpringContextUtils.getBean(ConfigureService.class);
//        String allowOrigin = configureService.getByNid("allow_origin");
        response.addHeader("Access-Control-Allow-Origin", /*"".equals(allowOrigin) ? "*" : allowOrigin*/ "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        // 在请求处理之前进行调用（Controller方法调用之前）
        debugLog(request, handler);
        // 登录校验
        this.doLoginAuth(request);
        //上线之前需要更改

        return true;
    }

    private void doLoginAuth(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // 判断是否需要拦截该地址
        if (this.isNeedLoginAuth(requestURI)) {
            String loginUserUuid = getLoginUserUuid(request);
            if (StringUtils.isBlank(loginUserUuid)) {
                throw new BussinessException(BussinessException.Code.E401);
            }
        }
    }

    private boolean isNeedLoginAuth(String uri) {
        if ("".equals(uri) || uri == null) {
            return false;
        }

        //包含的uri集合，可以使用*作为通配符
        List<String> includeUris = new ArrayList<>();
        includeUris.add("/api/web/tx/*");
        includeUris.add("/api/web/my/*");
        includeUris.add("/api/web/user/*");
        includeUris.add("/api/web/account/*");
        includeUris.add("/api/web/main/*");
        for (String str : includeUris) {
            //如果包含通配符
            if (str.contains("*")) {
                String prefix = str.substring(0, str.indexOf("*") - 1);
                if (uri.startsWith(prefix)) {
                    return true;
                }
            } else { //不包含通配符
                if (uri.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前登录用户的UUID
     *
     * @param request request
     * @return uuid
     */
    public static String getLoginUserUuid(HttpServletRequest request) {
        String sessionId = request.getParameter("sessionId");
        String uuid = request.getParameter("uuid");
        if (StringUtils.isBlank(uuid)) {
            throw new BussinessException(BussinessException.Code.E401);
        }
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        String encrypt = redisService.get(COOKIE_SESSION_ID + uuid + ":" + sessionId);
        if (StringUtils.isBlank(encrypt)) {
            throw new BussinessException(BussinessException.Code.E401);
        }
        String decryptData = ToolAES.decrypt(encrypt);
        if (null == decryptData || "".equals(decryptData)) {
            redisService.del(COOKIE_SESSION_ID + sessionId);
            throw new BussinessException(BussinessException.Code.E401);
        }
        String[] data = decryptData.split(".#.");
        //2.分解认证数据
        String cacheUuid;
        String userAgent;
        try {
            cacheUuid = data[1];
            userAgent = data[2];
        } catch (Exception e) {
            redisService.del(COOKIE_SESSION_ID + sessionId);
            throw new BussinessException(BussinessException.Code.E401);
        }
        if (!uuid.equals(cacheUuid)) {
            throw new BussinessException(BussinessException.Code.E401);
        }
        // 重新生成认证cookie，目的是更新时间戳
        String token = String.valueOf(ToolDateTime.getDateByTime()) + ".#." + cacheUuid + ".#." + userAgent + ".#." + sessionId;
        String authMark = ToolAES.encrypt(token);
        // 保存用户uuid到redis中
        redisService.put(COOKIE_SESSION_ID + sessionId, authMark, 1800);
        //更新用户uuid为key保存用户id的时间
        redisService.put(CACHE_UUID_ID + cacheUuid, redisService.get(CACHE_UUID_ID + cacheUuid, Integer.class), 1800);
        return cacheUuid;
    }

    /**
     * 获取当前登录用户
     *
     * @return User
     */
    public static Integer getLoginUserId() {
        HttpServletRequest request = GenericController.getRequest();
        String loginUserUuid = getLoginUserUuid(request);
        if (StringUtils.isBlank(loginUserUuid)) {
            throw new BussinessException(BussinessException.Code.E401);
        }
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        return redisService.get(CACHE_UUID_ID + loginUserUuid, Integer.class);
    }

    /**
     * 放置用户登录信息到缓存中
     *
     * @param user      登录用户
     * @param sessionId 会话ID
     */
    public static void setLoginUser(Users user, String sessionId) {
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        HttpServletRequest request = GenericController.getRequest();
        int maxAgeSeconds = 1800; //生命有效期30分钟
        //生成登录认证token
        String userAgent = request.getHeader("User-Agent");
        long timeStamp = ToolDateTime.getDateByTime();
        //待加密串
        String token = String.valueOf(timeStamp) + ".#." + user.getUuid() + ".#." + userAgent + ".#." + sessionId;
        String encrypt = ToolAES.encrypt(token);
        //删除之前未过期的同一用户的session数据
        removePreSessionInfo(user.getUuid());
        // 保存用户加密登录信息到redis中
        redisService.put(COOKIE_SESSION_ID + user.getUuid() + ":" + sessionId, encrypt, maxAgeSeconds);
        //以用户uuid为key保存用户id
        redisService.put(CACHE_UUID_ID + user.getUuid(), user.getId(), maxAgeSeconds);
    }

    /**
     * 删除之前的用户session信息
     */
    private static void removePreSessionInfo(String userUUid){
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        Set<String> keys = redisService.getKeys(COOKIE_SESSION_ID + userUUid);
        for (String key : keys) {
            redisService.del(key);
        }
    }


    /**
     * 清除用户在缓存中登录信息
     *
     * @param uuid      登录用户uuid
     * @param sessionId 会话ID
     */
    public static void outLoginUser(String uuid, String sessionId) {
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        // 清除用户在redis中密登录信息
        redisService.del(CACHE_UUID_ID + uuid);
        //清除用户uuid为key的用户id
        redisService.del(COOKIE_SESSION_ID + sessionId);
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

    private void debugLog(HttpServletRequest request, Object handler) {
        Thread.currentThread().getStackTrace();
        try {
            Class clazz = ((HandlerMethod) handler).getBean().getClass();
            Method m = ((HandlerMethod) handler).getMethod();
            StackTraceElement traces =
                    new StackTraceElement(clazz.getName(), m.getName(), clazz.getName(), 1);
            StringBuilder sb = new StringBuilder();
            sb.append("\n---------------------------------------------------------------------------------\n")
                    .append("类  : ")
                    .append(traces)
                    .append("\n方法: ")
                    .append(m.getName())
                    .append("\n参数: ");
            request.getParameterMap().forEach((k, v) -> sb.append(k).append("=").append(v[0]).append(" "));
            sb.append("\n---------------------------------------------------------------------------------\n");
            log.info(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
