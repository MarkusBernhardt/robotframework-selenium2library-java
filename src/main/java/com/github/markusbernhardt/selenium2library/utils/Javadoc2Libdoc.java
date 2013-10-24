package com.github.markusbernhardt.selenium2library.utils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.github.markusbernhardt.xmldoclet.xjc.AnnotationInstance;
import com.github.markusbernhardt.xmldoclet.xjc.Class;
import com.github.markusbernhardt.xmldoclet.xjc.Constructor;
import com.github.markusbernhardt.xmldoclet.xjc.Method;
import com.github.markusbernhardt.xmldoclet.xjc.ObjectFactory;
import com.github.markusbernhardt.xmldoclet.xjc.Package;
import com.github.markusbernhardt.xmldoclet.xjc.Root;
import com.github.markusbernhardt.xmldoclet.xjc.TagInfo;

public class Javadoc2Libdoc {
	protected final Map<String, String> keywordDocumentationMap;

	public Javadoc2Libdoc(java.lang.Class<?> clazz) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(clazz.getName().replace(".", File.separator) + ".javadoc");
		Root root = loadJavadocRoot(inputStream);
		keywordDocumentationMap = loadKeywordDocumentationMap(root, clazz.getName());
	}

	public String getKeywordDocumentation(String keywordName) {
		return keywordDocumentationMap.get(keywordName);
	}

	protected Root loadJavadocRoot(InputStream inputStream) {
		try {
			JAXBContext context = JAXBContext.newInstance(Root.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Root) unmarshaller.unmarshal(inputStream);
		} catch (JAXBException e) {
			return new ObjectFactory().createRoot();
		}
	}

	protected Map<String, String> loadKeywordDocumentationMap(Root root, String className) {
		Map<String, String> keywordDocumentation = new HashMap<String, String>();
		for (Package packageNode : root.getPackage()) {
			for (Class classNode : packageNode.getClazz()) {
				if (className.equals(classNode.getQualified())) {
					keywordDocumentation.put("__intro__", formatComment(classNode));
					Constructor constructorNodeWithComment = null;
					for (Constructor constructorNode : classNode.getConstructor()) {
						if (constructorNode.getComment() != null && constructorNode.getComment().trim().length() > 0) {
							constructorNodeWithComment = constructorNode;
						}
					}
					keywordDocumentation.put("__init__", formatComment(constructorNodeWithComment));
				}
				for (Method methodNode : classNode.getMethod()) {
					for (AnnotationInstance annotationInstanceNode : methodNode.getAnnotation()) {
						if (annotationInstanceNode.getName().equals("RobotKeyword")) {
							keywordDocumentation.put(methodNode.getName(), formatComment(methodNode));
							break;
						}
					}
				}
			}
		}
		return keywordDocumentation;
	}

	protected String formatComment(Class classNode) {
		if (classNode.getComment() != null) {
			return classNode.getComment();
		}
		return "";
	}

	protected String formatComment(Constructor constructorNode) {
		if (constructorNode.getComment() != null) {
			return constructorNode.getComment();
		}
		return "";
	}

	protected String formatComment(Method methodNode) {
		StringBuilder stringBuilder = new StringBuilder();

		if (methodNode.getComment() != null) {
			stringBuilder.append(methodNode.getComment());
		}

		stringBuilder.append(formatParam(methodNode));
		stringBuilder.append(formatReturn(methodNode));
		stringBuilder.append(formatSee(methodNode));

		return stringBuilder.toString();
	}

	protected String formatParam(Method methodNode) {
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

	protected String formatReturn(Method methodNode) {
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

	protected String formatSee(Method methodNode) {
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
