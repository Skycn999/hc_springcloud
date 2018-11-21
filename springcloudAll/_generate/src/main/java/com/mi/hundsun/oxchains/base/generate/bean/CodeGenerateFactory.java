package com.mi.hundsun.oxchains.base.generate.bean;

import com.mi.hundsun.oxchains.base.generate.Gener;
import com.mi.hundsun.oxchains.base.generate.util.DoMainUtils;
import com.mi.hundsun.oxchains.base.generate.util.PageParserUtils;
import com.mi.hundsun.oxchains.base.generate.util.SqlUtils;
import com.mi.hundsun.oxchains.base.generate.util.TableUtils;
import org.apache.velocity.VelocityContext;

import java.util.Date;
import java.util.Map;

public class CodeGenerateFactory {
    private static String url = Gener.URL;
    private static String username = Gener.USERNAME;
    private static String passWord = Gener.PASSWORD;

    /**
     * @param tableName：表名
     * @param codeName：注释
     * @param keyType：主键生成方式 01:UUID 02:自增
     */
    public static void codeGenerate(String tableName, String codeName, String keyType) {
        TableUtils.initDatabase(url, username, passWord);
        String className = TableUtils.getTablesNameToClassName(tableName, Gener.PREFIX);
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());

        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("codeName", codeName);
        context.put("tableName", tableName);

        context.put("baseDomainPath", Gener.baseDomainPath);
        context.put("domMainPath", Gener.domMainPath);
        context.put("baseMapperPath", Gener.baseMapperPath);
        context.put("mapperPath", Gener.mapperPath);
        context.put("baseServicePath", Gener.baseServicePath);
        context.put("baseServiceImplPath", Gener.baseServiceImplPath);
        context.put("servicePath", Gener.servicePath);
//		context.put("baseContrPath", Gener.baseContrPath);
//		context.put("contrPath", Gener.contrPath);
        context.put("corePath", Gener.corePath);
        context.put("modelName", Gener.modelName);

        context.put("c_user", Gener.c_user);
        context.put("c_date", Gener.c_date);
        context.put("c_info", Gener.c_info);

        context.put("keyType", keyType);
        context.put("user", System.getProperty("user.name"));
        context.put("time", new Date());
        context.put("organization", "");

        try {
            context.put("inportPakges", DoMainUtils.getImportPakges(tableName));
            context.put("feilds", DoMainUtils.getBeanFeilds(tableName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Map<String, Object> sqlMap = SqlUtils.getAutoCreateSql(tableName);
            context.put("columnDatas", TableUtils.getColumnDatas(tableName));
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        PageParserUtils.WriterPage(context, "EntityTemplate.ftl", Gener.fileDirPath + "po\\", className + ".java");
        PageParserUtils.WriterPage(context, "DaoTemplate.ftl", Gener.fileDirPath + "mapper\\", className + "Mapper.java");
        PageParserUtils.WriterPage(context, "ServiceTemplate.ftl", Gener.fileDirPath + "service\\",
                className + "Service.java");
        PageParserUtils.WriterPage(context, "ServiceImplTemplate.ftl", Gener.fileDirPath + "service\\impl\\",
                className + "ServiceImpl.java");
//		PageParserUtils.WriterPage(context, "MapperTemplate.xml", Gener.fileDirPath + "mppaer\\",
//				lowerName + "Mapper.xml");
//		PageParserUtils.WriterPage(context, "ControllerTemplate.ftl", Gener.fileDirPath + "cotroller\\",
//				className + "Controller.java");
//		PageParserUtils.WriterPage(context, "ServiceJunitTemplate.ftl", Gener.fileDirPath + "junit\\",
//				className + "ServiceTest.java");
        // log.info("----------------------------代码生成完毕---------------------------");
    }

}