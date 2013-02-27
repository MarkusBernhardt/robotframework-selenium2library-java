package org.robotframework.selenium2library.keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.robotframework.selenium2library.locators.ElementFinder;

public abstract class Element extends Cookie {

	// =================================================================
	// SECTION: ELEMENT - LOOKUPS (LINE 12)
	// =================================================================

	public void currentFrameContains(String text) {
		this.currentFrameContains(text, "INFO");
	}

	public void currentFrameContains(String text, String logLevel) {
		if (!isTextPresent(text)) {
			log(String.format("Current Frame Contains: %s => FAILED", text),
					logLevel);
			throw new AssertionError(String.format(
					"Page should have contained text '%s' but did not.", text));
		} else {
			log(String.format("Current Frame Contains: %s => OK", text),
					logLevel);
		}
	}

	public void elementShouldContain(String locator, String expected) {
		this.elementShouldContain(locator, expected, "");
	}

	public void elementShouldContain(String locator, String expected,
			String message) {
		String actual = fetchText(locator);

		if (!actual.toLowerCase().contains(expected.toLowerCase())) {
			info(String
					.format("Element Should Contain: %s => FAILED", expected));
			throw new AssertionError(
					String.format(
							"Element should have contained text '%s' but its text was %s.",
							expected, actual));
		} else {
			info(String.format("Element Should Contain: %s => OK", expected));
		}
	}

	public void frameShouldContain(String locator, String text) {
		this.frameShouldContain(locator, text, "INFO");
	}

	public void frameShouldContain(String locator, String text, String logLevel) {
		if (!frameContains(locator, text)) {
			log(String.format("Frame Should Contain: %s => FAILED", text),
					logLevel);
			throw new AssertionError(String.format(
					"Frame should have contained text '%s' but did not.", text));
		} else {
			log(String.format("Frame Should Contain: %s => OK", text), logLevel);
		}
	}

	public void pageShouldContain(String text) {
		this.pageShouldContain(text, "INFO");
	}

	public void pageShouldContain(String text, String logLevel) {
		if (!pageContains(text)) {
			log(String.format("Page Should Contain: %s => FAILED", text),
					logLevel);
			throw new AssertionError(String.format(
					"Page should have contained text '%s' but did not.", text));
		} else {
			log(String.format("Page Should Contain: %s => OK", text), logLevel);
		}
	}

	public void pageShouldContainElement(String locator) {
		this.pageShouldContainElement(locator, "", "INFO");
	}

	public void pageShouldContainElement(String locator, String message) {
		this.pageShouldContainElement(locator, message, "INFO");
	}

	public void pageShouldContainElement(String locator, String message,
			String logLevel) {
		this.pageShouldContainElement(locator, null, message, "INFO");
	}

	public void pageShouldContainElement(String locator, String tag,
			String message, String logLevel) {
		helperPageShouldContainElement(locator, tag, message, logLevel);
	}

	public void pageShouldNotContain(String text) {
		this.pageShouldNotContain(text, "INFO");
	}

	public void pageShouldNotContain(String text, String logLevel) {
		if (pageContains(text)) {
			log(String.format("Page Should Not Contain: %s => FAILED", text),
					logLevel);
			throw new AssertionError(String.format(
					"Page should not have contained text '%s' but did.", text));
		} else {
			log(String.format("Page Should Not Contain: %s => OK", text),
					logLevel);
		}
	}

	public void pageShouldNotContainElement(String locator) {
		this.pageShouldNotContainElement(locator, "", "INFO");
	}

	public void pageShouldNotContainElement(String locator, String message) {
		this.pageShouldNotContainElement(locator, message, "INFO");
	}

	public void pageShouldNotContainElement(String locator, String message,
			String logLevel) {
		this.pageShouldNotContainElement(locator, null, message, "INFO");
	}

	public void pageShouldNotContainElement(String locator, String tag,
			String message, String logLevel) {
		helperPageShouldNotContainElement(locator, tag, message, logLevel);
	}

