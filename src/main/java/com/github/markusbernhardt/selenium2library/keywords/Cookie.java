package com.github.markusbernhardt.selenium2library.keywords;

import java.util.ArrayList;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
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

	@RobotKeyword("Deletes all cookies.")
	public void deleteAllCookies() {
		browserManagement.getCurrentWebDriver().manage().deleteAllCookies();
	}

	@RobotKeyword("Deletes cookie matching _name_.\n\n"

	+ "If the cookie is not found, nothing happens.\n")
	@ArgumentNames({ "name" })
	public void deleteCookie(String name) {
		browserManagement.getCurrentWebDriver().manage().deleteCookieNamed(name);
	}

	@RobotKeyword("Returns all cookies of the current page.\n")
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

	@RobotKeyword("Returns value of cookie found with _name_.\n\n"

	+ "If no cookie is found with name, this keyword fails.\n")
	@ArgumentNames({ "name" })
	public String getCookieValue(String name) {
		org.openqa.selenium.Cookie cookie = browserManagement.getCurrentWebDriver().manage().getCookieNamed(name);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			throw new Selenium2LibraryNonFatalException(String.format("Cookie with name %s not found.", name));
		}
	}

	@RobotKeyword("Adds a cookie to your current session.")
	@ArgumentNames({ "name", "value", "path", "domain", "secure", "expiry" })
	public void addCookie(String name, String value, String path, String domain, String secure, String expiry) {
		// Parameter expiry not used by Python library
		org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie(name, value, domain, path, null,
				secure.equals("True"));
		browserManagement.getCurrentWebDriver().manage().addCookie(cookie);
	}
}
