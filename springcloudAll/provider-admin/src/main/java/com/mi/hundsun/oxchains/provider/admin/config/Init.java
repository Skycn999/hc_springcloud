package com.mi.hundsun.oxchains.provider.admin.config;

import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.State;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.system.ConfigureService;
import com.mi.hundsun.oxchains.base.core.service.system.MsgTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化数据
 */
@Slf4j
@Component
public class Init implements CommandLineRunner {
    @Autowired
    MsgTemplateService msgTemplateService;
    @Autowired
    ConfigureService configureService;
    @Autowired
    RedisService redisService;


    @Override
    public void run(String... strings) throws Exception {
        log.info("初始化数据...");
//        MsgTemplateInterface messageTemplateService = SpringContextUtils.getBean(MsgTemplateInterface.class);
//        ConfigureInterface configureService = SpringContextUtils.getBean(ConfigureInterface.class);
//        RedisService redisService = SpringContextUtils.getBean(RedisService.class);


        /* 缓存系统参数 */
        setWebConfigToRedis(configureService.select(new Configure(t -> t.setStatus(State.YES))), redisService);
        log.info("加载系统参数成功！");

        /* 缓存消息模板 */
        setMessageTempToRedis(msgTemplateService.select(new MsgTemplate()), redisService);
        log.info("加载消息模板成功！");
    }

    private static void setWebConfigToRedis(List<Configure> list, RedisService redisService) {
        // 更新缓存系统参数
        for (Configure config : list) {
            log.debug("缓存参数:key=" + CacheID.CONFIG_PREFIX + config.getNid() + ",val=" + config.getValue());
            redisService.put(CacheID.CONFIG_PREFIX + config.getNid(), config.getValue());
        }
    }

    private static void setMessageTempToRedis(List<MsgTemplate> list, RedisService redisService) {
        // 更新缓存系统参数
        for (MsgTemplate template : list) {
            log.debug("缓存参数:key=" + CacheID.TEMPLATE_MESSAGE_PREFIX + template.getNid() + ",val=" + template);
            redisService.put(CacheID.TEMPLATE_MESSAGE_PREFIX + template.getNid(), template);
        }
    }

}
