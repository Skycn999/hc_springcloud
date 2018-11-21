package com.mi.hundsun.oxchains.base.core.model.quote.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.model.quote.DepthVolume;
import com.mi.hundsun.oxchains.base.core.model.quote.ExchangeDepth;
import com.mi.hundsun.oxchains.base.core.model.quote.Tick;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Created by xmf
 * Date: 2018/3/8
 **/
//@DependsOn(value = "injectParams")
@Component
public class CommonMethod {
    @Autowired
    private InjectParams injectParams;
    private static final Logger logger = LoggerFactory.getLogger(CommonMethod.class);

    /**
     * 从depth原始数据中获取bids并设置买5档
     *
     * @param exchangeDepth
     * @param depthObject
     * @return
     */
    public void setExchangeDepthBids(ExchangeDepth exchangeDepth, Class exchangeDepthClass, JSONObject depthObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        JSONArray bidArrays = depthObject.getJSONArray(Constants.BIDS);
        int toIndex = bidArrays.size() >= Constants.DEPTH ? Constants.DEPTH : bidArrays.size();
        for (int i = 0; i < toIndex; i++) {
            Method setBidPriceMethod = exchangeDepthClass.getMethod("setBid_price" + (i + 1), Class.forName("java.lang.String"));
            Method setBidVolumeMethod = exchangeDepthClass.getMethod("setBid_volume" + (i + 1), Class.forName("java.lang.String"));
            JSONArray bidArray = bidArrays.getJSONArray(i);
            if (bidArray.size() >= 2) {
                String price = bidArray.getString(0);
                String volume = bidArray.getString(1);
                setBidPriceMethod.invoke(exchangeDepth, price);
                setBidVolumeMethod.invoke(exchangeDepth, volume);
            } else {
                logger.error(Constants.Error.RESPONSE_FORMAT_ERROR[0], Constants.Error.RESPONSE_FORMAT_ERROR[1] + "[bids字段值数据格式错误：数组长度不为2，无法获取price和volume]");
            }
        }
    }

    /**
     * 校验是否启用了除depth以外的其他功能
     */
    public void enableAllExceptDepthCheck() {
        if (!injectParams.isEnableAllExceptDepth()) {
            throw new BussinessException(Constants.Error.FUNCTION_NOT_ENABLE[0], Constants.Error.FUNCTION_NOT_ENABLE[1]);
        }
    }

    /**
     * 校验是否启用了depth功能
     */
    public void enableDepthCheck() {
        if (!injectParams.isEnableDepth()) {
            throw new BussinessException(Constants.Error.FUNCTION_NOT_ENABLE[0], Constants.Error.FUNCTION_NOT_ENABLE[1]);
        }
    }

    /**
     * kline类型校验
     *
     * @param klineType
     */
    public void klineTypeValid(String klineType) {
        if (!Constants.KLINE_TYPES.contains(klineType)) {
            throw new BussinessException(Constants.Error.PARAM_INVALID[0], Constants.Error.PARAM_INVALID[1] + "[kline_type]");
        }
    }

    /**
     * size校验
     *
     * @param size
     * @return
     */
    public int formatSize(String size) {
        int sizeInt = injectParams.getDefaultReqKlineSize();
        if (null != size && StringUtils.isNotEmpty(size)) {
            try {
                sizeInt = Integer.parseInt(size);
                if (sizeInt <= 0) {
                    logger.error("{}[size]", Constants.Error.PARAM_INVALID[1]);
                    throw new BussinessException(Constants.Error.PARAM_INVALID[0], Constants.Error.PARAM_INVALID[1] + "[size]");
                }
            } catch (NumberFormatException e) {
                logger.error("{}[size]", Constants.Error.PARAM_INVALID[1]);
                throw new BussinessException(Constants.Error.PARAM_INVALID[0], Constants.Error.PARAM_INVALID[1] + "[size]");
            }
        }
        return sizeInt;
    }

    /**
     * symbol校验
     *
     * @param symbol
     */
    public void symbolValid(String symbol) {
        if (null == symbol || StringUtils.isEmpty(symbol)) {
            throw new BussinessException(Constants.Error.NO_REQUIRE_PARAM[0], Constants.Error.NO_REQUIRE_PARAM[1] + "[symbol]");
        }
    }

