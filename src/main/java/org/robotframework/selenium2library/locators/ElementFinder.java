package org.robotframework.selenium2library.locators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.robotframework.selenium2library.Selenium2Library;
import org.robotframework.selenium2library.Selenium2LibraryNonFatalException;
import org.robotframework.selenium2library.utils.Python;

public class ElementFinder {

	private enum KeyAttrs {
		DEFAULT("@id,@name"), A(
				"@id,@name,@href,normalize-space(descendant-or-self::text())"), IMG(
				"@id,@name,@src,@alt"), INPUT("@id,@name,@value,@src"), BUTTON(
				"@id,@name,@value,normalize-space(descendant-or-self::text())");

		private String[] keyAttrs;

		KeyAttrs(String keyAttrs) {
			this.keyAttrs = keyAttrs.split(",");
		}

		public String[] getKeyAttrs() {
			return keyAttrs;
		}
	}

	private enum Strategy {
		DEFAULT {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				if (findByCoordinates.criteria.startsWith("//")) {
					return XPATH.findBy(webDriver, findByCoordinates);
				}
				return findByKeyAttrs(webDriver, findByCoordinates);
			}
		},
		IDENTIFIER {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				List<WebElement> elements = webDriver.findElements(By
						.id(findByCoordinates.criteria));
				elements.addAll(webDriver.findElements(By
						.name(findByCoordinates.criteria)));
				return filterElements(elements, findByCoordinates);
			}
		},
		ID {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.id(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		NAME {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.name(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		XPATH {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.xpath(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		DOM {

			@SuppressWarnings("unchecked")
			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				Object result = ((JavascriptExecutor) webDriver)
						.executeScript(String.format("return %s;",
								findByCoordinates.criteria));
				if (result == null) {
					return new ArrayList<WebElement>();
				} else if (result instanceof WebElement) {
					List<WebElement> list = new ArrayList<WebElement>();
					list.add((WebElement) result);
					result = list;
				}
				return filterElements((List<WebElement>) result,
						findByCoordinates);
			}
		},
		LINK {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.linkText(findByCoordinates.criteria)),
						findByCoordinates);
			}
		},
		CSS {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.cssSelector(findByCoordinates.criteria)),
						findByCoordinates);
			}
		},
		TAG {

			@Override
			List<WebElement> findBy(WebDriver webDriver,
					FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By
						.tagName(findByCoordinates.criteria)),
						findByCoordinates);
			}
		};

		abstract List<WebElement> findBy(WebDriver webDriver,
				FindByCoordinates findByCoordinates);

		private static List<WebElement> filterElements(
				List<WebElement> elements, FindByCoordinates findByCoordinates) {
			if (findByCoordinates.tag == null) {
				return elements;
			}

			List<WebElement> result = new ArrayList<WebElement>();
			for (WebElement element : elements) {
				if (elementMatches(element, findByCoordinates)) {
					result.add(element);
				}
			}
			return result;
		}

		private static boolean elementMatches(WebElement element,
				FindByCoordinates findByCoordinates) {
			if (!element.getTagName().toLowerCase()
					.equals(findByCoordinates.tag)) {
				return false;
			}

			for (String name : findByCoordinates.constraints.keySet()) {
				if (!element.getAttribute(name).equals(
						findByCoordinates.constraints.get(name))) {
					return false;
				}
			}

			return true;
		}

		private static List<WebElement> findByKeyAttrs(WebDriver webDriver,
				FindByCoordinates findByCoordinates) {
			KeyAttrs keyAttrs = KeyAttrs.DEFAULT;
			if (findByCoordinates.tag != null) {
				keyAttrs = KeyAttrs.valueOf(findByCoordinates.tag.trim()
						.toUpperCase());
			}
			String xpathCriteria = Selenium2Library
					.escapeXpathValue(findByCoordinates.criteria);
			String xpathTag = findByCoordinates.tag;
			if (findByCoordinates.tag == null) {
				xpathTag = "*";
			}
			List<String> xpathConstraints = new ArrayList<String>();
			for (Entry<String, String> entry : findByCoordinates.constraints
					.entrySet()) {
				xpathConstraints.add(String.format("@%s='%s'", entry.getKey(),
						entry.getValue()));
			}
			List<String> xpathSearchers = new ArrayList<String>();
			for (String attr : keyAttrs.getKeyAttrs()) {
				xpathSearchers.add(String.format("%s=%s", attr, xpathCriteria));
			}
			xpathSearchers.addAll(getAttrsWithUrl(webDriver, keyAttrs,
					findByCoordinates.criteria));
			String xpath = String.format(
					"//%s[%s(%s)]",
					xpathTag,
					Python.join(" and ", xpathConstraints)
							+ (xpathConstraints.size() > 0 ? " and " : ""),
					Python.join(" or ", xpathSearchers));

			return webDriver.findElements(By.xpath(xpath));
		}
	}

	private static List<String> getAttrsWithUrl(WebDriver webDriver,
			KeyAttrs keyAttrs, String criteria) {
		List<String> attrs = new ArrayList<String>();
		String url = null;
		String xpathUrl = null;
		String[] srcHref = { "@src", "@href" };
		for (String attr : srcHref) {
			for (String keyAttr : keyAttrs.getKeyAttrs()) {
				if (attr.equals(keyAttr)) {
					if (url == null || xpathUrl == null) {
						url = getBaseUrl(webDriver) + "/" + criteria;
						xpathUrl = Selenium2Library.escapeXpathValue(url);
					}
					attrs.add(String.format("%s=%s", attr, xpathUrl));
				}
			}
		}
		return attrs;
	}

	private static String getBaseUrl(WebDriver webDriver) {
		String url = webDriver.getCurrentUrl();
		int lastIndex = url.lastIndexOf('/');
		if (lastIndex != -1) {
			url = url.substring(0, lastIndex);
		}
		return url;
	}

	public static List<WebElement> find(WebDriver webDriver, String locator) {
		return find(webDriver, locator, null);
	}

	public static List<WebElement> find(WebDriver webDriver, String locator,
			String tag) {
		if (webDriver == null) {
			throw new Selenium2LibraryNonFatalException(
					"ElementFinder.find: webDriver is null.");
		}
		if (locator == null) {
			throw new Selenium2LibraryNonFatalException(
					"ElementFinder.find: locator is null.");
		}

		FindByCoordinates findByCoordinates = new FindByCoordinates();
		Strategy strategy = parseLocator(findByCoordinates, locator);
		parseTag(findByCoordinates, strategy, tag);
		return strategy.findBy(webDriver, findByCoordinates);
	}

	private static Strategy parseLocator(FindByCoordinates findByCoordinates,
			String locator) {
		String prefix = null;
		String criteria = locator;
		if (!locator.startsWith("//")) {
			String[] locatorParts = locator.split("=", 2);
			if (locatorParts[1] != null) {
				prefix = locatorParts[0].trim().toUpperCase();
				criteria = locatorParts[1].trim();
			}
		}

		Strategy strategy = Strategy.DEFAULT;
		if (prefix != null) {
			strategy = Strategy.valueOf(prefix);
		}
		findByCoordinates.criteria = criteria;
		return strategy;
	}

	private static void parseTag(FindByCoordinates findByCoordinates,
			Strategy strategy, String tag) {
		if (tag == null) {
			return;
		}
		tag = tag.toLowerCase();
		Map<String, String> constraints = new TreeMap<String, String>();
		if (tag.equals("link")) {
			tag = "a";
		} else if (tag.equals("image")) {
			tag = "img";
		} else if (tag.equals("list")) {
			tag = "select";
		} else if (tag.equals("radio button")) {
			tag = "input";
			constraints.put("type", "radio");
		} else if (tag.equals("checkbox")) {
			tag = "input";
			constraints.put("type", "checkbox");
		} else if (tag.equals("text field")) {
			tag = "input";
			constraints.put("type", "text");
		} else if (tag.equals("file upload")) {
			tag = "input";
			constraints.put("type", "file");
		}
		findByCoordinates.tag = tag;
		findByCoordinates.constraints = constraints;
	}

	private static class FindByCoordinates {

		String criteria;
		String tag;
		Map<String, String> constraints;
	}
}
