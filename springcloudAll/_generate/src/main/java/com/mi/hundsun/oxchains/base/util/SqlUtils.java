package com.mi.hundsun.oxchains.base.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.hundsun.oxchains.base.bean.ColumnData;

public class SqlUtils {
	/**
	 * 拼装删除sql
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public static String getDeleteSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("delete ");
		sb.append("\t from ").append(tableName).append(" where ");
		sb.append(columnsList[0]).append(" = #{").append(columnsList[0]).append("}");
		return sb.toString();
	}

	/**
	 * 拼装单个查询sql
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public static String getSelectByIdSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("select <include refid=\"base_column_list\" /> \n");
		sb.append("\t from ").append(tableName).append(" where ");
		sb.append(columnsList[0]).append(" = #{").append(columnsList[0]).append("}");
		return sb.toString();
	}

	/**
	 * 拼装条件更新sql
	 * 
	 * @param tableName
	 * @param columnList
	 * @return
	 * @throws SQLException
	 */
	public static String getUpdateSelectiveSql(String tableName, List<ColumnData> columnList) throws SQLException {
		StringBuffer sb = new StringBuffer();
		ColumnData cd = (ColumnData) columnList.get(0);
		sb.append("\t<trim  suffixOverrides=\",\" >\n");
		for (int i = 1; i < columnList.size(); i++) {
			ColumnData data = (ColumnData) columnList.get(i);
			String columnName = data.getColumnName();
			if ("CREATE_USER".equals(columnName.toUpperCase()) || "CREATE_TIME".equals(columnName.toUpperCase())) {
				continue;
			}
			sb.append("\t<if test=\"").append(Utils.underlineToCamel2(columnName)).append(" != null ");

			if ("String" == data.getDataType()) {
				sb.append(" and ").append(Utils.underlineToCamel2(columnName)).append(" != ''");
			}
			sb.append(" \">\n\t\t");
			sb.append(columnName + "=#{" + Utils.underlineToCamel2(columnName) + "},\n");
			sb.append("\t</if>\n");
		}
		sb.append("\t</trim>");
		String update = "update " + tableName + " set \n" + sb.toString() + " where " + cd.getColumnName() + "=#{"
				+ Utils.underlineToCamel2(cd.getColumnName()) + "}";
		return update;
	}

	/**
	 * 获取更新sql
	 * 
	 * @param tableName
	 * @param columnsList
	 * @return
	 * @throws SQLException
	 */
	public static String getUpdateSql(String tableName, String[] columnsList) throws SQLException {
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < columnsList.length; i++) {
			String column = columnsList[i];
			if ("CREATE_TIME".equals(column.toUpperCase()) || "CREATE_USER".equals(column.toUpperCase())) {
				continue;
			}
			if ("UPDATE_TIME".equals(column.toUpperCase()))
				sb.append(column + "=now()");
			else {
				sb.append(column + "=#{" + Utils.underlineToCamel2(column) + "}");
			}
			if (i + 1 < columnsList.length) {
				sb.append(",");
			}
		}
		String update = "update " + tableName + " set " + sb.toString() + " where " + columnsList[0] + "=#{"
				+ Utils.underlineToCamel2(columnsList[0]) + "}";
		return update;
	}

	public static String getInsertSql(String tableName, List<ColumnData> columnList) throws SQLException {
		StringBuffer columns = new StringBuffer(" ( \n");
		StringBuffer values = new StringBuffer(" ( \n");
		columns.append("\t<trim  suffixOverrides=\",\" >\n");
		columns.append("\t\t id , \n");
		values.append("\t<trim  suffixOverrides=\",\" >\n");
		values.append("\t\t #{id} , \n");
		for (int i = 1; i < columnList.size(); i++) {
			ColumnData data = (ColumnData) columnList.get(i);
			String columnName = data.getColumnName();
			if ("UPDATE_USER".equals(columnName.toUpperCase()) || "UPDATE_TIME".equals(columnName.toUpperCase())) {
				continue;
			}
			columns.append("\t<if test=\"").append(Utils.underlineToCamel2(columnName)).append(" != null ");
			if ("String" == data.getDataType()) {
				columns.append(" and ").append(Utils.underlineToCamel2(columnName)).append(" != ''");
			}
			columns.append(" \">\n\t\t");
			columns.append(columnName + ",\n");
			columns.append("\t</if>\n");

			values.append("\t<if test=\"").append(Utils.underlineToCamel2(columnName)).append(" != null ");
			if ("String" == data.getDataType()) {
				values.append(" and ").append(Utils.underlineToCamel2(columnName)).append(" != ''");
			}
			values.append(" \">\n\t\t");
			values.append("#{" + Utils.underlineToCamel2(columnName) + "},\n");
			values.append("\t</if>\n");
		}
		columns.append("\t</trim>");
		values.append("\t</trim>");
		columns.append(" ) ");
		values.append(" ) ");
		String insert = "insert into " + tableName + columns + " values " + values;
		return insert;
	}

	/**
	 * 创建sql语句
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getAutoCreateSql(String tableName) throws Exception {
		Map<String, Object> sqlMap = new HashMap<>();
		List<ColumnData> columnDatas = TableUtils.getColumnDatas(tableName);
		String columns = TableUtils.getColumnSplit(columnDatas);
		String[] columnList = TableUtils.getColumnList(columns);
		String columnFields = TableUtils.getColumnFields(columns);
		String insert = getInsertSql(tableName, columnDatas);
		String update = getUpdateSql(tableName, columnList);
		String updateSelective = getUpdateSelectiveSql(tableName, columnDatas);
		String selectById = getSelectByIdSql(tableName, columnList);
		String delete = getDeleteSql(tableName, columnList);
		sqlMap.put("columnList", columnList);
		sqlMap.put("columnFields", columnFields);
		sqlMap.put("insert", insert.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
		sqlMap.put("update", update.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
		sqlMap.put("delete", delete);
		sqlMap.put("updateSelective", updateSelective);
		sqlMap.put("selectById", selectById);
		return sqlMap;
	}
}
