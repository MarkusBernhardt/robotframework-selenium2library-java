package com.github.markusbernhardt.selenium2library.keywords;

import java.util.ArrayList;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

public abstract class Cookie extends BrowserManagement {

	// ##############################
	// Keywords
	// ##############################

	public void deleteAllCookies() {
		webDriverCache.getCurrent().manage().deleteAllCookies();
	}

	public void deleteCookie(String name) {
		webDriverCache.getCurrent().manage().deleteCookieNamed(name);
	}

	public String getCookies() {
		StringBuffer ret = new StringBuffer();

		ArrayList<org.openqa.selenium.Cookie> cookies = new ArrayList<org.openqa.selenium.Cookie>(
				webDriverCache.getCurrent().manage().getCookies());
		for (int i = 0; i < cookies.size(); i++) {
			ret.append(cookies.get(i).getName() + "="
					+ cookies.get(i).getValue());
			if (i != cookies.size() - 1) {
				ret.append("; ");
			}
		}

		return ret.toString();
	}

	public String getCookieValue(String name) {
		org.openqa.selenium.Cookie cookie = webDriverCache.getCurrent()
				.manage().getCookieNamed(name);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Cookie with name %s not found.", name));
		}
	}

	public void addCookie(String name, String value, String path,
			String domain, String secure, String expiry) {
		// Parameter expiry not used by Python library
		org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie(
				name, value, domain, path, null, secure.equals("True"));
		webDriverCache.getCurrent().manage().addCookie(cookie);
	}
}
