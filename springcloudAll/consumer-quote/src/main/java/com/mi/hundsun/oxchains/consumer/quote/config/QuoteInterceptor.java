package com.mi.hundsun.oxchains.consumer.quote.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 默认拦截器 Created by donfy on 2016/10/21.
 */
@Slf4j
@Component
@Configuration
public class QuoteInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在请求处理之前进行调用（Controller方法调用之前）
        debugLog(request, handler);
//        ConfigureService configureService = SpringContextUtils.getBean(ConfigureService.class);
//        String allowOrigin = configureService.getByNid("allow_origin");
        response.addHeader("Access-Control-Allow-Origin", /*StrUtil.isBlank(allowOrigin) ? "*" : allowOrigin*/ "*");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { }

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
