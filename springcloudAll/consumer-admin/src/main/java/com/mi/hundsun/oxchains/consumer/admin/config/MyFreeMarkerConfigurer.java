package com.mi.hundsun.oxchains.consumer.admin.config;

import com.mi.hundsun.oxchains.consumer.admin.tags.ShiroTags;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class MyFreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {
    @Override
    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
        return super.getAggregateTemplateLoader(templateLoaders);
    }

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }
}