    /**
     * 生成CountDownLatch列表
     *
     * @param totalCount
     * @return
     */
    public List<CountDownLatch> genLatchList(int totalCount, int onceHttpInitThreadCount) {
        int remainder = totalCount % onceHttpInitThreadCount;
        int initTimes = remainder == 0 ? totalCount / onceHttpInitThreadCount : totalCount / onceHttpInitThreadCount + 1;
        List<CountDownLatch> latchList = new ArrayList<>(initTimes);
        for (int i = 0; i < initTimes; i++) {
            int threadCount;
            if (i == initTimes - 1) {
                threadCount = remainder == 0 ? onceHttpInitThreadCount : remainder;
            } else {
                threadCount = onceHttpInitThreadCount;
            }
            final CountDownLatch latch = new CountDownLatch(threadCount);
            latchList.add(latch);
        }
        return latchList;
    }


    /**
     * 火币原始数据中生成分笔数据
     */
    public List<Tick> huoBiFormTick(JSONObject tickObject) {
        List<Tick> tickList = new ArrayList<>();
        //因为数据是数组，如果出现多条数据的情况需要，需要取所有数据
        JSONArray tickDataArray = tickObject.getJSONArray("data");
        if (null != tickDataArray) {
            Iterator it = tickDataArray.iterator();
            while (it.hasNext()) {
                JSONObject tickData = (JSONObject) it.next();
                String businessTime = tickData.getString("ts");
                String businessPrice = tickData.getString("price");
                String businessAmount = tickData.getString("amount");
                String originBusinessDirection = tickData.getString("direction");
                String businessDirection = Constants.HuoBi.TICK_BUSINESS_DIRECTION.get(originBusinessDirection);
                Tick tick = new Tick();
                tick.setBusiness_time(businessTime);
                tick.setBusiness_price(businessPrice);
                tick.setBusiness_amount(businessAmount);
                tick.setBusiness_direction(businessDirection);
                tickList.add(tick);
            }

        }
        return tickList;
    }

