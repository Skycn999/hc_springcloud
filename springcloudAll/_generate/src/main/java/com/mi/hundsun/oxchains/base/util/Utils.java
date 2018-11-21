package com.mi.hundsun.oxchains.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Utils {
	public void getAddNode(String filePath, String xPath, String newNode, Map<String, String> attrMap, String text)
			throws Exception {
		if (getQueryNode(filePath, xPath, newNode, attrMap, text) < 1) {
			Document document = getPath(filePath, "UTF-8");
			List<?> list = document.selectNodes(xPath);
			System.out.println(xPath);
			Element element = (Element) list.get(0);
			Element newElement = element.addElement(newNode);
			for (Map.Entry<String, String> entry : attrMap.entrySet()) {
				newElement.addAttribute((String) entry.getKey(), (String) entry.getValue());
			}
			if ((text != null) && (text.trim().length() > 0)) {
				newElement.addText(text);
			}
			getXMLWrite(document, filePath);
			System.out.println("修改" + xPath + "成功");
		} else {
			System.out.println("已添");
		}
	}

	public int getQueryNode(String filePath, String xPath, String newNode, Map<String, String> attrMap, String text)
			throws Exception {
		int count = 0;
		Document document = getPath(filePath, "UTF-8");
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : attrMap.entrySet()) {
			sb.append("[@" + (String) entry.getKey() + "='" + (String) entry.getValue() + "']");
		}
		xPath = xPath + "/" + newNode + sb.toString();
		System.out.println("xPath=" + xPath);
		document.selectNodes(xPath);
		List<?> list = document.selectNodes(xPath);
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element) list.get(i);
			if (element.getText().equals(text)) {
				count++;
			}

		}

		return count;
	}

	public void getXMLWrite(Document document, String filePath) throws Exception {
		OutputFormat of = new OutputFormat(" ", true);
		of.setEncoding("UTF-8");
		XMLWriter xw = new XMLWriter(new FileWriter(filePath), of);
		// xw.setEscapeText(false);
		xw.write(document);
		xw.close();
		System.out.println(document.asXML());
	}

	public void getEditNode(String filePath, String xPath, Map<String, String> attrMap, String text) throws Exception {
		Document document = getPath(filePath, "UTF-8");
		List<?> list = document.selectNodes(xPath);
		Element element = (Element) list.get(0);
		if (attrMap != null) {
			for (Map.Entry<String, String> entry : attrMap.entrySet()) {
				element.addAttribute((String) entry.getKey(), (String) entry.getValue());
			}
		}

		List<?> nodelist = element.elements();
		for (int i = 0; i < nodelist.size(); i++) {
			Element nodeElement = (Element) nodelist.get(i);
			nodeElement.getParent().remove(nodeElement);
		}
		element.setText(text);
		getXMLWrite(document, filePath);
	}

	public Document getPath(String filePath, String coding) {
		SAXReader saxReader = new SAXReader();

		Document document = null;
		try {
			saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			BufferedReader read = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(filePath)), coding));
			document = saxReader.read(read);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public static String getNullAble(String nullable) {
		if (("YES".equals(nullable)) || ("yes".equals(nullable)) || ("y".equals(nullable)) || ("Y".equals(nullable))) {
			return "Y";
		}
		if (("NO".equals(nullable)) || ("N".equals(nullable)) || ("no".equals(nullable)) || ("n".equals(nullable))) {
			return "N";
		}
		return null;
	}

	public static final char UNDERLINE = '_';

	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel2(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			// String.valueOf(Character.toUpperCase(sb.charAt(position)));
			sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Utils xml = new Utils();
		String filePath1 = "D:\\MyEclipse 8.5\\ssi\\src\\com\\wei\\ssi\\conf\\sqlmap\\ProUserSQL.xml";
		// String filePath = "D:\\MyEclipse
		// 8.5\\ssi\\src\\com\\wei\\ssi\\conf\\struts2\\struts2-ssi-proWbType.xml";
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("file", "no");
			xml.getEditNode(filePath1, "/sqlMap/select[@id='getProUserList']", map, "嘿嘿");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}