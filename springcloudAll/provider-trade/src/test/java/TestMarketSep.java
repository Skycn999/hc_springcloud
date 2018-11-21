/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.common.SubOrderModel;
import com.mi.hundsun.oxchains.base.core.common.VoDetail;
import com.mi.hundsun.oxchains.base.core.common.VoPrModel;
import com.mi.hundsun.oxchains.base.core.common.VolumeComparator;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-05-07 21:54.
 */

public class TestMarketSep {

    public static void main(String[] args) {
//        System.out.println(JSON.toJSON(new TestLimitedSep().sss("bid_volume2")));
        new TestMarketSep().testSeparateOrderByMarketPrice();
    }

    String json = "{\n" +
            "    \"code\": 200,\n" +
            "    \"message\": \"操作成功\",\n" +
            "    \"url\": \"\",\n" +
            "    \"data\": {\n" +
            "        \"bid_price10\": \"0.00145266\",\n" +
            "        \"bid_volume10\": \"8\",\n" +
            "        \"ask_volume10\": \"31.35\",\n" +
            "        \"ask_volume1\": \"305.1071096\",\n" +
            "        \"ask_volume3\": \"202.094276\",\n" +
            "        \"ask_volume2\": \"853.3383919\",\n" +
            "        \"ask_volume5\": \"1174.79485608\",\n" +
            "        \"ask_volume4\": \"1340.6\",\n" +
            "        \"ask_volume7\": \"2192.40085498\",\n" +
            "        \"ask_volume6\": \"8507.00350413\",\n" +
            "        \"ask_volume9\": \"14314.4990007\",\n" +
            "        \"ask_volume8\": \"270\",\n" +
            "        \"volume_detail\": \"{\\\"ask_volume1\\\":{\\\"bian\\\":\\\"102\\\",\\\"bitfinex\\\":\\\"105.1071096\\\",\\\"huobi\\\":\\\"98\\\"},\\\"ask_volume10\\\":{\\\"bian\\\":\\\"31.35\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume2\\\":{\\\"bian\\\":\\\"53.338391\\\",\\\"bitfinex\\\":\\\"90\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume3\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"202.094276\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume4\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"1340.6\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume5\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"1174.79485608\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume6\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"8507.00350413\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume7\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"2192.40085498\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume8\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"270\\\",\\\"huobi\\\":\\\"0\\\"},\\\"ask_volume9\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"14314.4990007\\\",\\\"huobi\\\":\\\"0\\\"},\\\"bid_volume1\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"418.62\\\"},\\\"bid_volume10\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"8\\\"},\\\"bid_volume2\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"100\\\"},\\\"bid_volume3\\\":{\\\"bian\\\":\\\"189\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"},\\\"bid_volume4\\\":{\\\"bian\\\":\\\"116.69\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"},\\\"bid_volume5\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"150\\\"},\\\"bid_volume6\\\":{\\\"bian\\\":\\\"100\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"},\\\"bid_volume7\\\":{\\\"bian\\\":\\\"0\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"70.66\\\"},\\\"bid_volume8\\\":{\\\"bian\\\":\\\"76.06\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"},\\\"bid_volume9\\\":{\\\"bian\\\":\\\"343.61\\\",\\\"bitfinex\\\":\\\"0\\\",\\\"huobi\\\":\\\"0\\\"}}\",\n" +
            "        \"ask_price8\": \"0.001454\",\n" +
            "        \"bid_price1\": \"0.00145556\",\n" +
            "        \"ask_price7\": \"0.001453\",\n" +
            "        \"bid_price2\": \"0.00145488\",\n" +
            "        \"ask_price9\": \"0.001455\",\n" +
            "        \"ask_price4\": \"0.00145\",\n" +
            "        \"bid_price5\": \"0.0014531\",\n" +
            "        \"ask_price3\": \"0.001449\",\n" +
            "        \"bid_price6\": \"0.001453\",\n" +
            "        \"ask_price6\": \"0.001452\",\n" +
            "        \"bid_price3\": \"0.0014538\",\n" +
            "        \"ask_price5\": \"0.001451\",\n" +
            "        \"bid_price4\": \"0.0014536\",\n" +
            "        \"ask_price10\": \"0.0014555\",\n" +
            "        \"bid_volume4\": \"116.69\",\n" +
            "        \"bid_volume5\": \"150\",\n" +
            "        \"bid_volume6\": \"100\",\n" +
            "        \"bid_volume7\": \"70.66\",\n" +
            "        \"bid_volume8\": \"76.06\",\n" +
            "        \"bid_volume9\": \"343.61\",\n" +
            "        \"bid_price9\": \"0.0014527\",\n" +
            "        \"ask_price2\": \"0.001448\",\n" +
            "        \"bid_price7\": \"0.00145293\",\n" +
            "        \"ask_price1\": \"0.001447\",\n" +
            "        \"bid_price8\": \"0.0014528\",\n" +
            "        \"bid_volume1\": \"418.62\",\n" +
            "        \"bid_volume2\": \"100\",\n" +
            "        \"bid_volume3\": \"189\"\n" +
            "    },\n" +
            "    \"uuid\": null\n" +
            "}";


    String accountsJson = "{\n" +
            "    \"code\": 200,\n" +
            "    \"message\": \"操作成功\",\n" +
            "    \"url\": \"\",\n" +
            "    \"data\": \"[{\\\"accountEmail\\\":\\\"db4@dasda .com\\\",\\\"accountId\\\":\\\"huobi2\\\",\\\"accountName\\\":\\\"account2\\\",\\\"apiKey\\\":\\\"VjRLMWIwaHpGYldpVkZrMjJwbmpSZz09\\\",\\\"apiSecret\\\":\\\"654321\\\",\\\"available\\\":2400.0000000000,\\\"coinCurrency\\\":\\\"BTC\\\",\\\"freeze\\\":0E-10,\\\"total\\\":2400.0000000000},{\\\"accountEmail\\\":\\\"db2@dasda .com\\\",\\\"accountId\\\":\\\"12111\\\",\\\"accountName\\\":\\\"account4\\\",\\\"apiKey\\\":\\\"VjRLMWIwaHpGYldpVkZrMjJwbmpSZz09\\\",\\\"apiSecret\\\":\\\"654321\\\",\\\"available\\\":11.0000000000,\\\"coinCurrency\\\":\\\"BTC\\\",\\\"freeze\\\":0E-10,\\\"total\\\":11.0000000000}]\",\n" +
            "    \"uuid\": null\n" +
            "}";

    private List<VoDetail> sss(String name) {

//        String s = json.replaceAll("\"", "\"").replaceAll("\n", "");
        ResultEntity resultEntity = JSON.parseObject(json, ResultEntity.class);
        Depth depth = JSON.parseObject(resultEntity.getData().toString(), Depth.class);
        String volume_detail = depth.getVolume_detail();
        List<VoDetail> voDetails = new ArrayList<>();
        Map<String, JSONObject> detailMap = JSON.parseObject(volume_detail, HashMap.class);
        for (Map.Entry<String, JSONObject> objectEntry : detailMap.entrySet()) {
            if (objectEntry.getKey().equals(name)) {
                JSONObject bid_volume11 = objectEntry.getValue();
                for (Map.Entry<String, Object> entry : bid_volume11.entrySet()) {
                    VoDetail voDetail = new VoDetail();
                    voDetail.setExchangeNo(entry.getKey());
                    voDetail.setVolume(entry.getValue().toString());
                    voDetails.add(voDetail);
                }
                voDetails.sort(new VolumeComparator());
            }
        }
        return voDetails;
    }

    public MainDelegation getMainOrder() {
        //生成主委托记录
        MainDelegation main = new MainDelegation();
        main.setUuid(RandomUtils.randomCustomUUID());
        main.setUserId(3);
        main.setDelegateNo(OrderNoUtils.getMainDelegateOrderNo());
        main.setStyle(MainDelegation.STYLE.MARKET.code);
        main.setOrigin(MainDelegation.ORIGIN.PC.code);
        main.setDirection(MainDelegation.DIRECTION.BUYIN.code);
        main.setCoinCurrency("BTC");
        main.setCoinCode("EOS");
        main.setAmount(BigDecimal.ZERO);
        main.setPrice(BigDecimal.ZERO);
        main.setGmv(new BigDecimal(0.8));
        main.setServiceFeeScale(new BigDecimal("0.01"));
        main.setState(MainDelegation.STATE.REPORTED.code);
        return main;
    }

    public void testSeparateOrderByMarketPrice() {
        ResultEntity resultEntity = JSON.parseObject(json, ResultEntity.class);
        Depth depth = JSON.parseObject(resultEntity.getData().toString(), Depth.class);

        ResultEntity acctResult = JSON.parseObject(accountsJson, ResultEntity.class);
        List<MotherAccountInfoModel> models = JSON.parseArray(acctResult.getData().toString(), MotherAccountInfoModel.class);
        System.out.println(this.separateOrderByMarketPrice(depth, getMainOrder()));
    }


    private Map<String, SubOrderModel> separateOrderByMarketPrice(Depth depth, MainDelegation delegate) {
        //市价买入时根据交易额来拆单
        //判断委托方向
        List<VoPrModel> voPrModels;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            //买入 - 解析卖档数据
            voPrModels = this.resolveDepth(depth, "ask");
        } else {
            //卖出 - 解析买档数据
            voPrModels = this.resolveDepth(depth, "bid");
        }

        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency().toLowerCase();
        Map<String, SubOrderModel> subOrderModels = new HashMap<>();
        List<VoDetail> voDetails;
        //剩余数量
        BigDecimal remainGmv = delegate.getGmv();
        for (VoPrModel prModel : voPrModels) {
            voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
            //是否跳出循环标记
            int c = 0;
            for (VoDetail voDetail : voDetails) {
                //声明本次下单数量
                BigDecimal thisGmv;
                //卖挡数据提供的交易额
                BigDecimal depthGmv = voDetail.getVolume().multiply(prModel.getPrice());
                //剩余数量小于等于0 表明已全部满足用户的委托数量 则跳出当前循环
                if (remainGmv.subtract(depthGmv).compareTo(BigDecimal.ZERO) <= 0) {
                    thisGmv = remainGmv;
                    c = 1;
                } else {
                    thisGmv = depthGmv;
                }
                remainGmv = remainGmv.subtract(depthGmv);
                SubOrderModel model = new SubOrderModel();
                model.setExchangeNo(voDetail.getExchangeNo());
                model.setVolume(thisGmv.setScale(10,BigDecimal.ROUND_HALF_UP));
                model.setPrice(delegate.getPrice());
                //判断集合中是否存在相同的交易所 如果存在则把数量相加
                int has = 0;
                for (String key : subOrderModels.keySet()) {
                    SubOrderModel m = subOrderModels.get(key);
                    if (key.equals(model.getExchangeNo())) {
                        //四舍五入
                        m.setVolume(m.getVolume().add(thisGmv).setScale(10,BigDecimal.ROUND_HALF_UP));
                        has = 1;
                    }
                }
                //不存在 则put
                if(has == 0) {
                    subOrderModels.put(voDetail.getExchangeNo(), model);
                }

                if (c == 1) {
                    break;
                }
            }
            if (c == 1) {
                break;
            }
        }

        return subOrderModels;
    }

    private List<VoDetail> getVolumeDetailByName(String name, Depth depth) {
        String volume_detail = depth.getVolume_detail();
        List<VoDetail> voDetails = new ArrayList<>();
        Map<String, JSONObject> detailMap = JSON.parseObject(volume_detail, HashMap.class);
        for (Map.Entry<String, JSONObject> objectEntry : detailMap.entrySet()) {
            if (objectEntry.getKey().equals(name)) {
                JSONObject bid_volume11 = objectEntry.getValue();
                for (Map.Entry<String, Object> entry : bid_volume11.entrySet()) {
                    VoDetail voDetail = new VoDetail();
                    voDetail.setExchangeNo(entry.getKey());
                    voDetail.setVolume(String.valueOf(entry.getValue()));
                    //判断数量=0的交易所
                    if (new BigDecimal(String.valueOf(entry.getValue())).compareTo(BigDecimal.ZERO) > 0) {
                        voDetails.add(voDetail);
                    }
                }
                voDetails.sort(new VolumeComparator());
            }
        }
        return voDetails;
    }

    private List<VoPrModel> resolveDepth(Depth depth, String type) {
        List<VoPrModel> voPrModels = new ArrayList<>();
        switch (type) {
            case "ask":
                VoPrModel model1 = new VoPrModel();
                model1.setName("ask_volume1");
                model1.setPrice(depth.getAsk_price1());
                model1.setVolume(depth.getAsk_volume1());
                voPrModels.add(model1);
                VoPrModel model2 = new VoPrModel();
                model2.setName("ask_volume2");
                model2.setPrice(depth.getAsk_price2());
                model2.setVolume(depth.getAsk_volume2());
                voPrModels.add(model2);
                VoPrModel model3 = new VoPrModel();
                model3.setName("ask_volume3");
                model3.setPrice(depth.getAsk_price3());
                model3.setVolume(depth.getAsk_volume3());
                voPrModels.add(model3);
                VoPrModel model4 = new VoPrModel();
                model4.setName("ask_volume4");
                model4.setPrice(depth.getAsk_price4());
                model4.setVolume(depth.getAsk_volume4());
                voPrModels.add(model4);
                VoPrModel model5 = new VoPrModel();
                model5.setName("ask_volume5");
                model5.setPrice(depth.getAsk_price5());
                model5.setVolume(depth.getAsk_volume5());
                voPrModels.add(model5);
                VoPrModel model6 = new VoPrModel();
                model6.setName("ask_volume6");
                model6.setPrice(depth.getAsk_price6());
                model6.setVolume(depth.getAsk_volume6());
                voPrModels.add(model6);
                VoPrModel model7 = new VoPrModel();
                model7.setName("ask_volume7");
                model7.setPrice(depth.getAsk_price7());
                model7.setVolume(depth.getAsk_volume7());
                voPrModels.add(model7);
                VoPrModel model8 = new VoPrModel();
                model8.setName("ask_volume8");
                model8.setPrice(depth.getAsk_price8());
                model8.setVolume(depth.getAsk_volume8());
                voPrModels.add(model8);
                VoPrModel model9 = new VoPrModel();
                model9.setName("ask_volume9");
                model9.setPrice(depth.getAsk_price9());
                model9.setVolume(depth.getAsk_volume9());
                voPrModels.add(model9);
                VoPrModel model10 = new VoPrModel();
                model10.setName("ask_volume10");
                model10.setPrice(depth.getAsk_price10());
                model10.setVolume(depth.getAsk_volume10());
                voPrModels.add(model10);
                break;
            case "bid":
                VoPrModel bidModel1 = new VoPrModel();
                bidModel1.setName("bid_volume1");
                bidModel1.setPrice(depth.getBid_price1());
                bidModel1.setVolume(depth.getBid_volume1());
                voPrModels.add(bidModel1);
                VoPrModel bidModel2 = new VoPrModel();
                bidModel2.setName("bid_volume2");
                bidModel2.setPrice(depth.getBid_price2());
                bidModel2.setVolume(depth.getBid_volume2());
                voPrModels.add(bidModel2);
                VoPrModel bidModel3 = new VoPrModel();
                bidModel3.setName("bid_volume3");
                bidModel3.setPrice(depth.getBid_price3());
                bidModel3.setVolume(depth.getBid_volume3());
                voPrModels.add(bidModel3);
                VoPrModel bidModel4 = new VoPrModel();
                bidModel4.setName("bid_volume4");
                bidModel4.setPrice(depth.getBid_price4());
                bidModel4.setVolume(depth.getBid_volume4());
                voPrModels.add(bidModel4);
                VoPrModel bidModel5 = new VoPrModel();
                bidModel5.setName("bid_volume5");
                bidModel5.setPrice(depth.getBid_price5());
                bidModel5.setVolume(depth.getBid_volume5());
                voPrModels.add(bidModel5);
                VoPrModel bidModel6 = new VoPrModel();
                bidModel6.setName("bid_volume6");
                bidModel6.setPrice(depth.getBid_price6());
                bidModel6.setVolume(depth.getBid_volume6());
                voPrModels.add(bidModel6);
                VoPrModel bidModel7 = new VoPrModel();
                bidModel7.setName("bid_volume7");
                bidModel7.setPrice(depth.getBid_price7());
                bidModel7.setVolume(depth.getBid_volume7());
                voPrModels.add(bidModel7);
                VoPrModel bidModel8 = new VoPrModel();
                bidModel8.setName("bid_volume8");
                bidModel8.setPrice(depth.getBid_price8());
                bidModel8.setVolume(depth.getBid_volume8());
                voPrModels.add(bidModel8);
                VoPrModel bidModel9 = new VoPrModel();
                bidModel9.setName("bid_volume9");
                bidModel9.setPrice(depth.getBid_price9());
                bidModel9.setVolume(depth.getBid_volume9());
                voPrModels.add(bidModel9);
                VoPrModel bidModel10 = new VoPrModel();
                bidModel10.setName("bid_volume10");
                bidModel10.setPrice(depth.getBid_price10());
                bidModel10.setVolume(depth.getBid_volume10());
                voPrModels.add(bidModel10);
                break;
        }
        return voPrModels;
    }

}
