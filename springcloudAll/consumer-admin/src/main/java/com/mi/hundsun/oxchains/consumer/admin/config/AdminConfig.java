package com.mi.hundsun.oxchains.consumer.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Donfy on 2017/5/25.
 */
@Slf4j
@Configuration
public class AdminConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("拦截器加载..");
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