	// =================================================================
	// SECTION: ELEMENT - ATTRIBUTES (LINE 106)
	// =================================================================

	public void assignIdToElement(String locator, String id) {
		info(String.format("Assigning temporary id '%s' to element '%s'", id,
				locator));
		List<WebElement> elements = elementFind(locator, true, true);

		((JavascriptExecutor) webDriverCache.getCurrent()).executeScript(
				String.format("arguments[0].id = '%s';", id), elements.get(0));
	}

	public void elementShouldBeDisabled(String locator) {
		if (isEnabled(locator)) {
			throw new AssertionError(String.format("Element %s is enabled.",
					locator));
		}
	}

	public void elementShouldBeEnabled(String locator) {
		if (!isEnabled(locator)) {
			throw new AssertionError(String.format("Element %s is disabled.",
					locator));
		}
	}

	public void elementShouldBeVisible(String locator) {
		this.elementShouldBeVisible(locator, "");
	}

	public void elementShouldBeVisible(String locator, String message) {
		info(String.format("Verifying element '%s' is visible.", locator));
		boolean visible = isVisible(locator);

		if (!visible) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should be visible, but it is not.",
						locator);
			}
			throw new AssertionError(message);
		}
	}

	public void elementShouldNotBeVisible(String locator) {
		this.elementShouldNotBeVisible(locator, "");
	}

	public void elementShouldNotBeVisible(String locator, String message) {
		info(String.format("Verifying element '%s' is not visible.", locator));
		boolean visible = isVisible(locator);

		if (visible) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should not be visible, but it is.",
						locator);
			}
			throw new AssertionError(message);
		}
	}

	public void elementTextShouldBe(String locator, String expected) {
		this.elementTextShouldBe(locator, expected, "");
	}

	public void elementTextShouldBe(String locator, String expected,
			String message) {
		info(String.format(
				"Verifying element '%s' contains exactly text '%s'.", locator,
				expected));

		List<WebElement> elements = elementFind(locator, true, true);
		String actual = elements.get(0).getText();

		if (!expected.equals(actual)) {
			if (message == null || message.equals("")) {
				message = String
						.format("The text of element '%s' should have been '%s', but it was '%s'.",
								locator, expected, actual);
			}
			throw new AssertionError(message);
		}
	}

	public String getElementAttribute(String attributeLocator) {
		String[] parts = parseAttributeLocator(attributeLocator);

		List<WebElement> elements = elementFind(parts[0], true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format("Element '%s' not found.",
					parts[0]));
		}

		return elements.get(0).getAttribute(parts[1]);
	}

	public int getHorizontalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getX();
	}

	public String getValue(String locator) {
		return getValue(locator, null);
	}

	public String getValue(String locator, String tag) {
		return this.fetchValue(locator, tag);
	}

	public String getText(String locator) {
		return fetchText(locator);
	}

	public int getVerticalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getY();
	}

	// =================================================================
	// SECTION: ELEMENT - MOUSE AND INPUT EVENTS (Line 252)
	// =================================================================

	public void clickElement(String locator) {
		info(String.format("Clicking element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true);

		elements.get(0).click();
	}

	public void doubleClickElement(String locator) {
		info(String.format("Double clicking element '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, true);
		Actions action = new Actions(webDriverCache.getCurrent());

		action.doubleClick(elements.get(0)).perform();
	}

	public void focus(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);
		((JavascriptExecutor) webDriverCache.getCurrent()).executeScript(
				"arguments[0].focus();", elements.get(0));
	}

	public void dragAndDrop(String source, String target) {
		List<WebElement> sourceElements = elementFind(source, true, true);
		List<WebElement> targetElements = elementFind(target, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.dragAndDrop(sourceElements.get(0), targetElements.get(0))
				.perform();
	}

	public void dragAndDropByOffset(String source, int xOffset, int yOffset) {
		List<WebElement> elements = elementFind(source, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.dragAndDropBy(elements.get(0), xOffset, yOffset).perform();
	}

	public void mouseDown(String locator) {
		info(String.format("Simulating Mouse Down on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"ERROR: Element %s not found.", locator));
		}

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	public void mouseOut(String locator) {
		info(String.format("Simulating Mouse Out on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Dimension size = element.getSize();
		int offsetX = size.getWidth() / 2 + 1;
		int offsetY = size.getHeight() / 2 + 1;

		Actions action = new Actions(webDriverCache.getCurrent());
		action.moveToElement(element).moveByOffset(offsetX, offsetY).perform();
	}

	public void mouseOver(String locator) {
		info(String.format("Simulating Mouse Over on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(webDriverCache.getCurrent());
		action.moveToElement(element).perform();
	}

	public void mouseUp(String locator) {
		info(String.format("Simulating Mouse Up on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new AssertionError(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(element).release(element).perform();
	}

	public void openContextMenu(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.contextClick(elements.get(0)).perform();
	}

	public void simulate(String locator, String event) {
		List<WebElement> elements = elementFind(locator, true, true);
		String script = "element = arguments[0];"
				+ "eventName = arguments[1];"
				+ "if (document.createEventObject) {"
				+ "return element.fireEvent('on' + eventName, document.createEventObject());"
				+ "}" + "var evt = document.createEvent(\"HTMLEvents\");"
				+ "evt.initEvent(eventName, true, true);"
				+ "return !element.dispatchEvent(evt);";

		((JavascriptExecutor) webDriverCache.getCurrent()).executeScript(
				script, elements.get(0), event);
	}

	public void pressKey(String locator, String key) {
		if (key.startsWith("\\") && key.length() > 1) {
			key = mapAsciiKeyCodeToKey(Integer.parseInt(key.substring(1)))
					.toString();
		}
		List<WebElement> element = elementFind(locator, true, true);
		element.get(0).sendKeys(key);
	}

	// =================================================================
	// SECTION: ELEMENT - LINKS (Line 252)
	// =================================================================

	public void clickLink(String locator) {
		info(String.format("Clicking link '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true, "a");

		elements.get(0).click();
	}

	public ArrayList<String> getAllLinks() {
		ArrayList<String> ret = new ArrayList<String>();

		List<WebElement> elements = elementFind("tag=a", false, false, "a");
		for (WebElement element : elements) {
			ret.add(element.getAttribute("id"));
		}

		return ret;
	}

	public void mouseDownOnLink(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "link");

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	public void pageShouldContainLink(String locator) {
		this.pageShouldContainLink(locator, "", "INFO");
	}

	public void pageShouldContainLink(String locator, String message) {
		this.pageShouldContainLink(locator, message, "INFO");
	}

	public void pageShouldContainLink(String locator, String message,
			String logLevel) {
		this.pageShouldContainElement(locator, "link", message, logLevel);
	}

	public void pageShouldNotContainLink(String locator) {
		this.pageShouldNotContainLink(locator, "", "INFO");
	}

	public void pageShouldNotContainLink(String locator, String message) {
		this.pageShouldNotContainLink(locator, message, "INFO");
	}

	public void pageShouldNotContainLink(String locator, String message,
			String logLevel) {
		this.pageShouldNotContainElement(locator, "link", message, logLevel);
	}

	// =================================================================
	// SECTION: ELEMENT - IMAGES (LINE 460)
	// =================================================================

	public void clickImage(String locator) {
		info(String.format("Clicking image '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, false, "image");

		if (elements.size() == 0) {
			elements = elementFind(locator, true, true, "input");
		}
		WebElement element = elements.get(0);
		element.click();
	}

	public void mouseDownOnImage(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "image");

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	public void pageShouldContainImage(String locator) {
		this.pageShouldContainImage(locator, "", "INFO");
	}

	public void pageShouldContainImage(String locator, String message) {
		this.pageShouldContainImage(locator, message, "INFO");
	}

	public void pageShouldContainImage(String locator, String message,
			String logLevel) {
		pageShouldContainElement(locator, "image", message, logLevel);
	}

	public void pageShouldNotContainImage(String locator) {
		this.pageShouldNotContainImage(locator, "", "INFO");
	}

	public void pageShouldNotContainImage(String locator, String message) {
		this.pageShouldNotContainImage(locator, message, "INFO");
	}

	public void pageShouldNotContainImage(String locator, String message,
			String logLevel) {
		pageShouldNotContainElement(locator, "image", message, logLevel);
	}

	// =================================================================
	// SECTION: ELEMENT - XPATH (LINE 506)
	// =================================================================

	public int getMatchingXpathCount(String xpath) {
		List<WebElement> elements = elementFind(
				String.format("xpath=%s", xpath), false, false);

		return elements.size();
	}

	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount) {
		this.xpathShouldMatchXTimes(xpath, expectedXpathCount, "");
	}

	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount,
			String message) {
		this.xpathShouldMatchXTimes(xpath, expectedXpathCount, message, "INFO");
	}

	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount,
			String message, String logLevel) {
		List<WebElement> elements = elementFind(
				String.format("xpath=%s", xpath), false, false);
		int actualXpathCount = elements.size();

		if (actualXpathCount != expectedXpathCount) {
			if (message == null || message.equals("")) {
				message = String
						.format("Xpath %s should have matched %s times but matched %s times.",
								xpath, expectedXpathCount, actualXpathCount);
			}
			throw new AssertionError(message);
		}

		log(String.format("Current page contains %s elements matching '%s'.",
				actualXpathCount, xpath), logLevel);
	}

	// =================================================================
	// SECTION: ELEMENT - PROTECTED HELPERS
	// =================================================================

	@Override
	protected List<WebElement> elementFind(String locator, boolean firstOnly,
			boolean required) {
		return elementFind(locator, firstOnly, required, null);
	}

	protected List<WebElement> elementFind(String locator, boolean firstOnly,
			boolean required, String tag) {
		List<WebElement> elements = ElementFinder.find(
				webDriverCache.getCurrent(), locator, tag);

		if (required && elements.size() == 0) {
			throw new IllegalArgumentException(
					String.format(
							"Element locator '%s' did not match any elements.",
							locator));
		}

		if (firstOnly) {
			if (elements.size() > 1) {
				List<WebElement> tmp = new ArrayList<WebElement>();
				tmp.add(elements.get(0));
				elements = tmp;
			}
		}

		return elements;
	}

	protected boolean frameContains(String locator, String text) {
		WebDriver current = webDriverCache.getCurrent();
		List<WebElement> elements = elementFind(locator, true, true);

		current.switchTo().frame(elements.get(0));
		info(String.format("Searching for text from frame '%s'.", locator));
		boolean found = isTextPresent(text);
		current.switchTo().defaultContent();

		return found;
	}

	protected String fetchText(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		if (elements.size() == 0) {
			return null;
		}

		return elements.get(0).getText();
	}

	protected String fetchValue(String locator) {
		return this.fetchValue(locator, null);
	}

	protected String fetchValue(String locator, String tag) {
		List<WebElement> elements = elementFind(locator, true, false, tag);

		if (elements.size() == 0) {
			return null;
		}

		return elements.get(0).getAttribute("value");
	}

	protected boolean isTextPresent(String text) {
		String locator = String.format("xpath=//*[contains(., %s)]",
				escapeXpathValue(text));

		return this.isElementPresent(locator);
	}

	protected boolean isEnabled(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);
		WebElement element = elements.get(0);

		if (!isFormElement(element)) {
			throw new AssertionError(String.format(
					"ERROR: Element %s is not an input.", locator));
		}
		if (!element.isEnabled()) {
			return false;
		}
		String readonly = element.getAttribute("readonly");
		if (readonly.equals("readonly") || readonly.equals("true")) {
			return false;
		}

		return true;
	}

	protected boolean isVisible(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);
		if (elements.size() == 0) {
			return false;
		}
		WebElement element = elements.get(0);
		return element.isDisplayed();
	}

	protected String[] parseAttributeLocator(String attributeLocator) {
		int index = attributeLocator.lastIndexOf('@');
		if (index <= 0) {
			throw new AssertionError(
					String.format(
							"Attribute locator '%s' does not contain an element locator.",
							attributeLocator));
		}
		if (index + 1 == attributeLocator.length()) {
			throw new AssertionError(
					String.format(
							"Attribute locator '%s' does not contain an attribute name.",
							attributeLocator));
		}
		String[] parts = new String[2];
		parts[0] = attributeLocator.substring(0, index);
		parts[1] = attributeLocator.substring(index + 1);

		return parts;
	}

	protected boolean isElementPresent(String locator) {
		return this.isElementPresent(locator, null);
	}

	protected boolean isElementPresent(String locator, String tag) {
		return elementFind(locator, true, false, tag).size() != 0;
	}

	protected boolean pageContains(String text) {
		WebDriver current = webDriverCache.getCurrent();
		current.switchTo().defaultContent();

		if (isTextPresent(text)) {
			return true;
		}

		List<WebElement> elements = elementFind("xpath=//frame|//iframe",
				false, false);
		Iterator<WebElement> it = elements.iterator();
		while (it.hasNext()) {
			current.switchTo().frame(it.next());
			boolean found = isTextPresent(text);
			current.switchTo().defaultContent();
			if (found) {
				return true;
			}
		}

		return false;
	}

	protected void helperPageShouldContainElement(String locator, String tag,
			String message, String logLevel) {
		String name = tag != null ? tag : "element";
		if (!isElementPresent(locator, tag)) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Page should have contained %s '%s' but did not", name,
						locator);
			}
			log(message, logLevel);
			throw new AssertionError(message);
		} else {
			log(String.format("Current page contains %s '%s'.", name, locator),
					logLevel);
		}
	}

	protected void helperPageShouldNotContainElement(String locator,
			String tag, String message, String logLevel) {
		String name = tag != null ? tag : "element";
		if (isElementPresent(locator, tag)) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Page should not have contained %s '%s' but did", name,
						locator);
			}
			log(message, logLevel);
			throw new AssertionError(message);
		} else {
			log(String.format("Current page does not contain %s '%s'.", name,
					locator), logLevel);
		}
	}

	protected CharSequence mapAsciiKeyCodeToKey(int keyCode) {
		switch (keyCode) {
		case 0:
			return Keys.NULL;
		case 8:
			return Keys.BACK_SPACE;
		case 9:
			return Keys.TAB;
		case 10:
			return Keys.RETURN;
		case 13:
			return Keys.ENTER;
		case 24:
			return Keys.CANCEL;
		case 27:
			return Keys.ESCAPE;
		case 32:
			return Keys.SPACE;
		case 42:
			return Keys.MULTIPLY;
		case 43:
			return Keys.ADD;
		case 44:
			return Keys.SEPARATOR;
		case 45:
			return Keys.SUBTRACT;
		case 56:
			return Keys.DECIMAL;
		case 57:
			return Keys.DIVIDE;
		case 59:
			return Keys.SEMICOLON;
		case 61:
			return Keys.EQUALS;
		case 127:
			return Keys.DELETE;
		default:
			return new StringBuffer((char) keyCode);
		}
	}

	public static String escapeXpathValue(String value) {
		if (value.contains("\"") && value.contains("'")) {
			String[] partsWoApos = value.split("'");
			return String.format("concat('%s')",
					join("', \"'\", '", Arrays.asList(partsWoApos)));
		}
		if (value.contains("'")) {
			return String.format("\"%s\"", value);
		}
		return String.format("'%s'", value);
	}

	// =================================================================
	// SECTION: ELEMENT - FORWARD DECLARATIONS
	// =================================================================

	protected abstract boolean isFormElement(WebElement element);
}