    /**
     * 价格排序
     *
     * @param exchangeDepthList
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Map<String, List<BigDecimal>> sortPrice(List<ExchangeDepth> exchangeDepthList) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, List<BigDecimal>> bidAskPriceMap = new HashMap<>();
        Set<BigDecimal> bidPriceSet = new TreeSet<>();
        Set<BigDecimal> askPriceSet = new TreeSet<>();
        Class exchangeDepthClass = Class.forName("com.hundsun.digiccy.domain.ExchangeDepth");
        for (int i = 0; i < exchangeDepthList.size(); i++) {
            ExchangeDepth exchangeDepth = exchangeDepthList.get(i);
            for (int j = 1; j <= Constants.DEPTH; j++) {
                Method getBidPriceMethod = exchangeDepthClass.getMethod("getBid_price" + j);
                Method getAskPriceMethod = exchangeDepthClass.getMethod("getAsk_price" + j);
                BigDecimal bidPrice = new BigDecimal((String) getBidPriceMethod.invoke(exchangeDepth));
                bidPriceSet.add(bidPrice);
                BigDecimal askPrice = new BigDecimal((String) getAskPriceMethod.invoke(exchangeDepth));
                askPriceSet.add(askPrice);
            }
        }
        List<BigDecimal> bidPriceList = new ArrayList<>(bidPriceSet);
        //ask从小到大
        List<BigDecimal> askPriceList = new ArrayList<>(askPriceSet);
        //bid从大到小
        Collections.reverse(bidPriceList);
        if (bidPriceList.size() > Constants.DEPTH) {
            bidPriceList = bidPriceList.subList(0, Constants.DEPTH);
        }
        if (askPriceList.size() > Constants.DEPTH) {
            askPriceList = askPriceList.subList(0, Constants.DEPTH);
        }
        bidAskPriceMap.put(Constants.BIDS, bidPriceList);
        bidAskPriceMap.put(Constants.ASKS, askPriceList);
        return bidAskPriceMap;
    }

    /**
     * 生成聚合行情
     *
     * @param exchangeDepthList
     * @param sortedPriceList
     * @param depthAggregation
     * @param depthVolumeMap
     * @param type
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void generatePriceVolumeAggregation(List<ExchangeDepth> exchangeDepthList, List<BigDecimal> sortedPriceList, Depth depthAggregation, Map<String, DepthVolume> depthVolumeMap, String type) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //先根据卖价排序，找到最小的卖价
        Class exchangeDepthClass = Class.forName("com.hundsun.digiccy.domain.ExchangeDepth");
        Class depthClass = Class.forName("com.hundsun.digiccy.domain.Depth");
        Class depthVolumeClass = Class.forName("com.hundsun.digiccy.domain.DepthVolume");

        for (int i = 0; i < sortedPriceList.size(); i++) {
            Method setDepthVolumeMethod = null;
            Method setDepthPriceMethod = null;
            Method setDepthVolumVolumMethod = null;
            int depth = i + 1;
            if (type.equalsIgnoreCase(Constants.BIDS)) {
                setDepthVolumeMethod = depthClass.getMethod("setBid_volume" + depth, Class.forName("java.lang.String"));
                setDepthPriceMethod = depthClass.getMethod("setBid_price" + depth, Class.forName("java.lang.String"));
            } else if (type.equalsIgnoreCase(Constants.ASKS)) {
                setDepthVolumeMethod = depthClass.getMethod("setAsk_volume" + depth, Class.forName("java.lang.String"));
                setDepthPriceMethod = depthClass.getMethod("setAsk_price" + depth, Class.forName("java.lang.String"));
            }
            if (type.equalsIgnoreCase(Constants.BIDS)) {
                setDepthVolumVolumMethod = depthVolumeClass.getMethod("setBid_volume" + depth, Class.forName("java.lang.String"));
            } else if (type.equalsIgnoreCase(Constants.ASKS)) {
                setDepthVolumVolumMethod = depthVolumeClass.getMethod("setAsk_volume" + depth, Class.forName("java.lang.String"));
            }
            BigDecimal volumeAggregation = new BigDecimal("0");
            BigDecimal sortedPrice = sortedPriceList.get(i);
            for (ExchangeDepth exchangeDepth : exchangeDepthList) {
                for (int j = 1; j <= Constants.DEPTH; j++) {
                    //遍历exchangeDepth中所有数据，查找sortedPrice
                    Method getPriceMethod = null;
                    if (type.equalsIgnoreCase(Constants.BIDS)) {
                        getPriceMethod = exchangeDepthClass.getMethod("getBid_price" + j);
                    } else if (type.equalsIgnoreCase(Constants.ASKS)) {
                        getPriceMethod = exchangeDepthClass.getMethod("getAsk_price" + j);
                    }
                    Method getVolumeMethod = null;
                    if (type.equalsIgnoreCase(Constants.BIDS)) {
                        getVolumeMethod = exchangeDepthClass.getMethod("getBid_volume" + j);
                    } else if (type.equalsIgnoreCase(Constants.ASKS)) {
                        getVolumeMethod = exchangeDepthClass.getMethod("getAsk_volume" + j);
                    }
                    if (null != getPriceMethod) {
                        BigDecimal price = new BigDecimal((String) getPriceMethod.invoke(exchangeDepth));
                        if (null != price && price.equals(sortedPrice)) {
                            BigDecimal volume = new BigDecimal((String) getVolumeMethod.invoke(exchangeDepth));
                            if (null != volume) {
                                String exchangeType = exchangeDepth.getExchange_type();
                                DepthVolume depthVolume = depthVolumeMap.get(exchangeType);
                                if (null == depthVolume) {
                                    depthVolume = new DepthVolume();
                                }
                                setDepthVolumVolumMethod.invoke(depthVolume, volume.stripTrailingZeros().toPlainString());
                                depthVolumeMap.put(exchangeType, depthVolume);
                                volumeAggregation = volumeAggregation.add(volume);
                            }
                            break;
                        }
                    }
                }
            }
            if (null != setDepthVolumeMethod) {
                setDepthVolumeMethod.invoke(depthAggregation, volumeAggregation.stripTrailingZeros().toPlainString());
            }
            if (null != setDepthPriceMethod) {
                setDepthPriceMethod.invoke(depthAggregation, sortedPrice.stripTrailingZeros().toPlainString());
            }
        }
    }

    /**
     * 设置bids asks 5档行情数据
     *
     * @param jsonArray
     * @param exchangeDepth
     * @param exchangeDepthClass
     * @param type
     */
    public void setDepthBidsAsks(JSONArray jsonArray, ExchangeDepth exchangeDepth, Class exchangeDepthClass, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {

        for (int i = 0; i < jsonArray.size(); i++) {
            Method setBidPriceMethod = exchangeDepthClass.getMethod("setBid_price" + (i + 1), Class.forName("java.lang.String"));
            Method setBidVolumeMethod = exchangeDepthClass.getMethod("setBid_volume" + (i + 1), Class.forName("java.lang.String"));
            Method setAskPriceMethod = exchangeDepthClass.getMethod("setAsk_price" + (i + 1), Class.forName("java.lang.String"));
            Method setAskVolumeMethod = exchangeDepthClass.getMethod("setAsk_volume" + (i + 1), Class.forName("java.lang.String"));
            JSONArray bidPriceAmount = jsonArray.getJSONArray(i);
            String price = bidPriceAmount.getString(0);
            String volume = bidPriceAmount.getString(1);
            if (type.equalsIgnoreCase(Constants.BIDS)) {
                setBidPriceMethod.invoke(exchangeDepth, price);
                setBidVolumeMethod.invoke(exchangeDepth, volume);
            } else if (type.equalsIgnoreCase(Constants.ASKS)) {
                setAskPriceMethod.invoke(exchangeDepth, price);
                setAskVolumeMethod.invoke(exchangeDepth, volume);
            }
        }
    }


    /**
     * 从depth原始数据中获取asks并设置
     *
     * @param exchangeDepth
     * @param depthObject
     * @return
     */
    public void setExchangeDepthAsks(ExchangeDepth exchangeDepth, Class exchangeDepthClass, JSONObject depthObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        JSONArray askArrays = depthObject.getJSONArray(Constants.ASKS);
        int toIndex = askArrays.size() >= Constants.DEPTH ? Constants.DEPTH : askArrays.size();
        for (int i = 0; i < toIndex; i++) {
            Method setAskPriceMethod = exchangeDepthClass.getMethod("setAsk_price" + (i + 1), Class.forName("java.lang.String"));
            Method setAskVolumeMethod = exchangeDepthClass.getMethod("setAsk_volume" + (i + 1), Class.forName("java.lang.String"));
            JSONArray askArray = askArrays.getJSONArray(i);
            if (askArray.size() >= 2) {
                String price = askArray.getString(0);
                String volume = askArray.getString(1);
                setAskPriceMethod.invoke(exchangeDepth, price);
                setAskVolumeMethod.invoke(exchangeDepth, volume);
            } else {
                logger.error(Constants.Error.RESPONSE_FORMAT_ERROR[0], Constants.Error.RESPONSE_FORMAT_ERROR[1] + "[asks字段值数据格式错误：数组长度不为2，无法获取price和volume]");
            }
        }

    }

    /**
     * 价格和数量小数位数格式化为10位，没有补0，有则截取并四舍五入
     *
     * @param str
     * @return
     */
    public static String formPriceAmount(String str) {
        if (null == str) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("0.0000000000");
        BigDecimal bd = new BigDecimal(str);
        str = df.format(bd);
        return str;
    }

    /**
     * 将null值转换成空字符串
     *
     * @param str
     * @return
     */
    public static String formNullStr(String str) {
        if (null == str) {
            str = "";
        }
        return str;
    }

    /**
     * 火币时间戳转换成毫秒
     *
     * @param timestamp
     * @return
     */
    public String formTimestamp(String timestamp) {
        StringBuilder sb = new StringBuilder();
        if (timestamp.length() == 10) {
            sb.append(timestamp);
            sb.append("999");
            return sb.toString();
        }
        return timestamp;
    }


    /**
     * 正则表达式匹配
     *
     * @param str
     * @param regEx
     * @return
     */
    public boolean isRegExMatch(String str, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    // buffer 转String
    public String byteBufferToString(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            Charset charset = Charset.forName(Constants.HuoBi.WEBSOCKET_MSG_ENCODING);
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            buffer.flip();
            return charBuffer.toString();
        } catch (Exception e) {
            logger.error("buffer转String失败", e);
            return null;
        }
    }

    // 压缩
    public String compress(String str) throws IOException {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString(Constants.HuoBi.WEBSOCKET_MSG_ENCODING);
    }

    // 解压缩
    public String uncompress(String str) throws IOException {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(Constants.HuoBi.WEBSOCKET_MSG_ENCODING));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }
}
