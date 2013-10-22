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
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.ElementFinder;
import com.github.markusbernhardt.selenium2library.utils.Python;

@RobotKeywords
public class Element extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated FormElement keyword bean
	 */
	@Autowired
	protected FormElement formElement;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	// ##############################
	// Keywords - Element Lookups
	// ##############################

	@RobotKeywordOverload
	@ArgumentNames({ "text" })
	public void currentFrameContains(String text) {
		currentFrameContains(text, "INFO");
	}

	/**
	 * Verifies the current frame contains <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about log levels.<br>
	 * 
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void currentFrameContains(String text, String logLevel) {
		if (!isTextPresent(text)) {
			logging.log(String.format("Current Frame Contains: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have contained text '%s', but did not.", text));
		} else {
			logging.log(String.format("Current Frame Contains: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void currentFrameShouldNotContain(String text) {
		currentFrameShouldNotContain(text, "INFO");
	}

	/**
	 * Verifies the current frame does not contain <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about log levels.<br>
	 * 
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void currentFrameShouldNotContain(String text, String logLevel) {
		if (isTextPresent(text)) {
			logging.log(String.format("Current Frame Should Not Contain: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have not contained text '%s', but did.", text));
		} else {
			logging.log(String.format("Current Frame Should Not Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void elementShouldContain(String locator, String text) {
		elementShouldContain(locator, text, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> contains <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about locators.
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void elementShouldContain(String locator, String text, String message) {
		String actual = getText(locator);

		if (!actual.toLowerCase().contains(text.toLowerCase())) {
			logging.info(String.format("Element Should Contain: %s => FAILED", text));
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element should have contained text '%s', but its text was %s.", text, actual));
		} else {
			logging.info(String.format("Element Should Contain: %s => OK", text));
		}
	}

	@RobotKeywordOverload
	public void elementShouldNotContain(String locator, String text) {
		elementShouldNotContain(locator, text, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> does not contain
	 * <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about locators.
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void elementShouldNotContain(String locator, String text, String message) {
		String actual = getText(locator);

		if (actual.toLowerCase().contains(text.toLowerCase())) {
			logging.info(String.format("Element Should Not Contain: %s => FAILED", text));
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element should not have contained text '%s', but its text was %s.", text, actual));
		} else {
			logging.info(String.format("Element Should Not Contain: %s => OK", text));
		}
	}

	@RobotKeywordOverload
	public void frameShouldContain(String locator, String text) {
		frameShouldContain(locator, text, "INFO");
	}

	/**
	 * Verifies the frame identified by <b>locator</b> contains <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about locators.
	 * 
	 * @param locator
	 *            The locator to locate the frame.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "loglevel=INFO" })
	public void frameShouldContain(String locator, String text, String logLevel) {
		if (!frameContains(locator, text)) {
			logging.log(String.format("Frame Should Contain: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Frame should have contained text '%s', but did not.", text));
		} else {
			logging.log(String.format("Frame Should Contain: %s => OK", text), logLevel);
		}
	}

	/**
	 * Verifies the frame identified by <b>locator</b> does not contain
	 * <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about locators.
	 * 
	 * @param locator
	 *            The locator to locate the frame.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "loglevel=INFO" })
	public void frameShouldNotContain(String locator, String text, String logLevel) {
		if (frameContains(locator, text)) {
			logging.log(String.format("Frame Should Not Contain: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Frame should not have contained text '%s', but did.", text));
		} else {
			logging.log(String.format("Frame Should Not Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldContain(String text) {
		pageShouldContain(text, "INFO");
	}

	/**
	 * Verifies the current page contains <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about log levels.<br>
	 * 
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void pageShouldContain(String text, String logLevel) {
		if (!pageContains(text)) {
			logging.log(String.format("Page Should Contain: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should have contained text '%s' but did not.", text));
		} else {
			logging.log(String.format("Page Should Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldNotContain(String text) {
		pageShouldNotContain(text, "INFO");
	}

	/**
	 * Verifies the current page does not contain <b>text</b>.<br>
	 * <br>
	 * See `Introduction` for details about log levels.<br>
	 * 
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "text", "loglevel=INFO" })
	public void pageShouldNotContain(String text, String logLevel) {
		if (pageContains(text)) {
			logging.log(String.format("Page Should Not Contain: %s => FAILED", text), logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Page should not have contained text '%s' but did.", text));
		} else {
			logging.log(String.format("Page Should Not Contain: %s => OK", text), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldContainElement(String locator) {
		pageShouldContainElement(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldContainElement(String locator, String message) {
		pageShouldContainElement(locator, message, "INFO");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainElement(String locator, String message, String logLevel) {
		pageShouldContainElement(locator, null, message, "INFO");
	}

	protected void pageShouldContainElement(String locator, String tag, String message, String logLevel) {
		String name = tag != null ? tag : "element";
		if (!isElementPresent(locator, tag)) {
			if (message == null || message.equals("")) {
				message = String.format("Page should have contained %s '%s' but did not", name, locator);
			}
			logging.log(message, logLevel);
			throw new Selenium2LibraryNonFatalException(message);
		} else {
			logging.log(String.format("Current page contains %s '%s'.", name, locator), logLevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldNotContainElement(String locator) {
		pageShouldNotContainElement(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainElement(String locator, String message) {
		pageShouldNotContainElement(locator, message, "INFO");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainElement(String locator, String message, String logLevel) {
		pageShouldNotContainElement(locator, null, message, "INFO");
	}

	protected void pageShouldNotContainElement(String locator, String tag, String message, String logLevel) {
		String name = tag != null ? tag : "element";
		if (isElementPresent(locator, tag)) {
			if (message == null || message.equals("")) {
				message = String.format("Page should not have contained %s '%s' but did", name, locator);
			}
			logging.log(message, logLevel);
			throw new Selenium2LibraryNonFatalException(message);
		} else {
			logging.log(String.format("Current page does not contain %s '%s'.", name, locator), logLevel);
		}
	}

	// ##############################
	// Keywords - Attributes
	// ##############################

	/**
	 * Assigns a temporary identifier to the element identified by
	 * <b>locator</b><br>
	 * <br>
	 * This is mainly useful, when the locator is a complicated and slow XPath
	 * expression. The identifier expires when the page is reloaded.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Assign ID to Element</td>
	 * <td>xpath=//div[@id=\"first_div\"]</td>
	 * <td>my id</td>
	 * </tr>
	 * <tr>
	 * <td>Page Should Contain Element</td>
	 * <td>my id</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param id
	 *            The id to assign.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "id" })
	public void assignIdToElement(String locator, String id) {
		logging.info(String.format("Assigning temporary id '%s' to element '%s'", id, locator));
		List<WebElement> elements = elementFind(locator, true, true);

		((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript(
				String.format("arguments[0].id = '%s';", id), elements.get(0));
	}

	/**
	 * Verifies the element identified by <b>locator</b> is enabled.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void elementShouldBeEnabled(String locator) {
		if (!isEnabled(locator)) {
			throw new Selenium2LibraryNonFatalException(String.format("Element %s is disabled.", locator));
		}
	}

	/**
	 * Verifies the element identified by <b>locator</b> is disabled.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void elementShouldBeDisabled(String locator) {
		if (isEnabled(locator)) {
			throw new Selenium2LibraryNonFatalException(String.format("Element %s is enabled.", locator));
		}
	}

	@RobotKeywordOverload
	public void elementShouldBeSelected(String locator) {
		elementShouldBeSelected(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is selected.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldBeSelected(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is selected.", locator));
		boolean selected = isSelected(locator);

		if (!selected) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should be selected, but it is not.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldNotBeSelected(String locator) {
		elementShouldNotBeSelected(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is not selected.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldNotBeSelected(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is not selected.", locator));
		boolean selected = isSelected(locator);

		if (selected) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should not be selected, but it is.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldBeVisible(String locator) {
		elementShouldBeVisible(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is visible.<br>
	 * <br>
	 * Herein, visible means that the element is logically visible, not
	 * optically visible in the current browser viewport. For example, an
	 * element that carries display:none is not logically visible, so using this
	 * keyword on that element would fail.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldBeVisible(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is visible.", locator));
		boolean visible = isVisible(locator);

		if (!visible) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should be visible, but it is not.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldNotBeVisible(String locator) {
		elementShouldNotBeVisible(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is not visible.<br>
	 * <br>
	 * Herein, visible means that the element is logically visible, not
	 * optically visible in the current browser viewport. For example, an
	 * element that carries display:none is not logically visible, so using this
	 * keyword on that element would fail.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldNotBeVisible(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is not visible.", locator));
		boolean visible = isVisible(locator);

		if (visible) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should not be visible, but it is.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldBeClickable(String locator) {
		elementShouldBeClickable(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is clickable.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldBeClickable(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is clickable.", locator));
		boolean clickable = isClickable(locator);

		if (!clickable) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should be clickable, but it is not.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementShouldNotBeClickable(String locator) {
		elementShouldNotBeClickable(locator, "");
	}

	/**
	 * Verifies the element identified by <b>locator</b> is not clickable.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE" })
	public void elementShouldNotBeClickable(String locator, String message) {
		logging.info(String.format("Verifying element '%s' is not clickable.", locator));
		boolean clickable = isClickable(locator);

		if (clickable) {
			if (message == null || message.equals("")) {
				message = String.format("Element '%s' should not be clickable, but it is.", locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementTextShouldBe(String locator, String expected) {
		elementTextShouldBe(locator, expected, "");
	}

	/**
	 * Verifies the text of the element identified by <b>locator</b> is exactly
	 * <b>text</b>.<br>
	 * <br>
	 * In contrast to `Element Should Contain`, this keyword does not try a
	 * substring match but an exact match on the element identified by locator.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void elementTextShouldBe(String locator, String text, String message) {
		List<WebElement> elements = elementFind(locator, true, true);
		String actual = elements.get(0).getText();

		if (!text.equals(actual)) {
			if (message == null || message.equals("")) {
				message = String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
						text, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void elementTextShouldNotBe(String locator, String expected) {
		elementTextShouldNotBe(locator, expected, "");
	}

	/**
	 * Verifies the text of the element identified by <b>locator</b> is not
	 * exactly <b>text</b>.<br>
	 * <br>
	 * In contrast to `Element Should Not Contain`, this keyword does not try a
	 * substring match but an exact match on the element identified by locator.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void elementTextShouldNotBe(String locator, String text, String message) {
		List<WebElement> elements = elementFind(locator, true, true);
		String actual = elements.get(0).getText();

		if (text.equals(actual)) {
			if (message == null || message.equals("")) {
				message = String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
						text, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	/**
	 * Returns the value of an element attribute.<br>
	 * <br>
	 * The <b>attribute_locator</b> consists of element locator followed by an @
	 * sign and attribute name.<br>
	 * Example: element_id@class<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param attributeLocator
	 *            The attribute locator.
	 * @return The attribute value.
	 */
	@RobotKeyword
	@ArgumentNames({ "attributeLocator" })
	public String getElementAttribute(String attributeLocator) {
		String[] parts = parseAttributeLocator(attributeLocator);

		List<WebElement> elements = elementFind(parts[0], true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format("Element '%s' not found.", parts[0]));
		}

		return elements.get(0).getAttribute(parts[1]);
	}

	/**
	 * Clears the text from element identified by <b>locator</b>.<br>
	 * <br>
	 * This keyword does not execute any checks on whether or not the clear
	 * method has succeeded, so if any subsequent checks are needed, they should
	 * be executed using method `Element Text Should Be`.<br>
	 * <br>
	 * Also, this method will use WebDriver's internal _element.clear()_ method,
	 * i.e. it will not send any keypresses, and it will not have any effect
	 * whatsoever on elements other than input textfields or input textareas.
	 * Clients relying on keypresses should implement their own methods.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void clearElementText(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		elements.get(0).clear();
	}

	/**
	 * Returns horizontal position of element identified by <b>locator</b>.<br>
	 * <br>
	 * The position is returned in pixels off the left side of the page, as an
	 * integer. Fails if the matching element is not found.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @return The horizontal position
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public int getHorizontalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getX();
	}

	/**
	 * Returns the value attribute of the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @return The value attribute of the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public String getValue(String locator) {
		return getValue(locator, null);
	}

	protected String getValue(String locator, String tag) {
		List<WebElement> elements = elementFind(locator, true, false, tag);

		if (elements.size() == 0) {
			return null;
		}

		return elements.get(0).getAttribute("value");
	}

	/**
	 * Returns the text of the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @return The text of the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public String getText(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		if (elements.size() == 0) {
			return null;
		}

		return elements.get(0).getText();
	}

	/**
	 * Returns vertical position of element identified by <b>locator</b>.<br>
	 * <br>
	 * The position is returned in pixels off the top of the page, as an
	 * integer. Fails if the matching element is not found.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @return The vertical position
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public int getVerticalPosition(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Could not determine position for '%s'.", locator));
		}

		return elements.get(0).getLocation().getY();
	}

	// ##############################
	// Keywords - Mouse Input/Events
	// ##############################

	/**
	 * Click on the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void clickElement(String locator) {
		logging.info(String.format("Clicking element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true);

		elements.get(0).click();
	}

	/**
	 * Double-Click on the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void doubleClickElement(String locator) {
		logging.info(String.format("Double clicking element '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, true);
		Actions action = new Actions(browserManagement.getCurrentWebDriver());

		action.doubleClick(elements.get(0)).perform();
	}

	/**
	 * Set the focus to the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void focus(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);
		((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript("arguments[0].focus();",
				elements.get(0));
	}

	/**
	 * Drag the element identified by the locator <b>source</b> and move it on
	 * top of the element identified by the locator <b>target</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Drag And Drop</td>
	 * <td>elem1</td>
	 * <td>elem2</td>
	 * <td># Move elem1 over elem2</td>
	 * </tr>
	 * </table>
	 * 
	 * @param source
	 *            The locator to locate the element to drag.
	 * @param target
	 *            The locator to locate the element where to drop the dragged
	 *            element.
	 */
	@RobotKeyword
	@ArgumentNames({ "source", "target" })
	public void dragAndDrop(String source, String target) {
		List<WebElement> sourceElements = elementFind(source, true, true);
		List<WebElement> targetElements = elementFind(target, true, true);

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.dragAndDrop(sourceElements.get(0), targetElements.get(0)).perform();
	}

	/**
	 * Drag the element identified by the locator <b>source</b> and move it by
	 * <b>xOffset</b> and <b>yOffset</b>.<br>
	 * <br>
	 * Both offsets are specified as negative (left/up) or positive (right/down)
	 * number.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Drag And Drop By Offset</td>
	 * <td>elem1</td>
	 * <td>50</td>
	 * <td>35</td>
	 * <td># Move elem1 50px right and 35px down.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param source
	 *            The locator to locate the element to drag.
	 * @param xOffset
	 *            The horizontal offset in pixel. Negative means left, positive
	 *            right.
	 * @param yOffset
	 *            The vertical offset in pixel. Negative means up, positive
	 *            down.
	 */
	@RobotKeyword
	@ArgumentNames({ "source", "xOffset", "yOffset" })
	public void dragAndDropByOffset(String source, int xOffset, int yOffset) {
		List<WebElement> elements = elementFind(source, true, true);

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.dragAndDropBy(elements.get(0), xOffset, yOffset).perform();
	}

	/**
	 * Simulates pressing the left mouse button on the element identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * The element is pressed without releasing the mouse button.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @see Element#mouseDownOnImage
	 * @see Element#mouseDownOnLink
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseDown(String locator) {
		logging.info(String.format("Simulating Mouse Down on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
		}

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.clickAndHold(elements.get(0)).perform();
	}

	/**
	 * Simulates moving the mouse away from the element identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseOut(String locator) {
		logging.info(String.format("Simulating Mouse Out on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Dimension size = element.getSize();
		int offsetX = size.getWidth() / 2 + 1;
		int offsetY = size.getHeight() / 2 + 1;

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.moveToElement(element).moveByOffset(offsetX, offsetY).perform();
	}

	/**
	 * Simulates moving the mouse over the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseOver(String locator) {
		logging.info(String.format("Simulating Mouse Over on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.moveToElement(element).perform();
	}

	/**
	 * Simulates releasing the left mouse button on the element identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseUp(String locator) {
		logging.info(String.format("Simulating Mouse Up on element '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false);

		if (elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
		}

		WebElement element = elements.get(0);
		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.clickAndHold(element).release(element).perform();
	}

	/**
	 * Opens the context menu on the element identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void openContextMenu(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.contextClick(elements.get(0)).perform();
	}

	/**
	 * Simulates the given <b>event</b> on the element identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * This keyword is especially useful, when the element has an OnEvent
	 * handler that needs to be explicitly invoked.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param event
	 *            The event to invoke.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "event" })
	public void simulate(String locator, String event) {
		List<WebElement> elements = elementFind(locator, true, true);
		String script = "element = arguments[0];" + "eventName = arguments[1];" + "if (document.createEventObject) {"
				+ "return element.fireEvent('on' + eventName, document.createEventObject());" + "}"
				+ "var evt = document.createEvent(\"HTMLEvents\");" + "evt.initEvent(eventName, true, true);"
				+ "return !element.dispatchEvent(evt);";

		((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript(script, elements.get(0), event);
	}

	/**
	 * Simulates pressing <b>key</b> on the element identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Key is either a single character, or a numerical ASCII code of the key
	 * lead by '\\'.<br>
	 * <br>
	 * Key attributes for arbitrary elements are id and name. See `Introduction`
	 * for details about log levels and locators.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Press Key</td>
	 * <td>text_field</td>
	 * <td>q</td>
	 * <td># Press 'q'</td>
	 * </tr>
	 * <tr>
	 * <td>Press Key</td>
	 * <td>login_button</td>
	 * <td>\\13</td>
	 * <td># ASCII code for enter key</td>
	 * </tr>
	 * </table>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @param key
	 *            The key to press.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "key" })
	public void pressKey(String locator, String key) {
		if (key.startsWith("\\") && key.length() > 1) {
			key = mapAsciiKeyCodeToKey(Integer.parseInt(key.substring(1))).toString();
		}
		List<WebElement> element = elementFind(locator, true, true);
		element.get(0).sendKeys(key);
	}

	// ##############################
	// Keywords - Links
	// ##############################

	/**
	 * Click on the link identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary links are id, name, href and link text. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void clickLink(String locator) {
		logging.info(String.format("Clicking link '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true, "a");

		elements.get(0).click();
	}

	/**
	 * Returns a list containing ids of all links found in current page.<br>
	 * <br>
	 * If a link has no id, an empty string will be in the list instead.<br>
	 * 
	 * @return The list of link ids.
	 */
	@RobotKeyword
	public ArrayList<String> getAllLinks() {
		ArrayList<String> ret = new ArrayList<String>();

		List<WebElement> elements = elementFind("tag=a", false, false, "a");
		for (WebElement element : elements) {
			ret.add(element.getAttribute("id"));
		}

		return ret;
	}

	/**
	 * Simulates pressing the left mouse button on the link identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * The element is pressed without releasing the mouse button.<br>
	 * <br>
	 * Key attributes for arbitrary links are id, name, href and link text. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @see Element#mouseDown
	 * @see Element#mouseDownOnImage
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseDownOnLink(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "link");

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.clickAndHold(elements.get(0)).perform();
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldContainLink(String locator) {
		pageShouldContainLink(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldContainLink(String locator, String message) {
		pageShouldContainLink(locator, message, "INFO");
	}

	/**
	 * Verifies the link identified by <b>locator</b> is found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for arbitrary links are id, name, href and link text. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainLink(String locator, String message, String logLevel) {
		pageShouldContainElement(locator, "link", message, logLevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainLink(String locator) {
		pageShouldNotContainLink(locator, "", "INFO");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainLink(String locator, String message) {
		pageShouldNotContainLink(locator, message, "INFO");
	}

	/**
	 * Verifies the link identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for arbitrary links are id, name, href and link text. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainLink(String locator, String message, String logLevel) {
		pageShouldNotContainElement(locator, "link", message, logLevel);
	}

	// ##############################
	// Keywords - Images
	// ##############################

	/**
	 * Click on the image identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for arbitrary images are id, src and alt. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void clickImage(String locator) {
		logging.info(String.format("Clicking image '%s'.", locator));

		List<WebElement> elements = elementFind(locator, true, false, "image");

		if (elements.size() == 0) {
			elements = elementFind(locator, true, true, "input");
		}
		WebElement element = elements.get(0);
		element.click();
	}

	/**
	 * Simulates pressing the left mouse button on the image identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * The element is pressed without releasing the mouse button.<br>
	 * <br>
	 * Key attributes for arbitrary images are id, src and alt. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the element.
	 * @see Element#mouseDown
	 * @see Element#mouseDownOnLink
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void mouseDownOnImage(String locator) {
		List<WebElement> elements = elementFind(locator, true, true, "image");

		Actions action = new Actions(browserManagement.getCurrentWebDriver());
		action.clickAndHold(elements.get(0)).perform();
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldContainImage(String locator) {
		pageShouldContainImage(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldContainImage(String locator, String message) {
		pageShouldContainImage(locator, message, "INFO");
	}

	/**
	 * Verifies the image identified by <b>locator</b> is found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for arbitrary images are id, src and alt. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainImage(String locator, String message, String logLevel) {
		pageShouldContainElement(locator, "image", message, logLevel);
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator" })
	public void pageShouldNotContainImage(String locator) {
		pageShouldNotContainImage(locator, "", "INFO");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "locator", "message=NONE" })
	public void pageShouldNotContainImage(String locator, String message) {
		pageShouldNotContainImage(locator, message, "INFO");
	}

	/**
	 * Verifies the image identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for arbitrary images are id, src and alt. See
	 * `Introduction` for details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainImage(String locator, String message, String logLevel) {
		pageShouldNotContainElement(locator, "image", message, logLevel);
	}

	// ##############################
	// Keywords - Xpath
	// ##############################

	/**
	 * Returns the number of elements located the given <b>xpath</b>.<br>
	 * <br>
	 * If you wish to assert the number of located elements, use `Xpath Should
	 * Match X Times`.<br>
	 * 
	 * @param xpath
	 *            The XPath to match page elements
	 * @return The number of located elements
	 */
	@RobotKeyword
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
		xpathShouldMatchXTimes(xpath, expectedXpathCount, "");
	}

	@RobotKeywordOverload
	@ArgumentNames({ "xpath", "expectedXpathCount", "message=NONE" })
	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount, String message) {
		xpathShouldMatchXTimes(xpath, expectedXpathCount, message, "INFO");
	}

	/**
	 * Verifies that the page contains the <b>expectedXpathCount</b> of elements
	 * located by the given <b>xpath</b>.<br>
	 * 
	 * @param xpath
	 *            The XPath to match page elements
	 * @param expectedXpathCount
	 *            The expected number of located elements
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "xpath", "expectedXpathCount", "message=NONE", "logLevel=INFO" })
	public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount, String message, String logLevel) {
		if (!xpath.startsWith("xpath=")) {
			xpath = "xpath=" + xpath;
		}
		List<WebElement> elements = elementFind(xpath, false, false);
		int actualXpathCount = elements.size();

		if (actualXpathCount != expectedXpathCount) {
			if (message == null || message.equals("")) {
				message = String.format("Xpath %s should have matched %s times but matched %s times.", xpath,
						expectedXpathCount, actualXpathCount);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}

		logging.log(String.format("Current page contains %s elements matching '%s'.", actualXpathCount, xpath),
				logLevel);
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected List<WebElement> elementFind(String locator, boolean firstOnly, boolean required) {
		return elementFind(locator, firstOnly, required, null);
	}

	protected List<WebElement> elementFind(String locator, boolean firstOnly, boolean required, String tag) {
		List<WebElement> elements = ElementFinder.find(browserManagement.getCurrentWebDriver(), locator, tag);

		if (required && elements.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Element locator '%s' did not match any elements.", locator));
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
		WebDriver current = browserManagement.getCurrentWebDriver();
		List<WebElement> elements = elementFind(locator, true, true);

		current.switchTo().frame(elements.get(0));
		logging.info(String.format("Searching for text from frame '%s'.", locator));
		boolean found = isTextPresent(text);
		current.switchTo().defaultContent();

		return found;
	}

	protected boolean isTextPresent(String text) {
		String locator = String.format("xpath=//*[contains(., %s)]", escapeXpathValue(text));

		return isElementPresent(locator);
	}

	protected boolean isEnabled(String locator) {
		List<WebElement> elements = elementFind(locator, true, true);
		WebElement element = elements.get(0);

		if (!formElement.isFormElement(element)) {
			throw new Selenium2LibraryNonFatalException(String.format("ERROR: Element %s is not an input.", locator));
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

	protected boolean isClickable(String locator) {
		List<WebElement> webElements = elementFind(locator, true, false);
		if (webElements.size() == 0) {
			return false;
		}
		WebElement element = webElements.get(0);
		return element.isDisplayed() && element.isEnabled();
	}

	protected boolean isSelected(String locator) {
		List<WebElement> webElements = elementFind(locator, true, false);
		if (webElements.size() == 0) {
			return false;
		}
		WebElement element = webElements.get(0);
		return element.isSelected();
	}

	protected String[] parseAttributeLocator(String attributeLocator) {
		int index = attributeLocator.lastIndexOf('@');
		if (index <= 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Attribute locator '%s' does not contain an element locator.", attributeLocator));
		}
		if (index + 1 == attributeLocator.length()) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Attribute locator '%s' does not contain an attribute name.", attributeLocator));
		}
		String[] parts = new String[2];
		parts[0] = attributeLocator.substring(0, index);
		parts[1] = attributeLocator.substring(index + 1);

		return parts;
	}

	protected boolean isElementPresent(String locator) {
		return isElementPresent(locator, null);
	}

	protected boolean isElementPresent(String locator, String tag) {
		return elementFind(locator, true, false, tag).size() != 0;
	}

	protected boolean pageContains(String text) {
		WebDriver current = browserManagement.getCurrentWebDriver();
		current.switchTo().defaultContent();

		if (isTextPresent(text)) {
			return true;
		}

		List<WebElement> elements = elementFind("xpath=//frame|//iframe", false, false);
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
			return String.format("concat('%s')", Python.join("', \"'\", '", Arrays.asList(partsWoApos)));
		}
		if (value.contains("'")) {
			return String.format("\"%s\"", value);
		}
		return String.format("'%s'", value);
	}

}
