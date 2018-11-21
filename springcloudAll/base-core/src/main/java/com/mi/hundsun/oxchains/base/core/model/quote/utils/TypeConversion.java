package com.mi.hundsun.oxchains.base.core.model.quote.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by xmf on 2017/3/8.
 */
public class TypeConversion {
    private TypeConversion(){}
    private static Logger logger = LoggerFactory.getLogger(TypeConversion.class);

    public static String byteBufferToString(ByteBuffer buffer) {
        String result = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            result = charBuffer.toString();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
        return result;
    }
}
