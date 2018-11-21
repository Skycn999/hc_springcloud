package com.mi.hundsun.oxchains.base.generate;

import com.mi.hundsun.oxchains.base.generate.bean.CodeGenerateFactory;
import com.mi.hundsun.oxchains.base.generate.util.GenerateUtils;

import java.util.HashMap;
import java.util.Map;

public class Gener {
    public final static String URL = "jdbc:mysql://119.23.57.169:3306/oxchains?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    public final static String USERNAME = "oxchains";
    public final static String PASSWORD = "oxchains123";
    public final static String DATABASE_NAME = "oxchains";

    public final static String fileDirPath = System.getProperty("user.dir") + "\\_generate\\src\\main\\java\\code-packge\\";

    // @RequestMapping("/${modelName}")

    // 模块名称controller注解
    public final static String baseMapperPath = "com.mi.hundsun.oxchains.base.core.dao.mapper";
    public final static String baseDomainPath = "com.mi.hundsun.oxchains.base.core.master.mapper.po";
    public final static String baseServicePath = "com.mi.hundsun.oxchains.base.core.service.BaseService";
    public final static String baseServiceImplPath = "com.mi.hundsun.oxchains.base.core.service.BaseServiceImpl";

    public final static String KEY_TYPE_01 = "01";
    public final static String KEY_TYPE_02 = "02";

    public final static String c_user = "bin";
    public final static String c_date = GenerateUtils.getCurrTimeStr();
    public final static String c_info = "";

    public final static String[] def_feilds = new String[]{"id", "createTime", "uuid", "delFlag", "updateTime", "creator", "updator"};
    public final static String PREFIX = "tpl_";// fixme 生成类名忽略的前缀，业务相关 <------------------------------- 根据生成对象修改
    public final static String modelName = "tpl";// fixme 包名  <------------------------------- 根据生成对象修改

    public final static String corePath = "com.mi.hundsun.oxchains.base.core.";
    public final static String domMainPath = "com.mi.hundsun.oxchains.base.core.master.mapper.po." + modelName;
    public final static String mapperPath = "com.mi.hundsun.oxchains.base.core.master.mapper." + modelName;
    public final static String servicePath = "com.mi.hundsun.oxchains.base.core.service." + modelName;

    public static void main(String[] args) {
        /* 此处修改成你的 表名 和 中文注释 ***/

        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("tpl_trade_fee", "交易手续费率模板");
        for (Map.Entry<String, String> map : tableMap.entrySet()) {
            CodeGenerateFactory.codeGenerate(map.getKey(), map.getValue(), KEY_TYPE_01);
        }
    }


}
