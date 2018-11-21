package com.mi.hundsun.oxchains.base.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Description: //TODO
 * Created by 陌上人 on 2017/1/17 15:18.
 */
@Slf4j
public class LogUtil {

    private static Class clazz = LogUtil.class;

    public static void error(Exception e,Class clazz){
        LogUtil.clazz = clazz;
        StackTraceElement element = e.getStackTrace()[0];
        LinkedList<Object> args = new LinkedList<>();
        args.add(e.getMessage());
        args.add(element.getLineNumber());
        args.add(element.getMethodName());
        args.add(element.getClassName());
        log.error("Bussiness Exception : {} ; LineNumber : {} ; MethodName : {} ; ClassName : {} ", args.toArray());
    }

    public static void info(Exception e,Class clazz){
        LogUtil.clazz = clazz;
        StackTraceElement element = e.getStackTrace()[0];
        LinkedList<Object> args = new LinkedList<>();
        args.add(e.getMessage());
        args.add(element.getLineNumber());
        args.add(element.getMethodName());
        args.add(element.getClassName());
        log.info("Bussiness Exception : {} ; LineNumber : {} ; MethodName : {} ; ClassName : {} ", args.toArray());
    }

    public static void debug(Exception e,Class clazz){
        LogUtil.clazz = clazz;
        StackTraceElement element = e.getStackTrace()[0];
        LinkedList<Object> args = new LinkedList<>();
        args.add(e.getMessage());
        args.add(element.getLineNumber());
        args.add(element.getMethodName());
        args.add(element.getClassName());
        log.info("Bussiness Exception : {} ; LineNumber : {} ; MethodName : {} ; ClassName : {} ", args.toArray());
    }
}
