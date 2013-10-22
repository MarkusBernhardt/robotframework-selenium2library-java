package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

@RobotKeywords
public class FormElement extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public void submitForm() {
		submitForm(null);
	}

	/**
	 * Submit the form identified by <b>locator</b>.<br>
	 * <br>
	 * If the locator is empty, the first form in the page will be submitted.<br>
	 * <br>
	 * Key attributes for forms are id and name. See `Introduction` for details
	 * about locators.<br>
	 * 
	 * @param locator
	 *            Default=NONE. The locator to locate the form.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator=NONE" })
	public void submitForm(String locator) {
		logging.info(String.format("Submitting form '%s'.", locator));
		if (locator == null) {
			locator = "xpath=//form";
		}
		List<WebElement> webElements = element.elementFind(locator, true, true, "form");
		webElements.get(0).submit();
	}

	/**
	 * Verify the checkbox identified by <b>locator</b> is selected/checked.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void checkboxShouldBeSelected(String locator) {
		logging.info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			throw new Selenium2LibraryNonFatalException(String.format("Checkbox '%s' should have been selected.",
					locator));
		}
	}

	/**
	 * Verify the checkbox identified by <b>locator</b> is not selected/checked.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void checkboxShouldNotBeSelected(String locator) {
		logging.info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			throw new Selenium2LibraryNonFatalException(String.format("Checkbox '%s' should not have been selected.",
					locator));
		}
	}

	@RobotKeywordOverload
	public void pageShouldContainCheckbox(String locator) {
		pageShouldContainCheckbox(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldContainCheckbox(String locator, String message) {
		pageShouldContainCheckbox(locator, message, "INFO");
	}

	/**
	 * Verify the checkbox identified by <b>locator</b> is found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainCheckbox(String locator, String message, String loglevel) {
		element.pageShouldContainElement(locator, "checkbox", message, loglevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainCheckbox(String locator) {
		pageShouldNotContainCheckbox(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainCheckbox(String locator, String message) {
		pageShouldNotContainCheckbox(locator, message, "INFO");
	}

	/**
	 * Verify the checkbox identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainCheckbox(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "checkbox", message, loglevel);
	}

	/**
	 * Select the checkbox identified by <b>locator</b>.<br>
	 * <br>
	 * Does nothing, if the checkbox is already selected.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void selectCheckbox(String locator) {
		logging.info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			element.click();
		}
	}

	/**
	 * Unselect the checkbox identified by <b>locator</b>.<br>
	 * <br>
	 * Does nothing, if the checkbox is not selected.<br>
	 * <br>
	 * Key attributes for checkboxes are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the checkbox.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void unselectCheckbox(String locator) {
		logging.info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			element.click();
		}
	}

	@RobotKeywordOverload
	public void pageShouldContainRadioButton(String locator) {
		pageShouldContainRadioButton(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldContainRadioButton(String locator, String message) {
		pageShouldContainRadioButton(locator, message, "INFO");
	}

	/**
	 * Verify the radio button identified by <b>locator</b> is found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for radio buttons are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the radio button.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainRadioButton(String locator, String message, String loglevel) {
		element.pageShouldContainElement(locator, "radio button", message, loglevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainRadioButton(String locator) {
		pageShouldNotContainRadioButton(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainRadioButton(String locator, String message) {
		pageShouldNotContainRadioButton(locator, message, "INFO");
	}

	/**
	 * Verify the radio button identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for radio buttons are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the radio button.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainRadioButton(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "radio button", message, loglevel);
	}

	/**
	 * Verify the radio button group identified by <b>groupName</b> has its
	 * selection set to <b>value</b>.<br>
	 * <br>
	 * See `Select Radio Button` for details about locating radio buttons.<br>
	 * 
	 * @param groupName
	 *            The radio button group name.
	 * @param value
	 *            The expected value.
	 */
	@RobotKeyword
	@ArgumentNames({ "groupName", "value" })
	public void radioButtonShouldBeSetTo(String groupName, String value) {
		logging.info(String.format("Verifying radio button '%s' has selection '%s'.", groupName, value));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue == null || !actualValue.equals(value)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Selection of radio button '%s' should have been '%s' but was '%s'", groupName, value, actualValue));
		}
	}

	/**
	 * Verify the radio button group identified by <b>groupName</b> has no
	 * selection.<br>
	 * <br>
	 * See `Select Radio Button` for details about locating radio buttons.<br>
	 * 
	 * @param groupName
	 *            The radio button group name.
	 */
	@RobotKeyword
	@ArgumentNames({ "groupName" })
	public void radioButtonShouldNotBeSelected(String groupName) {
		logging.info(String.format("Verifying radio button '%s' has no selection.", groupName));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue != null) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Radio button group '%s' should not have had selection, but '%s' was selected", groupName,
					actualValue));
		}
	}

	/**
	 * Sets the selection of the radio button group identified by
	 * <b>groupName</b> to <b>value</b>.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Select Radio Button</td>
	 * <td>size</td>
	 * <td>XL</td>
	 * <td># Matches HTML like &lt;input type="radio" name="size"
	 * value="XL"&gt;XL&lt;/input&gt;</td>
	 * </tr>
	 * <tr>
	 * <td>Select Radio Button</td>
	 * <td>size</td>
	 * <td>sizeXL</td>
	 * <td># Matches HTML like &lt;input type="radio" name="size" value="XL"
	 * id="sizeXL"&gt;XL&lt;/input&gt;</td>
	 * </tr>
	 * </table>
	 * 
	 * @param groupName
	 *            The radio button group name.
	 * @param value
	 *            The value or id attribute of the radio button to set.
	 */
	@RobotKeyword
	@ArgumentNames({ "groupName", "value" })
	public void selectRadioButton(String groupName, String value) {
		logging.info(String.format("Selecting '%s' from radio button '%s'.", value, groupName));
		WebElement element = getRadioButtonWithValue(groupName, value);
		if (!element.isSelected()) {
			element.click();
		}
	}

	/**
	 * Types the given <b>filePath</b> into the input field identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * This keyword is most often used to input files into upload forms. The
	 * file specified with filePath must be available on the same host where the
	 * Selenium Server is running.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Choose File</td>
	 * <td>my_upload_field</td>
	 * <td>/home/user/files/trades.csv</td>
	 * </tr>
	 * </table>
	 * Key attributes for input fields are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the input field.
	 * @param filePath
	 *            The file path to input
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "filePath" })
	public void chooseFile(String locator, String filePath) {
		if (!new File(filePath).isFile()) {
			logging.info(String.format("File '%s' does not exist on the local file system", filePath));
		}
		element.elementFind(locator, true, true).get(0).sendKeys(filePath);
	}

	/**
	 * Types the given <b>text</b> into the password field identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Key attributes for input fields are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the password field.
	 * @param text
	 *            The password to input
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text" })
	public void inputPassword(String locator, String text) {
		logging.info(String.format("Typing password into text field '%s'", locator));
		inputTextIntoTextField(locator, text);
	}

	/**
	 * Types the given <b>text</b> into the text field identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Key attributes for input fields are id and name. See `Introduction` for
	 * details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param text
	 *            The password to input
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text" })
	public void inputText(String locator, String text) {
		logging.info(String.format("Typing text '%s' into text field '%s'", text, locator));
		inputTextIntoTextField(locator, text);
	}

	@RobotKeywordOverload
	public void pageShouldContainTextfield(String locator) {
		pageShouldContainTextfield(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldContainTextfield(String locator, String message) {
		pageShouldContainTextfield(locator, message, "INFO");
	}

	/**
	 * Verify the text field identified by <b>locator</b> is found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainTextfield(String locator, String message, String loglevel) {
		element.pageShouldContainElement(locator, "text field", message, loglevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainTextfield(String locator) {
		pageShouldNotContainTextfield(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainTextfield(String locator, String message) {
		pageShouldNotContainTextfield(locator, message, "INFO");
	}

	/**
	 * Verify the text field identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainTextfield(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "text field", message, loglevel);
	}

	@RobotKeywordOverload
	public void textfieldShouldContain(String locator, String text) {
		textfieldShouldContain(locator, text, "");
	}

	@RobotKeywordOverload
	public void textfieldValueShouldBe(String locator, String text) {
		textfieldValueShouldBe(locator, text, "");
	}

	/**
	 * Verify the text field identified by <b>locator</b> is exactly
	 * <b>text</b>.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * 
	 * @see BrowserManagement#textfieldShouldNotBe
	 * @see BrowserManagement#textfieldShouldContain
	 * @see BrowserManagement#textfieldShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldValueShouldBe(String locator, String text, String message) {
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text field '%s' should have been '%s' but was '%s'", locator, text,
						actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text field '%s' is '%s'.", locator, text));
	}

	/**
	 * Verify the text field identified by <b>locator</b> is not exactly
	 * <b>text</b>.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * 
	 * @see BrowserManagement#textfieldShouldBe
	 * @see BrowserManagement#textfieldShouldContain
	 * @see BrowserManagement#textfieldShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldValueShouldNotBe(String locator, String text, String message) {
		String actual = element.getValue(locator, "text field");
		if (actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text field '%s' should not have been '%s' but was '%s'", locator, text,
						actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text field '%s' is '%s'.", locator, text));
	}

	/**
	 * Verify the text field identified by <b>locator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * 
	 * @see FormElement#textfieldShouldBe
	 * @see FormElement#textfieldShouldNotBe
	 * @see FormElement#textfieldShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldShouldContain(String locator, String text, String message) {
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Text field '%s' should have contained text '%s', but was '%s'", locator, text,
						actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

	@RobotKeywordOverload
	public void textfieldShouldNotContain(String locator, String text) {
		textfieldShouldNotContain(locator, text, "");
	}

	/**
	 * Verify the text field identified by <b>locator</b> does not contain
	 * <b>text</b>.<br>
	 * <br>
	 * Key attributes for text field are id and name. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the text field.
	 * @param text
	 *            The text to verify.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * 
	 * @see FormElement#textfieldShouldBe
	 * @see FormElement#textfieldShouldNotBe
	 * @see FormElement#textfieldShouldContain
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldShouldNotContain(String locator, String text, String message) {
		String actual = element.getValue(locator, "text field");
		if (actual.contains(text)) {
			if (message == null) {
				message = String.format("Text field '%s' should not have contained text '%s', but was '%s'", locator,
						text, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

	/**
	 * Click on the button identified by <b>locator</b>.<br>
	 * <br>
	 * Key attributes for buttons are id, name and value. See
	 * `Introduction` for details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the link.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void clickButton(String locator) {
		logging.info(String.format("Clicking button '%s'.", locator));
		List<WebElement> elements = element.elementFind(locator, true, false, "input");
		if (elements.size() == 0) {
			elements = element.elementFind(locator, true, true, "button");
		}
		elements.get(0).click();
	}

	@RobotKeywordOverload
	public void pageShouldContainButton(String locator) {
		pageShouldContainButton(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldContainButton(String locator, String message) {
		pageShouldContainButton(locator, message, "INFO");
	}

	/**
	 * Verify the button identified by <b>locator</b> is found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for buttons are id, name and value. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the button.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldContainButton(String locator, String message, String loglevel) {
		try {
			element.pageShouldContainElement(locator, "input", message, loglevel);
		} catch (AssertionError ae) {
			element.pageShouldContainElement(locator, "button", message, loglevel);
		}
	}

	@RobotKeywordOverload
	public void pageShouldNotContainButton(String locator) {
		pageShouldNotContainButton(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainButton(String locator, String message) {
		pageShouldNotContainButton(locator, message, "INFO");
	}

	/**
	 * Verify the button identified by <b>locator</b> is not found on the current
	 * page.<br>
	 * <br>
	 * Key attributes for buttons are id, name and value. See `Introduction` for
	 * details about log levels and locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the button.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainButton(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "input", message, loglevel);
		element.pageShouldNotContainElement(locator, "button", message, loglevel);
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected WebElement getCheckbox(String locator) {
		return element.elementFind(locator, true, true, "input").get(0);
	}

	protected List<WebElement> getRadioButtons(String groupName) {
		String xpath = String.format("xpath=//input[@type='radio' and @name='%s']", groupName);
		logging.debug("Radio group locator: " + xpath);
		return element.elementFind(xpath, false, true);
	}

	protected WebElement getRadioButtonWithValue(String groupName, String value) {
		String xpath = String.format("xpath=//input[@type='radio' and @name='%s' and (@value='%s' or @id='%s')]",
				groupName, value, value);
		logging.debug("Radio group locator: " + xpath);
		return element.elementFind(xpath, true, true).get(0);
	}

	protected String getValueFromRadioButtons(List<WebElement> elements) {
		for (WebElement element : elements) {
			if (element.isSelected()) {
				return element.getAttribute("value");
			}
		}
		return null;
	}

	protected void inputTextIntoTextField(String locator, String text) {
		WebElement webElement = element.elementFind(locator, true, true).get(0);
		webElement.clear();
		webElement.sendKeys(text);
	}

	protected boolean isFormElement(WebElement element) {
		if (element == null) {
			return false;
		}
		String tag = element.getTagName().toLowerCase();
		return tag == "input" || tag == "select" || tag == "textarea" || tag == "button" || tag == "option";
	}

}
