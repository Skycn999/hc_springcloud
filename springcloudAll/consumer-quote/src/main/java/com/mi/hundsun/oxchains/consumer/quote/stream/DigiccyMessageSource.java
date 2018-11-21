package com.mi.hundsun.oxchains.consumer.quote.stream;

public interface DigiccyMessageSource {
    /**
     * bitfinex分笔主题
     */
    String TICK_OUTPUT_BITFINEX = "digiccy-hq-tick-bitfinex";
    /**
     * okex分笔主题
     */
    String TICK_OUTPUT_OKEX = "digiccy-hq-tick-okex";
    /**
     * bian分笔主题
     */
    String TICK_OUTPUT_BIAN = "digiccy-hq-tick-bian";
    /**
     * huobi分笔主题
     */
    String TICK_OUTPUT_HUOBI = "digiccy-hq-tick-huobi";

    /**
     * bitfinex档位主题
     */
    String DEPTH_OUTPUT_BITFINEX = "digiccy-hq-depth-bitfinex";
    /**
     * okex档位主题
     */
    String DEPTH_OUTPUT_OKEX = "digiccy-hq-depth-okex";
    /**
     * bian档位主题
     */
    String DEPTH_OUTPUT_BIAN = "digiccy-hq-depth-bian";
    String DEPTH_OUTPUT_BIAN_QUEUE = "digiccy-hq-depth-bian.anonymous.YgvM6U0fSFe5ljA3TLEFmA";
    /**
     * huobi档位主题
     */
    String DEPTH_OUTPUT_HUOBI = "digiccy-hq-depth-huobi";

    /**
     * 聚合档位主题
     */
    String AGG_DEPTH_OUTPUT = "digiccy-hq-agg-depth";
}
