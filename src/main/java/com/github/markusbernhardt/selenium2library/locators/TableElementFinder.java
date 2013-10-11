package com.github.markusbernhardt.selenium2library.locators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TableElementFinder {

	protected final static TreeMap<String, List<String>> locatorSuffixesMap;

	static {
		locatorSuffixesMap = new TreeMap<String, List<String>>();
		addLocatorSuffix(locatorSuffixesMap, "css.default", "");
		addLocatorSuffix(locatorSuffixesMap, "css.content", "");
		addLocatorSuffix(locatorSuffixesMap, "css.header", " th");
		addLocatorSuffix(locatorSuffixesMap, "css.footer", " tfoot td");
		addLocatorSuffix(locatorSuffixesMap, "css.row", " tr:nth-child(%s)");
		addLocatorSuffix(locatorSuffixesMap, "css.col", " tr td:nth-child(%s)", " tr th:nth-child(%s)");

		addLocatorSuffix(locatorSuffixesMap, "sizzle.default", "");
		addLocatorSuffix(locatorSuffixesMap, "sizzle.content", "");
		addLocatorSuffix(locatorSuffixesMap, "sizzle.header", " th");
		addLocatorSuffix(locatorSuffixesMap, "sizzle.footer", " tfoot td");
		addLocatorSuffix(locatorSuffixesMap, "sizzle.row", " tr:nth-child(%s)");
		addLocatorSuffix(locatorSuffixesMap, "sizzle.col", " tr td:nth-child(%s)", " tr th:nth-child(%s)");

		addLocatorSuffix(locatorSuffixesMap, "xpath.default", "");
		addLocatorSuffix(locatorSuffixesMap, "xpath.content", "//*");
		addLocatorSuffix(locatorSuffixesMap, "xpath.header", "//th");
		addLocatorSuffix(locatorSuffixesMap, "xpath.footer", "//tfoot//td");
		addLocatorSuffix(locatorSuffixesMap, "xpath.row", "//tr[%s]//*");
		addLocatorSuffix(locatorSuffixesMap, "xpath.col", "//tr//*[self::td or self::th][%s]");
	}

	public static WebElement find(WebDriver webDriver, String tableLocator) {
		List<String> locators = parseTableLocator(tableLocator, "default");
		return searchInLocators(webDriver, locators, null);
	}

	public static WebElement findByContent(WebDriver webDriver, String tableLocator, String content) {
		List<String> locators = parseTableLocator(tableLocator, "content");
		return searchInLocators(webDriver, locators, content);
	}

	public static WebElement findByHeader(WebDriver webDriver, String tableLocator, String content) {
		List<String> locators = parseTableLocator(tableLocator, "header");
		return searchInLocators(webDriver, locators, content);
	}

	public static WebElement findByFooter(WebDriver webDriver, String tableLocator, String content) {
		List<String> locators = parseTableLocator(tableLocator, "footer");
		return searchInLocators(webDriver, locators, content);
	}

	public static WebElement findByRow(WebDriver webDriver, String tableLocator, int row, String content) {
		List<String> locators = parseTableLocator(tableLocator, "row");
		List<String> formattedLocators = new ArrayList<String>();
		for (String locator : locators) {
			formattedLocators.add(String.format(locator, Integer.toString(row)));
		}
		return searchInLocators(webDriver, formattedLocators, content);
	}

	public static WebElement findByCol(WebDriver webDriver, String tableLocator, int col, String content) {
		List<String> locators = parseTableLocator(tableLocator, "col");
		List<String> formattedLocators = new ArrayList<String>();
		for (String locator : locators) {
			formattedLocators.add(String.format(locator, Integer.toString(col)));
		}
		return searchInLocators(webDriver, formattedLocators, content);
	}

	protected static void addLocatorSuffix(Map<String, List<String>> locatorSuffixesMap, String key, String... values) {
		List<String> list = new ArrayList<String>();
		for (String value : values) {
			list.add(value);
		}
		locatorSuffixesMap.put(key, list);
	}

	protected static List<String> parseTableLocator(String tableLocator, String locationMethod) {
		String tableLocatorType = null;

		if (tableLocator.startsWith("xpath=")) {
			tableLocatorType = "xpath.";
		} else if (tableLocator.startsWith("jquery=") || tableLocator.startsWith("sizzle=")) {
			tableLocatorType = "sizzle.";
		} else {
			if (!tableLocator.startsWith("css=")) {
				tableLocator = String.format("css=table#%s", tableLocator);
			}
			tableLocatorType = "css.";
		}

		List<String> locatorSuffixes = locatorSuffixesMap.get(tableLocatorType + locationMethod);

		List<String> parsedTabeLocators = new ArrayList<String>();
		for (String locatorSuffix : locatorSuffixes) {
			parsedTabeLocators.add(tableLocator + locatorSuffix);
		}
		return parsedTabeLocators;
	}

	protected static WebElement searchInLocators(WebDriver webDriver, List<String> locators, String content) {
		for (String locator : locators) {
			List<WebElement> elements = ElementFinder.find(webDriver, locator);
			for (WebElement element : elements) {
				if (content == null) {
					return element;
				}
				String elementText = element.getText();
				if (elementText != null && elementText.contains(content)) {
					return element;
				}
			}
		}
		return null;
	}
}
