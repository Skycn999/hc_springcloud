package com.mi.hundsun.oxchains.consumer.web.config;


import com.mi.hundsun.oxchains.base.common.utils.SpringContextUtils;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.consumer.web.service.tpl.NetWorthControlInterface;
import com.mi.hundsun.oxchains.consumer.web.service.tpl.ServiceFeeInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Init implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
         /* 缓存手续费模板 */
        setWebConfigToTpl();
        log.info("加载手续费模板成功！");
    }

    //缓存手续费模板
    private static void setWebConfigToTpl( ) {
//        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
//        //将手续费模板放入缓存
//        ServiceFeeInterface serviceFeeInterface = SpringContextUtils.getBean(ServiceFeeInterface.class);
//        //获取启用中默认的BTC币种手续费模板，若无默认，则获取最新启用的模板
//        ServiceFee btcFee =  serviceFeeInterface.findServiceFeeIsDefault(CoinCode.BTC);
//        redisService.put(CacheID.BTC_SERVICE_FEE_TPL,btcFee);
//        ServiceFee ethFee =  serviceFeeInterface.findServiceFeeIsDefault(CoinCode.ETH);
//        redisService.put(CacheID.ETH_SERVICE_FEE_TPL,ethFee);
//        //将净值风控模板放入缓存
//        NetWorthControlInterface netWorthControlInterface = SpringContextUtils.getBean(NetWorthControlInterface.class);
//        //获取启用中默认的模板，若无默认，则获取最新启用的模板
//        NetWorthControl netWorthControl =  netWorthControlInterface.findNetWorthControlIsDefault();
//        redisService.put(CacheID.NET_WORTH_CONTROL_TPL, netWorthControl);
}
}
