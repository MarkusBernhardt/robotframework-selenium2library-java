package com.github.markusbernhardt.selenium2library.keywords;

import java.util.ArrayList;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

@RobotKeywords
public class Cookie extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	// ##############################
	// Keywords
	// ##############################

	/**
	 * Deletes all cookies.<br>
	 */
	@RobotKeyword
	public void deleteAllCookies() {
		browserManagement.getCurrentWebDriver().manage().deleteAllCookies();
	}

	/**
	 * Deletes cookie matching <b>name</b>.<br>
	 * <br>
	 * If the cookie is not found, nothing happens<br>
	 * 
	 * @param name
	 *            The name of the cookie to delete.
	 */
	@RobotKeyword
	@ArgumentNames({ "name" })
	public void deleteCookie(String name) {
		browserManagement.getCurrentWebDriver().manage().deleteCookieNamed(name);
	}

	/**
	 * Returns all cookies of the current page.<br>
	 * 
	 * @return All cookies of the current page.
	 */
	@RobotKeyword
	public String getCookies() {
		StringBuffer ret = new StringBuffer();

		ArrayList<org.openqa.selenium.Cookie> cookies = new ArrayList<org.openqa.selenium.Cookie>(browserManagement
				.getCurrentWebDriver().manage().getCookies());
		for (int i = 0; i < cookies.size(); i++) {
			ret.append(cookies.get(i).getName() + "=" + cookies.get(i).getValue());
			if (i != cookies.size() - 1) {
				ret.append("; ");
			}
		}

		return ret.toString();
	}

	/**
	 * Returns value of cookie found with <b>name</b>.<br>
	 * <br>
	 * If no cookie is found with name, this keyword fails.<br>
	 * 
	 * @param name
	 *            The name of the cookie
	 * @return The value of the found cookie
	 */
	@RobotKeyword
	@ArgumentNames({ "name" })
	public String getCookieValue(String name) {
		org.openqa.selenium.Cookie cookie = browserManagement.getCurrentWebDriver().manage().getCookieNamed(name);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			throw new Selenium2LibraryNonFatalException(String.format("Cookie with name %s not found.", name));
		}
	}

	@RobotKeywordOverload
	public void addCookie(String name, String value) {
		addCookie(name, value, null);
	}

	@RobotKeywordOverload
	public void addCookie(String name, String value, String path) {
		addCookie(name, value, path, null);
	}

	@RobotKeywordOverload
	public void addCookie(String name, String value, String path, String domain) {
		addCookie(name, value, path, domain, "");
	}

	@RobotKeywordOverload
	public void addCookie(String name, String value, String path, String domain, String secure) {
		addCookie(name, value, path, domain, secure, null);
	}

	/**
	 * Adds a cookie to your current session.<br>
	 * 
	 * @param name
	 *            The name of the cookie.
	 * @param value
	 *            The cookie value.
	 * @param path
	 *            Default=NONE. The path the cookie is visible to.
	 * @param domain
	 *            Default=NONE. The domain the cookie is visible to.
	 * @param secure
	 *            Default=NONE. Whether this cookie requires a secure
	 *            connection.
	 * @param expiry
	 *            Default=NONE. The cookie's expiration date
	 */
	@RobotKeyword
	@ArgumentNames({ "name", "value", "path=NONE", "domain=NONE", "secure=NONE", "expiry=NONE" })
	public void addCookie(String name, String value, String path, String domain, String secure, String expiry) {
		// Parameter expiry not used by Python library
		org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie(name, value, domain, path, null,
				"true".equals(secure.toLowerCase()));
		browserManagement.getCurrentWebDriver().manage().addCookie(cookie);
	}
}
