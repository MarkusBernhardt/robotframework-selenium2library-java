package com.github.markusbernhardt.selenium2library.locators;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

public class WindowManager {

	public static int WINDOW_INFO_INDEX_WINDOW_ID = 0;
	public static int WINDOW_INFO_INDEX_WINDOW_NAME = 1;
	public static int WINDOW_INFO_INDEX_DOCUMENT_TITLE = 2;
	public static int WINDOW_INFO_INDEX_DOCUMENT_URL = 3;

	protected enum WindowManagerStrategy {
		DEFAULT {

			@Override
			public void select(WebDriver webDriver, SelectCoordinates selectCoordinates) {
				if (selectCoordinates.criteria == null || selectCoordinates.criteria.toLowerCase().equals("null")) {
					webDriver.switchTo().window("");
					return;
				}

				try {
					NAME.select(webDriver, selectCoordinates);
					return;
				} catch (Throwable t) {
				}

				try {
					TITLE.select(webDriver, selectCoordinates);
					return;
				} catch (Throwable t) {
				}
				throw new Selenium2LibraryNonFatalException("Unable to locate window with name or title '"
						+ selectCoordinates.criteria + "'");
			}
		},
		TITLE {

			@Override
			public void select(WebDriver webDriver, final SelectCoordinates selectCoordinates) {
				selectMatching(webDriver, new Matcher() {

					@Override
					public boolean match(List<String> currentWindowInfo) {
						return currentWindowInfo.get(WINDOW_INFO_INDEX_DOCUMENT_TITLE).trim().toLowerCase()
								.equals(selectCoordinates.criteria.toLowerCase());
					}

				}, "Unable to locate window with title '" + selectCoordinates.criteria + "'");
			}
		},
		NAME {

			@Override
			public void select(WebDriver webDriver, final SelectCoordinates selectCoordinates) {
				selectMatching(webDriver, new Matcher() {

					@Override
					public boolean match(List<String> currentWindowInfo) {
						return currentWindowInfo.get(WINDOW_INFO_INDEX_WINDOW_NAME).trim().toLowerCase()
								.equals(selectCoordinates.criteria.toLowerCase());
					}

				}, "Unable to locate window with name '" + selectCoordinates.criteria + "'");
			}
		},
		URL {

			@Override
			public void select(WebDriver webDriver, final SelectCoordinates selectCoordinates) {
				selectMatching(webDriver, new Matcher() {

					@Override
					public boolean match(List<String> currentWindowInfo) {
						return currentWindowInfo.get(WINDOW_INFO_INDEX_DOCUMENT_URL).trim().toLowerCase()
								.equals(selectCoordinates.criteria.toLowerCase());
					}

				}, "Unable to locate window with URL '" + selectCoordinates.criteria + "'");
			}
		};

		abstract public void select(WebDriver webDriver, SelectCoordinates selectCoordinates);

		protected static void selectMatching(WebDriver webDriver, Matcher matcher, String error) {
			String startingHandle = webDriver.getWindowHandle();
			for (String handle : webDriver.getWindowHandles()) {
				webDriver.switchTo().window(handle);
				if (matcher.match(getCurrentWindowInfo(webDriver))) {
					return;
				}
			}
			webDriver.switchTo().window(startingHandle);
			throw new Selenium2LibraryNonFatalException(error);
		}
	}

	public static List<String> getWindowIds(WebDriver webDriver) {
		List<String> windowIds = new ArrayList<String>();
		for (List<String> windowInfo : getWindowInfos(webDriver)) {
			windowIds.add(windowInfo.get(0));
		}
		return windowIds;
	}

	public static List<String> getWindowNames(WebDriver webDriver) {
		List<String> windowNames = new ArrayList<String>();
		for (List<String> windowInfo : getWindowInfos(webDriver)) {
			windowNames.add(windowInfo.get(1));
		}
		return windowNames;
	}

	public static List<String> getWindowTitles(WebDriver webDriver) {
		List<String> windowTitles = new ArrayList<String>();
		for (List<String> windowInfo : getWindowInfos(webDriver)) {
			windowTitles.add(windowInfo.get(2));
		}
		return windowTitles;
	}

	public static List<List<String>> getWindowInfos(WebDriver webDriver) {
		List<List<String>> windowInfos = new ArrayList<List<String>>();
		String startingHandle = webDriver.getWindowHandle();
		try {
			for (String handle : webDriver.getWindowHandles()) {
				webDriver.switchTo().window(handle);
				windowInfos.add(getCurrentWindowInfo(webDriver));
			}
		} finally {
			webDriver.switchTo().window(startingHandle);
		}
		return windowInfos;
	}

	public static void select(WebDriver webDriver, String locator) {
		if (webDriver == null) {
			throw new Selenium2LibraryNonFatalException("WindowManager.select: webDriver is null.");
		}

		SelectCoordinates selectCoordinates = new SelectCoordinates();
		WindowManagerStrategy strategy = parseLocator(selectCoordinates, locator);
		strategy.select(webDriver, selectCoordinates);
	}

	protected static WindowManagerStrategy parseLocator(SelectCoordinates selectCoordinates, String locator) {
		String prefix = null;
		String criteria = locator;
		if (locator != null && locator.length() > 0) {
			String[] locatorParts = locator.split("=", 2);
			if (locatorParts.length == 2) {
				prefix = locatorParts[0].trim().toUpperCase();
				criteria = locatorParts[1].trim();
			}
		}

		if (prefix == null || prefix.equals("name")) {
			if (criteria == null || criteria.toLowerCase().equals("main")) {
				criteria = "";
			}
		}

		WindowManagerStrategy strategy = WindowManagerStrategy.DEFAULT;
		if (prefix != null) {
			strategy = WindowManagerStrategy.valueOf(prefix);
		}
		selectCoordinates.criteria = criteria;
		return strategy;
	}

	@SuppressWarnings("unchecked")
	protected static List<String> getCurrentWindowInfo(WebDriver webDriver) {
		return (List<String>) ((JavascriptExecutor) webDriver)
				.executeScript("return [ window.id, window.name, document.title, document.url ];");
	}

	protected static class SelectCoordinates {

		String criteria;
	}

	protected static interface Matcher {

		boolean match(List<String> currentWindowInfo);

	}
}
