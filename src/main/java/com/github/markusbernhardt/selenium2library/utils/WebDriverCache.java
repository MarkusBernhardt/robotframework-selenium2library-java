package com.github.markusbernhardt.selenium2library.utils;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryFatalException;

public class WebDriverCache {

	/**
	 * The session id of the last registered web driver
	 */
	int currentSessionId = 0;

	/**
	 * The last registered web driver
	 */
	WebDriver currentWebDriver;

	/**
	 * Map session ids to webdrivers
	 */
	Map<Integer, WebDriver> webDriverBySessionId = new TreeMap<Integer, WebDriver>();

	/**
	 * Map aliases to webdrivers
	 */
	Map<String, Integer> sessionIdByAlias = new TreeMap<String, Integer>();

	public String register(WebDriver webDriver, String alias) {
		currentSessionId++;
		currentWebDriver = webDriver;
		webDriverBySessionId.put(currentSessionId, webDriver);
		if (alias != null) {
			sessionIdByAlias.put(alias, currentSessionId);
		}
		return Integer.toString(currentSessionId);
	}

	public WebDriver getCurrent() {
		return currentWebDriver;
	}

	public String getCurrentSessionId() {
		return Integer.toString(currentSessionId);
	}

	public void close() {
		currentSessionId = 0;
		close(currentWebDriver);
	}

	public void close(WebDriver webDriver) {
		if (webDriver != null) {
			webDriver.quit();
			webDriverBySessionId.values().remove(webDriver);
			sessionIdByAlias.values().remove(webDriver);
		}
	}

	public void closeAll() {
		for (WebDriver webDriver : webDriverBySessionId.values()) {
			webDriver.quit();
		}
		webDriverBySessionId = new TreeMap<Integer, WebDriver>();
		sessionIdByAlias = new TreeMap<String, Integer>();
	}

	public void switchBrowser(String sessionIdOrAlias) {
		Integer sessionId = sessionIdByAlias.get(sessionIdOrAlias);
		if (sessionId == null) {
			try {
				sessionId = Integer.parseInt(sessionIdOrAlias);
			} catch (NullPointerException npe) {
				throw new Selenium2LibraryFatalException(String.format(
						"Non-existing index or alias '%s'", sessionIdOrAlias));
			}
		}
		WebDriver webDriver = webDriverBySessionId.get(sessionId);
		if (webDriver == null) {
			throw new Selenium2LibraryFatalException(String.format(
					"Non-existing index or alias '%s'", sessionIdOrAlias));
		}
		currentWebDriver = webDriver;
		currentSessionId = sessionId;
	}

	public Collection<WebDriver> getWebDrivers() {
		return webDriverBySessionId.values();
	}
}
