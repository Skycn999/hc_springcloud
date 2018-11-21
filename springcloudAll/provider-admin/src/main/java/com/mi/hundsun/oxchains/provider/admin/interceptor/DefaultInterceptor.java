package com.mi.hundsun.oxchains.provider.admin.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认拦截器 Created by donfy on 2016/10/21.
 */
@Slf4j
@Component
public class DefaultInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在请求处理之前进行调用（Controller方法调用之前）
        debugLog(request, handler);
        response.addHeader("Access-Control-Allow-Origin", "*");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
        // 初始化数据
        formatData(request, handler, response, modelAndView);

        log.info("test");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
        log.info("默认");

    }

    private void formatData(HttpServletRequest request, Object handler, HttpServletResponse response, ModelAndView modelAndView) {
        // 初始化基础数据
//        ModelMap mm = new ModelMap();
//        mm.addAttribute("publicRoot", "http://localhost:8081/");
//        mm.addAttribute("pluginsRoot", "http://localhost:8081/plugins/");
//        mm.addAttribute("jsRoot", "http://localhost:8081/js/");
//        mm.addAttribute("cssRoot", "http://localhost:8081/css/");
//        mm.addAttribute("imgRoot", "http://localhost:8081/");
//        mm.addAttribute("fileRoot", "http://localhost:8081/");
//        mm.addAttribute("adminRoot", "http://localhost:8081/");
//        HashMap<String, Long> map = new HashMap<>();
//        mm.addAttribute("countPrompt", map);
//        modelAndView.addAllObjects(mm);

    }

    private void debugLog(HttpServletRequest request, Object handler) {
//        Thread.currentThread().getStackTrace();
//        try {
//            Class clazz = ((HandlerMethod) handler).getBean().getClass();
//            Method m = ((HandlerMethod) handler).getMethod();
//            StackTraceElement traces =
//                    new StackTraceElement(clazz.getName(), m.getName(), clazz.getName(), 1);
//            StringBuilder sb = new StringBuilder();
//            sb.append("\n---------------------------------------------------------------------------------\n                   ")
//                    .append(traces)
//                    .append("\n          方法:     ")
//                    .append(m.getName())
//                    .append("\n          参数:     ");
//            request.getParameterMap().forEach((k, v) -> sb.append(k + "=" + v[0] + " "));
//            sb.append("\n---------------------------------------------------------------------------------");
//            log.info(sb.toString());
//        } catch (Exception e) {
//
//        }
    }
}
