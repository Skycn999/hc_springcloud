package com.mi.hundsun.oxchains.base.core.model.quote.utils;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * Created by xmf
 * Date: 2018/3/8
 **/
@Component(value = "injectParams")
@ConfigurationProperties(prefix = "params")
public class InjectParams implements Serializable {

    private static final long serialVersionUID = 8609222518482447545L;
    /**
     * 火币http请求地址前半段
     */
    private String huobiHttpUrl;
    /**
     * 火币http K线请求地址后半段
     */
    private String huobiHttpKline;

    /**
     * 火币http 分时请求地址后半段
     */
    private String huobiHttpTick;

    /**
     * 币安http请求地址前半段
     */
    private String bianHttpUrl;

    /**
     * 币安http K线请求地址后半段
     */
    private String bianHttpKline;

    /**
     * 币安http 分时请求地址后半段
     */
    private String bianHttpTick;

    /***
     * K线数据初始化条数
     */
    private int initKlineSize;

    /**
     * 分笔数据初始化条数
     */
    private int initTickSize;

    /**
     * websocket连接url中的交易对列表
     */
    private List<String> symbolList;
    /**
     * 交易所和K线类型的对应关系，币安需要做转换
     */
    private Map<String, List<String>> exchangeKlinePeriod;
    /**
     * websocket连接中k线的时间间隔
     */
    private List<String> klinePeriodList;
    /**
     * websocket连接超时时间
     */
    private long wssTimeOut;
    /**
     * websocket重连时间间隔
     */
    private long reConTime;
    /**
     * websocket连接交易所类型，用来判断如何处理数据
     */
    private String exchangeType;
    /**
     * 火币网websocket连接地址
     */
    private String huoBiWebSocketUrl;

    /**
     * 币安websocket连接地址
     */
    private String biAnWebSocketUrl;

    /**
     * http请求初始化数据重试次数
     */
    private int httpInitRetryTime;
    /**
     * http请求初始化数据重试等待时间
     */
    private long httpInitRetryInterval;

    /**
     * websocket订阅重试次数
     */
    private int websocketSubRetryTime;
    /**
     * websocket订阅重试等待时间
     */
    private long websocketSubRetryInterval;

    /**
     * 默认K线条数
     */
    private int defaultReqKlineSize;

    /**
     * 是否启用除了depth以外的功能
     */
    private boolean enableAllExceptDepth;

    /**
     * 是否启用depth功能
     */
    private boolean enableDepth;

    /**
     * 是否手动释放redis连接
     */
    private boolean enableReleaseRedisConnection;

    /**
     * 从连接池中获取连接的timeout时间
     */
    private int connectTimeout;

    /**
     * 建立socket连接超时时间
     */
    private int socketTimeout;
    /**
     * 发送请求响应时间
     */
    private int requestTimeout;

    /**
     * 配置httpclient每个路由最大连接数 访问每个目标机器 算一个路由
     */
    private int maxRouteConnections;
    /**
     * 配置httpclient最大连接数
     */
    private int maxTotalConnections;

    /**
     * 一条websocket连接最大订阅条数
     */
    private int onceWebSocketSubSize;

    /**
     * http请求间隔，用于有流量限制的交易所
     */
    private long httpRequestInterval;

    public String getHuobiHttpUrl() {
        return huobiHttpUrl;
    }

    public void setHuobiHttpUrl(String huobiHttpUrl) {
        this.huobiHttpUrl = huobiHttpUrl;
    }

    public String getHuobiHttpKline() {
        return huobiHttpKline;
    }

    public void setHuobiHttpKline(String huobiHttpKline) {
        this.huobiHttpKline = huobiHttpKline;
    }

    public String getHuobiHttpTick() {
        return huobiHttpTick;
    }

    public void setHuobiHttpTick(String huobiHttpTick) {
        this.huobiHttpTick = huobiHttpTick;
    }

    public String getBianHttpUrl() {
        return bianHttpUrl;
    }

    public void setBianHttpUrl(String bianHttpUrl) {
        this.bianHttpUrl = bianHttpUrl;
    }

    public String getBianHttpKline() {
        return bianHttpKline;
    }

    public void setBianHttpKline(String bianHttpKline) {
        this.bianHttpKline = bianHttpKline;
    }

    public String getBianHttpTick() {
        return bianHttpTick;
    }

    public void setBianHttpTick(String bianHttpTick) {
        this.bianHttpTick = bianHttpTick;
    }

    public int getInitKlineSize() {
        return initKlineSize;
    }

    public void setInitKlineSize(int initKlineSize) {
        this.initKlineSize = initKlineSize;
    }

