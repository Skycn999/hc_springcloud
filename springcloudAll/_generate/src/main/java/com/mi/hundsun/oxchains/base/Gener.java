package com.mi.hundsun.oxchains.base;

import com.mi.hundsun.oxchains.base.bean.CodeGenerateFactory;
import com.mi.hundsun.oxchains.base.util.GenerateUtils;

import java.util.HashMap;
import java.util.Map;

public class Gener {
    public final static String URL = "jdbc:mysql://116.62.127.60:3306/oxchains?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "root";
    public final static String DATABASE_NAME = "oxchains_tx";

    public final static String fileDirPath = System.getProperty("user.dir") + "\\_generate\\src\\main\\java\\code-packge\\";

    // @RequestMapping("/${modelName}")

    // 模块名称controller注解
    public final static String baseMapperPath = "com.mi.hundsun.oxchains.base.core.dao.mapper";
    public final static String baseDomainPath = "com.mi.hundsun.oxchains.base.core.po";
    public final static String baseServicePath = "com.mi.hundsun.oxchains.base.core.service.BaseService";
    public final static String baseServiceImplPath = "com.mi.hundsun.oxchains.base.core.service.BaseServiceImpl";

    public final static String KEY_TYPE_01 = "01";
    public final static String KEY_TYPE_02 = "02";

    public final static String c_user = "lzj";
    public final static String c_date = GenerateUtils.getCurrTimeStr();
    public final static String c_info = "";

    public final static String[] def_feilds = new String[]{"id", "createTime", "version", "delFlag", "updateTime", "creator", "updator"};
    public final static String PREFIX = "";// fixme 生成类名忽略的前缀，业务相关 <------------------------------- 根据生成对象修改
    public final static String modelName = "user";// fixme 包名  <------------------------------- 根据生成对象修改

    public final static String corePath = "com.mi.hundsun.oxchains.base.core.";
    public final static String domMainPath = "com.mi.hundsun.oxchains.base.core.po." + modelName;
    public final static String mapperPath = "com.mi.hundsun.oxchains.base.core.mapper." + modelName;
    public final static String servicePath = "com.mi.hundsun.oxchains.base.core.service." + modelName;

    public static void main(String[] args) {
        /* 此处修改成你的 表名 和 中文注释 ***/

        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("user_freeze", "冻结功能表");
        tableMap.put("user_address", "用户提币地址表");
        tableMap.put("user_attachment", "用户附属信息表");
        tableMap.put("user_in_letter", "用户站内信表");
        for (Map.Entry<String, String> map : tableMap.entrySet()) {
            CodeGenerateFactory.codeGenerate(map.getKey(), map.getValue(), KEY_TYPE_01);
        }
    }


}
