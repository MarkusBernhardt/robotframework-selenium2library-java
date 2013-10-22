package com.github.markusbernhardt.selenium2library.utils;

import com.github.markusbernhardt.xmldoclet.xjc.Method;
import com.github.markusbernhardt.xmldoclet.xjc.TagInfo;

public class Javadoc2LibdocFormatter {
	public static String formatComment(Method methodNode) {
		StringBuilder stringBuilder = new StringBuilder();

		if (methodNode.getComment() != null) {
			stringBuilder.append(methodNode.getComment());
		}

		stringBuilder.append(formatParam(methodNode));
		stringBuilder.append(formatReturn(methodNode));
		stringBuilder.append(formatSee(methodNode));

		return stringBuilder.toString();
	}

	private static String formatParam(Method methodNode) {
		boolean hasTag = false;
		StringBuilder stringBuilderParam = new StringBuilder();
		stringBuilderParam.append("<br><b>Parameters:</b><br>");
		for (TagInfo tagInfo : methodNode.getTag()) {
			if (!tagInfo.getName().equals("@param")) {
				continue;
			}
			hasTag = true;
			String text = tagInfo.getText();
			int index = text.indexOf('\n');
			stringBuilderParam.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>");
			stringBuilderParam.append(text.substring(0, index));
			stringBuilderParam.append("</b>&nbsp;");
			stringBuilderParam.append(text.substring(index + 1).trim());
			stringBuilderParam.append("<br>");
		}
		if (hasTag) {
			return stringBuilderParam.toString();
		}
		return "";
	}

	private static String formatReturn(Method methodNode) {
		boolean hasTag = false;
		StringBuilder stringBuilderParam = new StringBuilder();
		stringBuilderParam.append("<br><b>Returns:</b><br>");
		for (TagInfo tagInfo : methodNode.getTag()) {
			if (!tagInfo.getName().equals("@return")) {
				continue;
			}
			hasTag = true;
			String text = tagInfo.getText();
			stringBuilderParam.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			stringBuilderParam.append(text);
			stringBuilderParam.append("<br>");
		}
		if (hasTag) {
			return stringBuilderParam.toString();
		}
		return "";
	}

	private static String formatSee(Method methodNode) {
		boolean hasTag = false;
		StringBuilder stringBuilderParam = new StringBuilder();
		stringBuilderParam.append("<br><b>See Also:</b><br>");
		for (TagInfo tagInfo : methodNode.getTag()) {
			if (!tagInfo.getName().equals("@see")) {
				continue;
			}
			hasTag = true;
			String camelCasedKeyword = tagInfo.getText();
			int index = camelCasedKeyword.indexOf('#');
			if (index >= 0) {
				camelCasedKeyword = camelCasedKeyword.substring(index + 1);
			}
			camelCasedKeyword = camelCasedKeyword.trim();

			stringBuilderParam.append("&nbsp;&nbsp;&nbsp;&nbsp;`");
			char[] camelCasedKeywordArray = camelCasedKeyword.toCharArray();
			stringBuilderParam.append(Character.toUpperCase(camelCasedKeywordArray[0]));
			for (int i = 1; i < camelCasedKeywordArray.length; i++) {
				if (camelCasedKeywordArray[i] >= 'A' && camelCasedKeywordArray[i] <= 'Z') {
					stringBuilderParam.append(' ');
				}
				stringBuilderParam.append(camelCasedKeywordArray[i]);
			}
			stringBuilderParam.append("`<br>");
		}
		if (hasTag) {
			return stringBuilderParam.toString();
		}
		return "";
	}

}
