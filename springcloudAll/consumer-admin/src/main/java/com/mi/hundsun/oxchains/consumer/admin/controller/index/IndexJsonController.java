package com.mi.hundsun.oxchains.consumer.admin.controller.index;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.api.sendEmail.SendEmailUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class IndexJsonController extends GenericController {


    /**
     * 昨日今日销售趋势图
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/hourtrend", method = RequestMethod.GET)
    public HashMap<String, Object> hourSaleTrend() {
//        //处理搜索参数
//        HashMap<String, Timestamp> timeMap = StatHelper.getStatParamsPrevAndCurrForDay(WinHelper.getFutureTimestamp(Calendar.DATE, -1));
//        //查询昨天记录
//        List<StatHours> statHoursListPrev = statHoursDao.findStatHoursByDate(timeMap.get("prevSTime"));
//        //查询今天记录
//        List<StatHours> statHoursListCurr = statHoursDao.findStatHoursByDate(timeMap.get("currSTime"));
//        //小时数组
//        Integer[] hoursArr = StatHelper.getHoursArr();
//        //初始化数据
//        HashMap<Integer, BigDecimal> statHoursDataTmp = new HashMap<>();
//        for (int i = 0; i < hoursArr.length; i++) {
//            statHoursDataTmp.put(hoursArr[i], new BigDecimal(0));
//        }
//        //构造图表
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("title_text", WinHelper.formatTimestamp(timeMap.get("currSTime"))+"销售走势");
//        params.put("legend_data", new String[]{"昨天", "当天"});
//        params.put("xAxis_data", hoursArr);
//        List<HashMap<String, Object>> seriesParams = new ArrayList<>();
//        //昨天数据
//        HashMap<String, Object> seriesParamsItemPrev = new HashMap<>();
//        seriesParamsItemPrev.put("name", "昨天");
//        HashMap<Integer, BigDecimal> seriesParamsItemDataPrev = (HashMap<Integer, BigDecimal>) statHoursDataTmp.clone();
//        for (int i = 0; i < statHoursListPrev.size(); i++) {
//            seriesParamsItemDataPrev.put(statHoursListPrev.get(i).getStatHour(), statHoursListPrev.get(i).getOrdersAmount());
//        }
//        seriesParamsItemPrev.put("data", StatHelper.getHashMapValuesAndSortByKeys(seriesParamsItemDataPrev));
//        seriesParams.add(seriesParamsItemPrev);
//        //今天数据
//        HashMap<String, Object> seriesParamsItemCurr = new HashMap<>();
//        seriesParamsItemCurr.put("name", "当天");
//        HashMap<Integer, BigDecimal> seriesParamsItemDataCurr = (HashMap<Integer, BigDecimal>) statHoursDataTmp.clone();
//        for (int i = 0; i < statHoursListCurr.size(); i++) {
//            seriesParamsItemDataCurr.put(statHoursListCurr.get(i).getStatHour(), statHoursListCurr.get(i).getOrdersAmount());
//        }
//        seriesParamsItemCurr.put("data", StatHelper.getHashMapValuesAndSortByKeys(seriesParamsItemDataCurr));
//        seriesParams.add(seriesParamsItemCurr);
//
//        //获得图表Json
//        HashMap<String, Object> chartsMap = EchartsHelper.getLineStack(params, seriesParams);
//        return chartsMap;
        return null;
    }


    /**
     * 7日内商品销售TOP20
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/goods/rank", method = RequestMethod.POST)
    public List<Object> goodsRank() {
//        Timestamp eTime = WinHelper.getTimestampOfDayStart(WinHelper.getCurrentTimestamp());
//        Timestamp sTime = WinHelper.getFutureTimestamp(eTime, Calendar.DATE, -7);
//        //查询列表
//        List<StatOrdersGoodsVo> statOrdersGoodsVoList = statOrdersGoodsDao.findStatOrdersGoodsVoForCommonIdAndBuyNumSort(sTime, eTime, 0, 0, 20);
//        //构造列表
//        List<Object> tableList = new ArrayList<>();
//        for(int i = 0; i < statOrdersGoodsVoList.size(); i++){
//            HashMap<String, Object> tableTr = new HashMap<>();
//            tableTr.put("sortNum", i+1);
//            tableTr.put("commonId", statOrdersGoodsVoList.get(i).getStatOrdersGoods().getCommonId());
//            tableTr.put("goodsName", statOrdersGoodsVoList.get(i).getStatOrdersGoods().getGoodsName());
//            tableTr.put("goodsBuyNumSum", statOrdersGoodsVoList.get(i).getGoodsBuyNumSum());
//            tableList.add(tableTr);
//        }
//        return tableList;
        return null;
    }


    /**
     * 测试发送邮件
     */
    @ResponseBody
    @RequestMapping(value = "sendMail", method = RequestMethod.GET)
    public ResultEntity goodsRank(String email) throws Exception {

        SendEmailUtils.sendEmail("测试邮件","测试发送邮件，收到就成功了",email);
        return ok();
    }
}