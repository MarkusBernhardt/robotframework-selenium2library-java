package com.github.markusbernhardt.selenium2library.keywords;

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
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.ElementFinder;
import com.github.markusbernhardt.selenium2library.utils.Python;

@RobotKeywords
public abstract class Element extends Cookie {

	// ##############################
	// Keywords - Element Lookups
	// ##############################

	@RobotKeywordOverload
	@ArgumentNames({ "text" })
	public void currentFrameContains(String text) {
		this.currentFrameContains(text, "INFO");
	}

	@RobotKeyword("Verifies that current frame contains _text_.\n\n"

	+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n")
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void currentFrameContains(String text, String logLevel) {
		if (!isTextPresent(text)) {
			log(String.format("Current Frame Contains: %s => FAILED", text),
					logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have contained text '%s', but did not.", text));
		} else {
			log(String.format("Current Frame Contains: %s => OK", text),
					logLevel);
		}
	}

	@RobotKeywordOverload
	public void currentFrameShouldNotContain(String text) {
		this.currentFrameShouldNotContain(text, "INFO");
	}

	@RobotKeyword("Verifies that current frame does not contain _text_.\n\n"

	+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n")
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void currentFrameShouldNotContain(String text, String logLevel) {
		if (isTextPresent(text)) {
			log(String.format("Current Frame Should Not Contain: %s => FAILED",
					text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have not contained text '%s', but did.", text));
		} else {
			log(String.format("Current Frame Should Not Contain: %s => OK",
					text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void elementShouldContain(String locator, String expected) {
		this.elementShouldContain(locator, expected, "");
	}

	@RobotKeyword("Verifies that the element identified by _locator_ is NOT visible.\n\n"

			+ "This is the opposite of `Element Should Be Visible`.\n\n"

			+ "_message_ can be used to override the default error message.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` "
			+ "for details about locating elements.\n")
	@ArgumentNames({ "locator", "expected", "message=NONE" })
	public void elementShouldContain(String locator, String expected,
			String message) {
		String actual = fetchText(locator);

		if (!actual.toLowerCase().contains(expected.toLowerCase())) {
			info(String
					.format("Element Should Contain: %s => FAILED", expected));
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Element should have contained text '%s' but its text was %s.",
							expected, actual));
		} else {
			info(String.format("Element Should Contain: %s => OK", expected));
		}
	}

	@RobotKeywordOverload
	public void frameShouldContain(String locator, String text) {
		this.frameShouldContain(locator, text, "INFO");
	}

	@RobotKeyword("Verifies frame identified by _locator_ contains _text_.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for frames are id and name. See `Introduction` for details about "
			+ "locating elements.\n")
	@ArgumentNames({ "locator", "text", "loglevel=INFO" })
	public void frameShouldContain(String locator, String text, String logLevel) {
		if (!frameContains(locator, text)) {
			log(String.format("Frame Should Contain: %s => FAILED", text),
					logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Frame should have contained text '%s' but did not.", text));
		} else {
			log(String.format("Frame Should Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldContain(String text) {
		this.pageShouldContain(text, "INFO");
	}

	@RobotKeyword("Verifies that current page contains _text_.\n\n"

			+ "If this keyword fails, it automatically logs the page source using the log level "
			+ "specified with the optional loglevel argument. Giving NONE as level disables "
			+ "logging.\n")
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void pageShouldContain(String text, String logLevel) {
		if (!pageContains(text)) {
			log(String.format("Page Should Contain: %s => FAILED", text),
					logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have contained text '%s' but did not.", text));
		} else {
			log(String.format("Page Should Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldContainElement(String locator) {
		this.pageShouldContainElement(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldContainElement(String locator, String message) {
		this.pageShouldContainElement(locator, message, "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldContainElement(String locator, String message,
			String logLevel) {
		this.pageShouldContainElement(locator, null, message, "INFO");
	}

	@RobotKeyword("Verifies element identified by _locator_ is found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "text", "tag=NONE", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainElement(String locator, String tag,
			String message, String logLevel) {
		helperPageShouldContainElement(locator, tag, message, logLevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContain(String text) {
		this.pageShouldNotContain(text, "INFO");
	}

	@RobotKeyword("Verifies the current page does not contain _text_.\n\n"

	+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n")
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void pageShouldNotContain(String text, String logLevel) {
		if (pageContains(text)) {
			log(String.format("Page Should Not Contain: %s => FAILED", text),
					logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should not have contained text '%s' but did.", text));
		} else {
			log(String.format("Page Should Not Contain: %s => OK", text),
					logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldNotContainElement(String locator) {
		this.pageShouldNotContainElement(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainElement(String locator, String message) {
		this.pageShouldNotContainElement(locator, message, "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainElement(String locator, String message,
			String logLevel) {
		this.pageShouldNotContainElement(locator, null, message, "INFO");
	}

	@RobotKeyword("Verifies element identified by _locator_ is not found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "text", "tag=", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainElement(String locator, String tag,
			String message, String logLevel) {
		helperPageShouldNotContainElement(locator, tag, message, logLevel);
	}

	// ##############################
	// Keywords - Attributes
	// ##############################

	@RobotKeyword("Assigns a temporary identifier to element specified by _locator_.\n\n"

			+ "This is mainly useful if the locator is complicated/slow XPath expression. Identifier "
			+ "expires when the page is reloaded.\n\n"

			+ "Example:\n"
			+ "| Assign ID to Element | xpath=//div[@id=\"first_div\"] | my id |\n"
			+ "| Page Should Contain Element | my id |\n")
	@ArgumentNames({ "locator", "id" })
	public void assignIdToElement(String locator, String id) {
		info(String.format("Assigning temporary id '%s' to element '%s'", id,
				locator));
		List<WebElement> elements = elementFind(locator, true, true);

		((JavascriptExecutor) webDriverCache.getCurrent()).executeScript(
				String.format("arguments[0].id = '%s';", id), elements.get(0));
	}

	@RobotKeyword("Verifies that element identified with _locator_ is disabled.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void elementShouldBeDisabled(String locator) {
		if (isEnabled(locator)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element %s is enabled.", locator));
		}
	}

	@RobotKeyword("Verifies that element identified with _locator_ is enabled.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void elementShouldBeEnabled(String locator) {
		if (!isEnabled(locator)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element %s is disabled.", locator));
		}
	}

	@RobotKeywordOverload
	public void elementShouldBeVisible(String locator) {
		this.elementShouldBeVisible(locator, "");
	}

	@RobotKeyword("Verifies that the element identified by _locator_ is visible.\n\n"

			+ "Herein, visible means that the element is logically visible, not optically visible "
			+ "in the current browser viewport. For example, an element that carries display:none "
			+ "is not logically visible, so using this keyword on that element would fail.\n\n"

			+ "_message_ can be used to override the default error message.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldBeVisible(String locator, String message) {
		info(String.format("Verifying element '%s' is visible.", locator));
		boolean visible = isVisible(locator);

		if (!visible) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should be visible, but it is not.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldNotBeVisible(String locator) {
		this.elementShouldNotBeVisible(locator, "");
	}

	@RobotKeyword("Verifies that the element identified by _locator_ is NOT visible.\n\n"

			+ "This is the opposite of `Element Should Be Visible`.\n\n"

			+ "_message_ can be used to override the default error message.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldNotBeVisible(String locator, String message) {
		info(String.format("Verifying element '%s' is not visible.", locator));
		boolean visible = isVisible(locator);

		if (visible) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should not be visible, but it is.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementTextShouldBe(String locator, String expected) {
		this.elementTextShouldBe(locator, expected, "");
	}

	@RobotKeyword("Verifies element identified by _locator_ exactly contains text expected.\n\n"

			+ "In contrast to `Element Should Contain`, this keyword does not try a substring match but "
			+ "an exact match on the element identified by locator.\n\n"

			+ "_message_ can be used to override the default error message.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "expected", "message=NONE" })
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
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeyword("Return value of element attribute.\n\n"

			+ "_attribute_locator_ consists of element locator followed by an @ sign and attribute name, "
			+ "for example \"element_id@class\".\n")
	@ArgumentNames({ "attributeLocator" })
	public String getElementAttribute(String attributeLocator) {
		String[] parts = parseAttributeLocator(attributeLocator);

		List<WebElement> elements = elementFind(parts[0], true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element '%s' not found.", parts[0]));
		}

		return elements.get(0).getAttribute(parts[1]);
	}

	@RobotKeyword("Clears the text from element identified by _locator_\n\n"

			+ "NOTE: This keyword does not execute any checks on whether or not the clear method has "
			+ "succeeded, so if any subsequent checks are needed, they should be executed using method "
			+ "`Element Text Should Be`.\n\n"

			+ "Also, this method will use WebDriver's internal _element.clear()_ method, i.e. it will not "
			+ "send any keypresses, and it will not have any effect whatsoever on elements other than input "
			+ "textfields or input textareas. Clients relying on keypresses should implement their own methods.")
	@ArgumentNames({ "locator" })
	public void clearElementText(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		elements.get(0).clear();
	}

	@RobotKeyword("Returns horizontal position of element identified by _locator_.\n\n"

			+ "The position is returned in pixels off the left side of the page, as an integer. Fails if a "
			+ "matching element is not found.\n\n"

			+ "See also `Get Vertical Position`.\n")
	@ArgumentNames({ "locator" })
	public int getHorizontalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getX();
	}

	@RobotKeywordOverload
	public String getValue(String locator) {
		return getValue(locator, null);
	}

	@RobotKeyword("Returns the value attribute of element identified by _locator_.\n\n"

			+ "See `Introduction` for details about locating elements.\n")
	@ArgumentNames({ "locator", "tag=NONE" })
	public String getValue(String locator, String tag) {
		return this.fetchValue(locator, tag);
	}

	@RobotKeyword("Returns the text value of element identified by _locator_.\n\n"

			+ "See `Introduction` for details about locating elements.\n")
	@ArgumentNames({ "locator" })
	public String getText(String locator) {
		return fetchText(locator);
	}

	@RobotKeyword("Returns vertical position of element identified by _locator_.\n\n"

			+ "The position is returned in pixels off the top of the page, as an integer. Fails if a "
			+ "matching element is not found.\n\n"

			+ "See also `Get Horizontal Position`.\n")
	@ArgumentNames({ "locator" })
	public int getVerticalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getY();
	}

	// ##############################
	// Keywords - Mouse Input/Events
	// ##############################

	@RobotKeyword("Click element identified by _locator_.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void clickElement(String locator) {
		info(String.format("Clicking element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true);

		elements.get(0).click();
	}

	@RobotKeyword("Double-Click element identified by _locator_.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void doubleClickElement(String locator) {
		info(String.format("Double clicking element '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, true);
		Actions action = new Actions(webDriverCache.getCurrent());

		action.doubleClick(elements.get(0)).perform();
	}

	@RobotKeyword("Sets focus to element identified by _locator_.\n")
	@ArgumentNames({ "locator" })
	public void focus(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);
		((JavascriptExecutor) webDriverCache.getCurrent()).executeScript(
				"arguments[0].focus();", elements.get(0));
	}

	@RobotKeyword("Drags element identified with _source_ which is a locator.\n\n"

			+ "Element can be moved on top of another element with _target_ argument.\n\n"

			+ "_target_ is a locator of the element where the dragged object is dropped.\n\n"

			+ "Examples:\n"
			+ "| Drag And Drop | elem1 | elem2 | # Move elem1 over elem2. |\n")
	@ArgumentNames({ "source", "target" })
	public void dragAndDrop(String source, String target) {
		List<WebElement> sourceElements = elementFind(source, true, true);
		List<WebElement> targetElements = elementFind(target, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.dragAndDrop(sourceElements.get(0), targetElements.get(0))
				.perform();
	}

	@RobotKeyword("Drags element identified with _source_ which is a locator.\n\n"

			+ "Element will be moved by xoffset and yoffset. each of which is a negative or positive "
			+ "number specify the offset.\n\n"

			+ "Examples:\n"
			+ "| Drag And Drop | myElem | 50 | 35 | # Move myElem 50px right and 35px down. |\n")
	@ArgumentNames({ "source", "xOffset", "yOffset" })
	public void dragAndDropByOffset(String source, int xOffset, int yOffset) {
		List<WebElement> elements = elementFind(source, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.dragAndDropBy(elements.get(0), xOffset, yOffset).perform();
	}

	@RobotKeyword("Simulates pressing the left mouse button on the element specified by _locator_.\n\n"

			+ "The element is pressed without releasing the mouse button.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details about "
			+ "locating elements.\n\n"

			+ "See also the more specific keywords `Mouse Down On Image` and `Mouse Down On Link`.\n")
	@ArgumentNames({ "locator" })
	public void mouseDown(String locator) {
		info(String.format("Simulating Mouse Down on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"ERROR: Element %s not found.", locator));
		}

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	@RobotKeyword("Simulates moving mouse away from the element specified by _locator_.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details about "
			+ "locating elements.\n")
	@ArgumentNames({ "locator" })
	public void mouseOut(String locator) {
		info(String.format("Simulating Mouse Out on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Dimension size = element.getSize();
		int offsetX = size.getWidth() / 2 + 1;
		int offsetY = size.getHeight() / 2 + 1;

		Actions action = new Actions(webDriverCache.getCurrent());
		action.moveToElement(element).moveByOffset(offsetX, offsetY).perform();
	}

	@RobotKeyword("Simulates moving mouse away from the element specified by _locator_.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details about "
			+ "locating elements.\n")
	@ArgumentNames({ "locator" })
	public void mouseOver(String locator) {
		info(String.format("Simulating Mouse Over on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(webDriverCache.getCurrent());
		action.moveToElement(element).perform();
	}

	@RobotKeyword("Simulates releasing the left mouse button on the element specified by _locator_.\n\n"

			+ "Key attributes for arbitrary elements are id and name. See `Introduction` for details about "
			+ "locating elements.\n")
	@ArgumentNames({ "locator" })
	public void mouseUp(String locator) {
		info(String.format("Simulating Mouse Up on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(element).release(element).perform();
	}

	@RobotKeyword("Opens context menu on element identified by _locator_.\n")
	@ArgumentNames({ "locator" })
	public void openContextMenu(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		Actions action = new Actions(webDriverCache.getCurrent());
		action.contextClick(elements.get(0)).perform();
	}

	@RobotKeyword("Simulates _event_ on element identified by _locator._\n\n"

			+ "This keyword is useful if element has OnEvent handler that needs to be explicitly "
			+ "invoked.\n\n"

			+ "See `Introduction` for details about locating elements.\n")
	@ArgumentNames({ "locator", "event" })
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

	@RobotKeyword("Simulates user pressing _key_ on element identified by _locator._\n\n"

			+ "_key_ is either a single character, or a numerical ASCII code of the key lead by "
			+ "'\\'.\n\n"

			+ "Examples:\n"
			+ "| Press Key | text_field | q |\n"
			+ "| Press Key | login_button | \\13 | # ASCII code for enter key |\n")
	@ArgumentNames({ "locator", "key" })
	public void pressKey(String locator, String key) {
		if (key.startsWith("\\") && key.length() > 1) {
			key = mapAsciiKeyCodeToKey(Integer.parseInt(key.substring(1)))
					.toString();
		}
		List<WebElement> element = elementFind(locator, true, true);
		element.get(0).sendKeys(key);
	}

	// ##############################
	// Keywords - Links
	// ##############################

	@RobotKeyword("Clicks a link identified by _locator_.\n\n"

			+ "Key attributes for links are id, name, href and link text. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void clickLink(String locator) {
		info(String.format("Clicking link '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true, "a");

		elements.get(0).click();
	}

	@RobotKeyword("Returns a list containing ids of all links found in current page.\n\n"

			+ "If a link has no id, an empty string will be in the list instead.\n")
	public ArrayList<String> getAllLinks() {
		ArrayList<String> ret = new ArrayList<String>();

		List<WebElement> elements = elementFind("tag=a", false, false, "a");
		for (WebElement element : elements) {
			ret.add(element.getAttribute("id"));
		}

		return ret;
	}

	@RobotKeyword("Simulates a mouse down event on a link identified by _locator_.\n\n"

			+ "Key attributes for links are id, name, href and link text. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void mouseDownOnLink(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "link");

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldContainLink(String locator) {
		this.pageShouldContainLink(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldContainLink(String locator, String message) {
		this.pageShouldContainLink(locator, message, "INFO");
	}

	@RobotKeyword("Verifies element identified by _locator_ is found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for links are id, name, href and link text. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainLink(String locator, String message,
			String logLevel) {
		this.pageShouldContainElement(locator, "link", message, logLevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainLink(String locator) {
		this.pageShouldNotContainLink(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainLink(String locator, String message) {
		this.pageShouldNotContainLink(locator, message, "INFO");
	}

	@RobotKeyword("Verifies element identified by _locator_ is not found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for links are id, name, href and link text. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainLink(String locator, String message,
			String logLevel) {
		this.pageShouldNotContainElement(locator, "link", message, logLevel);
	}

	// ##############################
	// Keywords - Images
	// ##############################

	@RobotKeyword("Clicks an image found by _locator_.\n\n"

			+ "Key attributes for images are id, src and alt. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void clickImage(String locator) {
		info(String.format("Clicking image '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, false, "image");

		if (elements.size() == 0) {
			elements = elementFind(locator, true, true, "input");
		}
		WebElement element = elements.get(0);
		element.click();
	}

	@RobotKeyword("Simulates a mouse down event on an image found by _locator_.\n\n"

			+ "Key attributes for images are id, src and alt. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void mouseDownOnImage(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "image");

		Actions action = new Actions(webDriverCache.getCurrent());
		action.clickAndHold(elements.get(0)).perform();
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldContainImage(String locator) {
		this.pageShouldContainImage(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldContainImage(String locator, String message) {
		this.pageShouldContainImage(locator, message, "INFO");
	}

	@RobotKeyword("Verifies image identified by _locator_ is found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for images are id, src and alt. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainImage(String locator, String message,
			String logLevel) {
		pageShouldContainElement(locator, "image", message, logLevel);
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldNotContainImage(String locator) {
		this.pageShouldNotContainImage(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldNotContainImage(String locator, String message) {
		this.pageShouldNotContainImage(locator, message, "INFO");
	}

	@RobotKeyword("Verifies image identified by _locator_ is not found on the current page.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain` for explanation about _loglevel_ argument.\n\n"

			+ "Key attributes for images are id, src and alt. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainImage(String locator, String message,
			String logLevel) {
		pageShouldNotContainElement(locator, "image", message, logLevel);
	}

	// ##############################
	// Keywords - Xpath
	// ##############################

	@RobotKeyword("Returns number of elements matching xpath.\n\n"

			+ "If you wish to assert the number of matching elements, use `Xpath Should Match X Times`.\n")
	@ArgumentNames({ "xpath" })
	public int getMatchingXpathCount(String xpath) {
		if (!xpath.startsWith("xpath=")) {
			xpath = "xpath=" + xpath;
		}
		List<WebElement> elements = elementFind(xpath, false, false);

		return elements.size();
	}

	@RobotKeywordOverload
	@ArgumentNames({ "xpath", "expectedXpathCount" })
	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount) {
		this.xpathShouldMatchXTimes(xpath, expectedXpathCount, "");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "xpath", "expectedXpathCount", "message=NONE" })
	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount,
			String message) {
		this.xpathShouldMatchXTimes(xpath, expectedXpathCount, message, "INFO");
	}

	@RobotKeyword("Verifies that the page contains the given number of elements located by the given xpath.\n\n"

			+ "_message_ can be used to override default error message.\n\n"

			+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ arguments.\n")
	@ArgumentNames({ "xpath", "expectedXpathCount", "message=NONE",
			"logLevel=INFO" })
	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount,
			String message, String logLevel) {
		if (!xpath.startsWith("xpath=")) {
			xpath = "xpath=" + xpath;
		}
		List<WebElement> elements = elementFind(xpath, false, false);
		int actualXpathCount = elements.size();

		if (actualXpathCount != expectedXpathCount) {
			if (message == null || message.equals("")) {
				message = String
						.format("Xpath %s should have matched %s times but matched %s times.",
								xpath, expectedXpathCount, actualXpathCount);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}

		log(String.format("Current page contains %s elements matching '%s'.",
				actualXpathCount, xpath), logLevel);
	}

	// ##############################
	// Internal Methods
	// ##############################

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
			throw new Selenium2LibraryNonFatalException(
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
			throw new Selenium2LibraryNonFatalException(String.format(
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
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Attribute locator '%s' does not contain an element locator.",
							attributeLocator));
		}
		if (index + 1 == attributeLocator.length()) {
			throw new Selenium2LibraryNonFatalException(
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
			throw new Selenium2LibraryNonFatalException(message);
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
			throw new Selenium2LibraryNonFatalException(message);
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
					Python.join("', \"'\", '", Arrays.asList(partsWoApos)));
		}
		if (value.contains("'")) {
			return String.format("\"%s\"", value);
		}
		return String.format("'%s'", value);
	}

	// ##############################
	// Forward Declarations
	// ##############################

	protected abstract boolean isFormElement(WebElement element);
}
