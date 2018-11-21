package com.mi.hundsun.oxchains.base.util;

import com.mi.hundsun.oxchains.base.Gener;
import com.mi.hundsun.oxchains.base.bean.ColumnData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoMainUtils {
    private static boolean isEnum(String comment, String name) {
//        if ("STATUS".equals(name.toUpperCase())) {
//            return false;
//        }
        if (GenerateUtils.isEmpty(comment)) {
            return false;
        }
        comment = comment.trim();
        if (!comment.contains(",")) {
            return false;
        }
        if (!comment.contains(":")) {
            return false;
        }
        return true;
    }

    /**
     * @param comment 注释
     * @param name    字段名
     * @return 生成枚举类型
     */
    private static String getEnum(String comment, String name) {
        if (isEnum(comment, name)) {
            StringBuffer column_enum = new StringBuffer();
            column_enum.append("\r\t/**").append(comment.replaceAll(";", "<br>") + "**/").append("\r\t")
                    .append("public enum ").append(name.toUpperCase()).append(" {\r\t");
            String[] comments = comment.split(";");
            if (comments.length == 0) {
                return "";
            }
            int count = 0;
            for (String column_comment : comments) {
                ++count;
                String[] code_column_comment = column_comment.split(",");
                if (code_column_comment.length == 0) {
                    return "";
                }
                int code = Integer.valueOf(code_column_comment[0]);
                String[] comment_value = code_column_comment[1].split(":");
                if (comment_value.length == 0) {
                    return "";
                }
                String value_name = comment_value[0];
                String value = comment_value[1];
                column_enum.append("\r\t\t/**").append(column_comment + "**/").append("\r\t");
                column_enum.append("\t").append(value.toUpperCase()).append("(\"" + value_name + "\"").append(",")
                        .append(code).append(")");
                if (comments.length == count) {
                    column_enum.append(";\r\t");
                } else {
                    column_enum.append(",\r\t");
                }
            }
            column_enum.append("\r\t\t").append("public final int code;");
            column_enum.append("\r\t\t").append("public final String value;");
            column_enum.append("\r\t\t")
                    .append("private static Map<Integer, String> map = new HashMap<Integer, String>();\r\t");
            column_enum.append("\r\t\t").append("private " + name.toUpperCase() + "(String value, int code) {\r\t\t\t")
                    .append("this.code = code;\r\t\t\t").append("this.value = value;\r\t\t").append("}\r\t");
            column_enum.append("\r\t\t").append("public static String getValue(Integer code) {\r\t\t\t")
                    .append("if (null == code) {\r\t\t\t\t").append("return null;\r\t\t\t}\r\t\t\t")
                    .append("for (" + name.toUpperCase() + " " + name.toLowerCase() + " : " + name.toUpperCase()
                            + ".values()) {\r\t\t\t\t")
                    .append("if (" + name.toLowerCase() + ".code == code) { \r\t\t\t\t\t")
                    .append("return " + name.toLowerCase() + ".value;\r\t\t\t\t}\r\t\t\t}\r\t\t\t")
                    .append("return null;\r\t\t}\r\t");

            column_enum.append("\r\t\t").append("public static Integer getCode(String value) {\r\t\t\t")
                    .append("if (null == value  || \"\".equals(value)) {\r\t\t\t\t\t")
                    .append("return null;\r\t\t\t}\r\t\t\t")
                    .append("for (" + name.toUpperCase() + " " + name.toLowerCase() + " : " + name.toUpperCase()
                            + ".values()) {\r\t\t\t\t")
                    .append("if (" + name.toLowerCase() + ".value.equals(value)) { \r\t\t\t\t\t")
                    .append("return " + name.toLowerCase() + ".code;\r\t\t\t\t}\r\t\t\t}\r\t\t\t")
                    .append("return null;\r\t\t}\r\t");

            column_enum.append("\r\t\t").append("public static  Map<Integer, String> getEnumMap() {").append("\r\t\t\t")
                    .append("if(map.size() == 0){").append("\r\t\t\t\t")
                    .append("for (" + name.toUpperCase() + " " + name.toLowerCase() + " : " + name.toUpperCase()
                            + ".values()) {")
                    .append("\r\t\t\t\t\t")
                    .append("map.put(" + name.toLowerCase() + ".code" + "," + name.toLowerCase() + ".value);")
                    .append("\r\t\t\t\t}").append("\r\t\t\t}").append("\r\t\t\t").append("return map;")
                    .append("\r\t\t}");
            column_enum.append("\r\t").append("}\r\t");
            return column_enum.toString();
        }
        return "";
    }

    /**
     * @param comment
     * @param name
     * @return 获取枚举类型字段
     */
    private static String getEnumFeilds(String comment, String name) {
        if (isEnum(comment, name)) {
            String nameFormatter = name + "Formatter";
            StringBuffer formater = new StringBuffer();
            formater.append("\n\t@Transient\r\t").append("private String ").append(nameFormatter + " ;");
            return formater.toString();
        }
        return "";
    }

    /**
     * @param comment
     * @param name
     * @return 格式化枚举类型的getSet方法
     */
    private static String getEnumFormatter(String comment, String name) {
        if (isEnum(comment, name)) {
            StringBuffer formatter = new StringBuffer();
            String nameFormatter = name + "Formatter";
            String maxChar = nameFormatter.substring(0, 1).toUpperCase();
            String method = Utils.underlineToCamel2(maxChar + nameFormatter.substring(1, nameFormatter.length()));
            String name_ = Utils.underlineToCamel2(maxChar + name.substring(1, name.length()));
            formatter.append("\r\t").append("public String ").append("get" + method + "() {\r\t");
            formatter.append("    if(null == " + nameFormatter + " || \"\".equals(" + nameFormatter + ")" + "){\r\t");
            formatter.append("\t")
                    .append("    return " + name.toUpperCase() + ".getValue(" + "get" + name_ + "());\r\t\t}\r\t");
            formatter.append("    return this.").append(nameFormatter).append(";\r\t}");
            formatter.append("\r\t").append("public void ")
                    .append("set" + method + "(" + "String " + nameFormatter + ") {\r\t");
            formatter.append("    this." + nameFormatter + "=").append(nameFormatter).append(";\r\t}");
            return formatter.toString();
        }
        return "";
    }

    private static boolean isDefFeilds(String feilds) {
        for (String def : Gener.def_feilds) {
            if (def.equalsIgnoreCase(feilds)) {
                return true;
            }
        }
        return false;
    }

    public static String getBeanFeilds(String tableName) throws SQLException {
        List<ColumnData> dataList = TableUtils.getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        StringBuffer column_enum = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = Utils.underlineToCamel2(d.getColumnName());
            if (isDefFeilds(name)) {
                continue;
            }
            String type = d.getDataType();
            String comment = d.getColumnComment();

            String maxChar = name.substring(0, 1).toUpperCase();
            str.append("\r\t/**").append(comment + "**/");
            str.append("\r\t@ApiModelProperty(value = \"").append(comment + "\")");
            if ("Date".equals(type)) {
                str.append("\r\t").append("@JSONField(format=\"yyyy-MM-dd HH:mm:ss\")");
            }
            str.append("\r\t").append("private ").append(type + " ").append(name + ";");
            str.append(getEnumFeilds(comment, name));
//			String method = Utils.underlineToCamel2(maxChar + name.substring(1, name.length()));
//			getset.append("\r\t").append("public ").append(type + " ").append("get" + method + "() {\r\t");
//			getset.append("    return this.").append(name).append(";\r\t}");
//			getset.append("\r\t").append("public void ").append("set" + method + "(" + type + " " + name + ") {\r\t");
//			getset.append("    this." + name + "=").append(name).append(";\r\t}");
            getset.append(getEnumFormatter(comment, name));

            column_enum.append(getEnum(comment, name));
        }
        return str.toString() + getset.toString() + column_enum.toString();
    }

    public static String getImportPakges(String tableName) throws SQLException {
        List<ColumnData> dataList = TableUtils.getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        Set<String> importSet = new HashSet<>();
        for (ColumnData d : dataList) {
            if (GenerateUtils.isNotEmpty(d.getImportPackge())) {
                importSet.add(d.getImportPackge());
                if ("java.util.Date;".equals(d.getImportPackge())) {
                    // 时间格式化导入
                    importSet.add("com.alibaba.fastjson.annotation.JSONField;");
                }
            }
            // 枚举类型导入map
            if (isEnum(d.getColumnComment(), d.getColumnName())) {
                importSet.add("java.util.HashMap;");
                importSet.add("java.util.Map;");
            }
        }
        if (importSet.isEmpty()) {
            return "";
        }
        for (String string : importSet) {
            str.append("import  " + string + "\n");
        }
        return str.toString();
    }
}
