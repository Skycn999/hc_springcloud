package com.mi.hundsun.oxchains.base.core.model.quote.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xmf
 * Date: 2018/3/6
 **/
public class Constants {
    public static final String HUOBI = "huobi";
    public static final String BIAN = "bian";
    public static final String CHARSET = "UTF-8";
    public static final String HUOBI_WEBSOCKET_CHARSET = "ISO-8859-1";
    public static final String BIDS = "bids";
    public static final String ASKS = "asks";
    public static final int DEPTH = 10;
    public static final String KLINE_TYPE_1_DAY = "1day";

    private Constants() {
    }

    public static final List<String> KLINE_TYPES = new ArrayList<String>() {
        private static final long serialVersionUID = 4696003362619172325L;

        {
            add("1min");
            add("5min");
            add("15min");
            add("30min");
            add("60min");
            add("1day");
            add("1week");
            add("1mon");
        }
    };

    public static class WebSocketConnectType {
        private WebSocketConnectType() {
        }

        public static final String KLINE = "kline";
        public static final String TICK = "tick";
        public static final String DEPTH = "depth";
    }

    /**
     * 火币的参数
     */
    public static class HuoBi {
        private HuoBi() {
        }

        public static final String WEBSOCKET_MSG_ENCODING = "ISO-8859-1";
        public static final String RESPONSE_SUCCESS = "ok";
        public static final String RESPONSE_ERROR = "error";
        public static final String WEBSOCKET_MSG_STATUS="status";
        public static final String HUOBI_1MIN_KLINE = "1min";
        public static final String HUOBI_KLINE_REGEX = "^market\\.[a-z]+\\.kline\\..+";
        public static final String HUOBI_TICK_REGEX = "^market\\.[a-z]+\\.trade\\.detail$";
        public static final String HUOBI_DEPTH_REGEX = "^market\\.[a-z]+\\.depth\\.step0$";

        public static final Map<String, String> TICK_BUSINESS_DIRECTION = new HashMap<String, String>() {

            private static final long serialVersionUID = 4696003362619172325L;

            {
                put("buy", "1");
                put("sell", "0");
            }
        };
    }

    /**
     * 币安的参数
     */
    public static class BiAn {
        private BiAn() {
        }

        public static final String STREAM = "stream";
        public static final String BIAN_1MIN_KLINE = "1m";
        public static final String BIAN_KLINE_REGEX = "^[a-z]+@kline_.+$";
        public static final String BIAN_TICK_REGEX = "^[a-z]+@trade$";
        public static final String BIAN_DEPTH_REGEX = "^[a-z]+@depth(5|10|20)$";
        public static final Map<Boolean, String> TICK_BUSINESS_DIRECTION = new HashMap<Boolean, String>() {

            private static final long serialVersionUID = 4696003362619172325L;

            {
                put(false, "1");
                put(true, "0");
            }

        };

        public static final Map<String, String> KLINE_TYPE_ALIAS = new HashMap<String, String>() {

            private static final long serialVersionUID = 13785292604494485L;

            {
                put("1min", "1m");
                put("1m", "1min");
                put("5min", "5m");
                put("5m", "5min");
                put("15min", "15m");
                put("15m", "15min");
                put("30min", "30m");
                put("30m", "30min");
                put("60min", "1h");
                put("1h", "60min");
                put("1day", "1d");
                put("1d", "1day");
                put("1mon", "1M");
                put("1M", "1mon");
                put("1week", "1w");
                put("1w", "1week");
            }
        };
    }

    public static class Error {
        private Error() {
        }

        public static final String HTTP_ERROR = "1000";
        public static final String[] RESPONSE_NULL_ERROR = {"1001", "应答数据为空"};
        public static final String[] NOT_INIT = {"1002", "对象未初始化"};

        public static final String[] NO_REQUIRE_PARAM = {"2000", "必填参数缺失"};
        public static final String[] PARAM_INVALID = {"2001", "参数不合法"};

        public static final String[] RESPONSE_FORMAT_ERROR = {"2002", "应答处理异常"};

        public static final String[] FUNCTION_NOT_ENABLE = {"2003", "该功能未启用"};

        public static final String[] UNKNOW_ERROR = {"2004", "未知异常"};


    }


}