    public int getInitTickSize() {
        return initTickSize;
    }

    public void setInitTickSize(int initTickSize) {
        this.initTickSize = initTickSize;
    }

    public List<String> getSymbolList() {
        return symbolList;
    }

    public void setSymbolList(List<String> symbolList) {
        this.symbolList = symbolList;
    }

    public Map<String, List<String>> getExchangeKlinePeriod() {
        return exchangeKlinePeriod;
    }

    public void setExchangeKlinePeriod(Map<String, List<String>> exchangeKlinePeriod) {
        this.exchangeKlinePeriod = exchangeKlinePeriod;
        Set<String> keySet = exchangeKlinePeriod.keySet();
        int exchangeTypeSize = keySet.size();
        if (exchangeTypeSize != 1) {
            throw new BussinessException(Constants.Error.PARAM_INVALID[0], Constants.Error.PARAM_INVALID[1] + "[exchangeKlinePeriod只能有一个键值对]");
        }
        Iterator<String> it = keySet.iterator();
        String exchangeType = it.next();
        this.exchangeType = exchangeType;
        List<String> originalPeriodList = exchangeKlinePeriod.get(exchangeType);
        this.klinePeriodList = originalPeriodList;
    }

    public List<String> getKlinePeriodList() {
        return klinePeriodList;
    }

    public long getWssTimeOut() {
        return wssTimeOut;
    }

    public void setWssTimeOut(long wssTimeOut) {
        this.wssTimeOut = wssTimeOut;
    }

    public long getReConTime() {
        return reConTime;
    }

    public void setReConTime(long reConTime) {
        this.reConTime = reConTime;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public String getHuoBiWebSocketUrl() {
        return huoBiWebSocketUrl;
    }

    public void setHuoBiWebSocketUrl(String huoBiWebSocketUrl) {
        this.huoBiWebSocketUrl = huoBiWebSocketUrl;
    }

    public String getBiAnWebSocketUrl() {
        return biAnWebSocketUrl;
    }

    public void setBiAnWebSocketUrl(String biAnWebSocketUrl) {
        this.biAnWebSocketUrl = biAnWebSocketUrl;
    }

    public int getHttpInitRetryTime() {
        return httpInitRetryTime;
    }

    public void setHttpInitRetryTime(int httpInitRetryTime) {
        this.httpInitRetryTime = httpInitRetryTime;
    }

    public long getHttpInitRetryInterval() {
        return httpInitRetryInterval;
    }

    public void setHttpInitRetryInterval(long httpInitRetryInterval) {
        this.httpInitRetryInterval = httpInitRetryInterval;
    }


    public int getWebsocketSubRetryTime() {
        return websocketSubRetryTime;
    }

    public void setWebsocketSubRetryTime(int websocketSubRetryTime) {
        this.websocketSubRetryTime = websocketSubRetryTime;
    }

    public long getWebsocketSubRetryInterval() {
        return websocketSubRetryInterval;
    }

    public void setWebsocketSubRetryInterval(long websocketSubRetryInterval) {
        this.websocketSubRetryInterval = websocketSubRetryInterval;
    }

    public int getDefaultReqKlineSize() {
        return defaultReqKlineSize;
    }

    public void setDefaultReqKlineSize(int defaultReqKlineSize) {
        this.defaultReqKlineSize = defaultReqKlineSize;
    }

    public boolean isEnableAllExceptDepth() {
        return enableAllExceptDepth;
    }

    public void setEnableAllExceptDepth(boolean enableAllExceptDepth) {
        this.enableAllExceptDepth = enableAllExceptDepth;
    }

    public boolean isEnableDepth() {
        return enableDepth;
    }

    public void setEnableDepth(boolean enableDepth) {
        this.enableDepth = enableDepth;
    }

    public boolean isEnableReleaseRedisConnection() {
        return enableReleaseRedisConnection;
    }

    public void setEnableReleaseRedisConnection(boolean enableReleaseRedisConnection) {
        this.enableReleaseRedisConnection = enableReleaseRedisConnection;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getMaxRouteConnections() {
        return maxRouteConnections;
    }

    public void setMaxRouteConnections(int maxRouteConnections) {
        this.maxRouteConnections = maxRouteConnections;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getOnceWebSocketSubSize() {
        return onceWebSocketSubSize;
    }

    public void setOnceWebSocketSubSize(int onceWebSocketSubSize) {
        this.onceWebSocketSubSize = onceWebSocketSubSize;
    }

    public long getHttpRequestInterval() {
        return httpRequestInterval;
    }

    public void setHttpRequestInterval(long httpRequestInterval) {
        this.httpRequestInterval = httpRequestInterval;
    }
}
