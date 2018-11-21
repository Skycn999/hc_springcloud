package com.mi.hundsun.oxchains.base.generate.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mi.hundsun.oxchains.base.generate.Gener;
import com.mi.hundsun.oxchains.base.generate.bean.ColumnData;

/**
 * 
 * @author liugang
 * @date 2016年6月7日 上午11:26:07
 * @ClassName: TableUtils
 * @Description: 数据表操作工具类
 */
public class TableUtils {
	String SQLTables = "show tables";

	static String url;
	static String username;
	static String password;
	static String selectStr;
	static String from;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		selectStr = "select ";
		from = " from ";
	}

	public static void initDatabase(String url, String username, String password) {
		TableUtils.url = url;
		TableUtils.username = username;
		TableUtils.password = password;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public List<String> getTables() throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(this.SQLTables);
		ResultSet rs = ps.executeQuery();
		List<String> list = new ArrayList<>();
		while (rs.next()) {
			String tableName = rs.getString(1);
			list.add(tableName);
		}
		rs.close();
		ps.close();
		con.close();
		return list;
	}

	/**
	 * 获取字段类型
	 * 
	 * @param dataType
	 * @param precision
	 * @param scale
	 * @return
	 */
	public static String getType(String dataType, String precision, String scale) {
		dataType = dataType.toLowerCase();
		if (dataType.contains("char"))
			dataType = "String";
		else if (dataType.contains("int"))
			dataType = "Integer";
		else if (dataType.contains("float"))
			dataType = "Float";
		else if (dataType.contains("double"))
			dataType = "Double";
		else if (dataType.contains("number")) {
			if ((GenerateUtils.isNotEmpty(scale)) && (Integer.parseInt(scale) > 0)) {
				dataType = "BigDecimal";
			} else if ((GenerateUtils.isNotEmpty(precision)) && (Integer.parseInt(precision) > 6))
				dataType = "Long";
			else
				dataType = "Integer";
		} else if (dataType.contains("decimal")) {
			dataType = "BigDecimal";
		} else if (dataType.contains("date")) {
			dataType = "Date";
		} else if (dataType.contains("time")) {
			dataType = "Timestamp";
		} else if (dataType.contains("clob")) {
			dataType = "Clob";
		} else {
			dataType = "Object";
		}
		return dataType;
	}

	public static List<ColumnData> getColumnDatas(String tableName) throws SQLException {
		String SQLColumns = "select column_name ,data_type,column_comment,0,0,character_maximum_length,is_nullable nullable from information_schema.columns where table_name =  '"
				+ tableName + "' " + "and table_schema =  '" + Gener.DATABASE_NAME + "'";

		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(SQLColumns);
		List<ColumnData> columnList = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1).toLowerCase();
			String type = rs.getString(2);
			String comment = rs.getString(3);
			String precision = rs.getString(4);
			String scale = rs.getString(5);
			String charmaxLength = rs.getString(6) == null ? "" : rs.getString(6);
			String nullable = Utils.getNullAble(rs.getString(7));
			type = getType(type, precision, scale);

			ColumnData cd = new ColumnData();
			cd.setColumnName(name);
			cd.setColumnNameFormat(Utils.underlineToCamel2(name));
			cd.setDataType(type);
			cd.setColumnType(rs.getString(2));
			cd.setColumnComment(comment);
			cd.setPrecision(precision);
			cd.setScale(scale);
			cd.setCharmaxLength(charmaxLength);
			cd.setNullable(nullable);
			cd.setImportPackge(getImportType(type, scale));
			formatFieldClassType(cd);
			columnList.add(cd);
		}
		rs.close();
		ps.close();
		con.close();
		return columnList;
	}
	/**
	 * 表名转换成类名
	 * @param tableName 表名
	 * @param prefix 业务前缀
	 * @return
	 */
	public static String getTablesNameToClassName(String tableName,String prefix) {
		if(tableName.startsWith(prefix)){
			tableName = tableName.replaceFirst(prefix, "");
		}
		String[] split = tableName.split("_");
		if (split.length > 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < split.length; i++) {
				String tempTableName = split[i].substring(0, 1).toUpperCase()
						+ split[i].substring(1, split[i].length());
				sb.append(tempTableName);
			}

			return sb.toString();
		}
		String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
		return tempTables;
	}

	/**
	 * 获取导入包
	 * 
	 * @param dataType
	 * @param scale
	 * @return
	 */
	public static String getImportType(String dataType, String scale) {
		dataType = dataType.toLowerCase();
		if (dataType.contains("number")) {
			if ((GenerateUtils.isNotEmpty(scale)) && (Integer.parseInt(scale) > 0)) {
				return "java.math.BigDecimal;";
			}
		} else if (dataType.contains("decimal")) {
			return "java.math.BigDecimal;";
		} else if (dataType.contains("date")) {
			return "java.util.Date;";
		} else if (dataType.contains("time")) {
			return "java.sql.Timestamp;";
		} else if (dataType.contains("clob")) {
			return "java.sql.Clob;";
		}
		return "";
	}

	private static void formatFieldClassType(ColumnData columnt) {
		String fieldType = columnt.getColumnType();
		String scale = columnt.getScale();

		if ("N".equals(columnt.getNullable())) {
			columnt.setOptionType("required:true");
		}
		if (("datetime".equals(fieldType)) || ("time".equals(fieldType))) {
			columnt.setClassType("easyui-datetimebox");
		} else if ("date".equals(fieldType)) {
			columnt.setClassType("easyui-datebox");
		} else if ("int".equals(fieldType)) {
			columnt.setClassType("easyui-numberbox");
		} else if ("number".equals(fieldType)) {
			if ((GenerateUtils.isNotEmpty(scale)) && (Integer.parseInt(scale) > 0)) {
				columnt.setClassType("easyui-numberbox");
				if (GenerateUtils.isNotEmpty(columnt.getOptionType()))
					columnt.setOptionType(columnt.getOptionType() + "," + "precision:2,groupSeparator:','");
				else
					columnt.setOptionType("precision:2,groupSeparator:','");
			} else {
				columnt.setClassType("easyui-numberbox");
			}
		} else if (("float".equals(fieldType)) || ("double".equals(fieldType)) || ("decimal".equals(fieldType))) {
			columnt.setClassType("easyui-numberbox");
			if (GenerateUtils.isNotEmpty(columnt.getOptionType()))
				columnt.setOptionType(columnt.getOptionType() + "," + "precision:2,groupSeparator:','");
			else
				columnt.setOptionType("precision:2,groupSeparator:','");
		} else {
			columnt.setClassType("easyui-validatebox");
		}
	}

	public static String getColumnFields(String columns) throws SQLException {
		String fields = columns;
		if ((fields != null) && (!"".equals(fields))) {
			fields = fields.replaceAll("[|]", ",");
		}
		return fields;
	}

	public static String[] getColumnList(String columns) throws SQLException {
		String[] columnList = columns.split("[|]");
		return columnList;
	}

	public static String getColumnSplit(List<ColumnData> columnList) throws SQLException {
		StringBuffer commonColumns = new StringBuffer();
		for (ColumnData data : columnList) {
			commonColumns.append(data.getColumnName() + "|");
		}
		return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
	}

}
