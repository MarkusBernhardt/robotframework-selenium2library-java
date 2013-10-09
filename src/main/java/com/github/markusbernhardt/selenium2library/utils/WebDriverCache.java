package com.github.markusbernhardt.selenium2library.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryFatalException;

public class WebDriverCache {

	/**
	 * The currently active web driver instance
	 */
	SessionIdAliasWebDriverTuple currentSessionIdAliasWebDriverTuple;

	/**
	 * The maximum assigned session id
	 */
	int maxAssignedSessionId = 0;

	/**
	 * Stack of currently open session ids to reuse
	 */
	Stack<String> openSessionIds = new Stack<String>();

	/**
	 * Stack of already closed session ids to reuse
	 */
	Stack<String> closedSessionIds = new Stack<String>();

	/**
	 * Map session ids to webdrivers
	 */
	Map<String, SessionIdAliasWebDriverTuple> tupleBySessionId = new TreeMap<String, SessionIdAliasWebDriverTuple>();

	/**
	 * Map aliases to webdrivers
	 */
	Map<String, SessionIdAliasWebDriverTuple> tupleByAlias = new TreeMap<String, SessionIdAliasWebDriverTuple>();

	public String register(WebDriver webDriver, String alias) {
		// create the new tuple
		currentSessionIdAliasWebDriverTuple = new SessionIdAliasWebDriverTuple();
		currentSessionIdAliasWebDriverTuple.alias = alias;
		currentSessionIdAliasWebDriverTuple.webDriver = webDriver;
		if (closedSessionIds.size() == 0) {
			// no closed id
			maxAssignedSessionId++;
			currentSessionIdAliasWebDriverTuple.id = Integer.toString(maxAssignedSessionId);
		} else {
			// reuse closed id
			currentSessionIdAliasWebDriverTuple.id = closedSessionIds.pop();
		}

		// store the new tuple
		openSessionIds.push(currentSessionIdAliasWebDriverTuple.id);
		tupleBySessionId.put(currentSessionIdAliasWebDriverTuple.id, currentSessionIdAliasWebDriverTuple);
		if (alias != null) {
			tupleByAlias.put(currentSessionIdAliasWebDriverTuple.alias, currentSessionIdAliasWebDriverTuple);
		}
		return currentSessionIdAliasWebDriverTuple.id;
	}

	public WebDriver getCurrent() {
		if (currentSessionIdAliasWebDriverTuple != null) {
			return currentSessionIdAliasWebDriverTuple.webDriver;
		}
		return null;
	}

	public String getCurrentSessionId() {
		if (currentSessionIdAliasWebDriverTuple != null) {
			return currentSessionIdAliasWebDriverTuple.id;
		}
		return null;
	}

	public void close() {
		if (currentSessionIdAliasWebDriverTuple != null) {
			// Close the webdriver and remove it from all stores
			currentSessionIdAliasWebDriverTuple.webDriver.quit();
			tupleBySessionId.remove(currentSessionIdAliasWebDriverTuple.id);
			openSessionIds.remove(currentSessionIdAliasWebDriverTuple.id);
			closedSessionIds.push(currentSessionIdAliasWebDriverTuple.id);
			if (currentSessionIdAliasWebDriverTuple.alias != null) {
				tupleByAlias.remove(currentSessionIdAliasWebDriverTuple.alias);
			}

			// Set the last opened webdriver as current webdriver
			if (openSessionIds.size() != 0) {
				currentSessionIdAliasWebDriverTuple = tupleBySessionId.get(openSessionIds.pop());
			} else {
				currentSessionIdAliasWebDriverTuple = null;
			}
		}
	}

	public void closeAll() {
		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : tupleBySessionId.values()) {
			sessionIdAliasWebDriverTuple.webDriver.quit();
		}
		maxAssignedSessionId = 0;
		currentSessionIdAliasWebDriverTuple = null;
		openSessionIds = new Stack<String>();
		closedSessionIds = new Stack<String>();
		tupleBySessionId = new TreeMap<String, SessionIdAliasWebDriverTuple>();
		tupleByAlias = new TreeMap<String, SessionIdAliasWebDriverTuple>();
	}

	public void switchBrowser(String sessionIdOrAlias) {
		SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple = tupleByAlias.get(sessionIdOrAlias);
		if (sessionIdAliasWebDriverTuple != null) {
			currentSessionIdAliasWebDriverTuple = sessionIdAliasWebDriverTuple;
			openSessionIds.remove(currentSessionIdAliasWebDriverTuple.id);
			openSessionIds.push(currentSessionIdAliasWebDriverTuple.id);
			return;
		}

		sessionIdAliasWebDriverTuple = tupleBySessionId.get(sessionIdOrAlias);
		if (sessionIdAliasWebDriverTuple != null) {
			currentSessionIdAliasWebDriverTuple = sessionIdAliasWebDriverTuple;
			openSessionIds.remove(currentSessionIdAliasWebDriverTuple.id);
			openSessionIds.push(currentSessionIdAliasWebDriverTuple.id);
			return;
		}
		throw new Selenium2LibraryFatalException(String.format("Non-existing index or alias '%s'", sessionIdOrAlias));
	}

	public Collection<SessionIdAliasWebDriverTuple> getWebDrivers() {
		return tupleBySessionId.values();
	}

	public static class SessionIdAliasWebDriverTuple {
		public String id;
		public String alias;
		public WebDriver webDriver;
	}
}
