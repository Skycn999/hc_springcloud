package com.mi.hundsun.oxchains.base.core.model.quote;

import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.core.model.quote.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/***
 * 交易所5档行情明细
 * Created by xmf
 * Date: 2018/3/15
 **/
public class ExchangeDepth extends DepthVolume implements Serializable {
    private static final long serialVersionUID = -1266170855903364226L;
    private static final Logger logger = LoggerFactory.getLogger(ExchangeDepth.class);
    /**
     * 交易所类型
     */
    private String exchange_type;

    public String getExchange_type() {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type) {
        this.exchange_type = exchange_type;
    }


    @Override
    public String toString() {
        //格式{"bids":[[5价、量][4][3][2][1]],"asks":[[1][2][3][4][5]]}
        JSONObject bidAskObj = new JSONObject();
        List<List<String>> bidList = new ArrayList<>();
        List<List<String>> askList = new ArrayList<>();

        for (int i = 0; i < Constants.DEPTH; i++) {
            List<String> bidPriceVolume = new ArrayList<>();
            List<String> askPriceVolume = new ArrayList<>();
            try {
                Method getBidPriceMethod = this.getClass().getMethod("getBid_price" + (i + 1));
                Method getBidVolumeMethod = this.getClass().getMethod("getBid_volume" + (i + 1));
                Method getAskPriceMethod = this.getClass().getMethod("getAsk_price" + (i + 1));
                Method getAskVolumeMethod = this.getClass().getMethod("getAsk_volume" + (i + 1));
                String bidPrice = (String) getBidPriceMethod.invoke(this);
                String bidVolume = (String) getBidVolumeMethod.invoke(this);
                String askPrice = (String) getAskPriceMethod.invoke(this);
                String askVolume = (String) getAskVolumeMethod.invoke(this);
                bidPriceVolume.add(bidPrice);
                bidPriceVolume.add(bidVolume);
                askPriceVolume.add(askPrice);
                askPriceVolume.add(askVolume);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("ExchangeDepth转换为json格式字符串失败", e);
            }
            bidList.add(bidPriceVolume);
            askList.add(askPriceVolume);
        }
        bidAskObj.put("bids", bidList);
        bidAskObj.put("asks", askList);
        return bidAskObj.toJSONString();
    }
}
