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

	@RobotKeyword("Submits a form identified by _locator_.\n\n"

	+ "If _locator_ is empty, first form in the page will be submitted. Key "
			+ "attributes for forms are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator=NONE" })
	public void submitForm(String locator) {
		logging.info(String.format("Submitting form '%s'.", locator));
		if (locator == null) {
			locator = "xpath=//form";
		}
		List<WebElement> webElements = element.elementFind(locator, true, true, "form");
		webElements.get(0).submit();
	}

	@RobotKeyword("Verifies checkbox identified by _locator_ is selected/checked.\n\n"

	+ "Key attributes for checkboxes are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void checkboxShouldBeSelected(String locator) {
		logging.info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			throw new Selenium2LibraryNonFatalException(String.format("Checkbox '%s' should have been selected.",
					locator));
		}
	}

	@RobotKeyword("Verifies checkbox identified by _locator_ is not selected/checked.\n\n"

	+ "Key attributes for checkboxes are id and name. See `Introduction` for details " + "about locating elements.\n")
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

	@RobotKeyword("Verifies checkbox identified by _locator_ is found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for checkboxes are id and name. See `Introduction` for details " + "about locating elements.\n")
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

	@RobotKeyword("Verifies checkbox identified by _locator_ is not found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for checkboxes are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainCheckbox(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "checkbox", message, loglevel);
	}

	@RobotKeyword("Selects checkbox identified by _locator_.\n\n"

	+ "Does nothing if checkbox is already selected. Key attributes for checkboxes are id "
			+ "and name. See `Introduction` for details about locating elements.\n")
	@ArgumentNames({ "locator" })
	public void selectCheckbox(String locator) {
		logging.info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			element.click();
		}
	}

	@RobotKeyword("Removes selection of checkbox identified by _locator_.\n\n"

	+ "Does nothing if checkbox is not checked. Key attributes for checkboxes are id "
			+ "and name. See `Introduction` for details about locating elements.\n")
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

	@RobotKeyword("Verifies radio button identified by _locator_ is found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for radio buttons are id, name and value. See `Introduction` for details "
			+ "about locating elements.\n")
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

	@RobotKeyword("Verifies radio button identified by _locator_ is not found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for radio buttons are id, name and value. See `Introduction` for details "
			+ "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainRadioButton(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "radio button", message, loglevel);
	}

	@RobotKeyword("Verifies radio button group identified by _group_name_ has its selection " + "set to _value_.\n\n"

	+ "See `Select Radio Button` for information about how radio buttons are located.\n")
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

	@RobotKeyword("Verifies radio button group identified by _group_name_ has no selection.\n\n"

	+ "See `Select Radio Button` for information about how radio buttons are located.\n")
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

	@RobotKeyword("Sets selection of radio button group identified by _group_name_ to _value_.\n\n"

	+ "The radio button to be selected is located by two arguments:\n"
			+ "_group_name_ is used as the name of the radio input"
			+ "_value_ is used for the value attribute or for the id attribute\n"
			+ "The XPath used to locate the correct radio button then looks like this: "
			+ "//input[@type='radio' and @name='group_name' and (@value='value' " + "or @id='value')]\n\n"

			+ "Examples:\n"
			+ "| Select Radio Button | size | XL | # Matches HTML like <input type=\"radio\" name=\"size\""
			+ " value=\"XL\">XL</input> |\n"
			+ "| Select Radio Button | size | sizeXL | # Matches HTML like <input type=\"radio\" name=\"size\" "
			+ "value=\"XL\" id=\"sizeXL\">XL</input> |\n")
	@ArgumentNames({ "groupName", "value" })
	public void selectRadioButton(String groupName, String value) {
		logging.info(String.format("Selecting '%s' from radio button '%s'.", value, groupName));
		WebElement element = getRadioButtonWithValue(groupName, value);
		if (!element.isSelected()) {
			element.click();
		}
	}

	@RobotKeyword("Inputs the _file_path_ into file input field found by _identifier_.\n\n"

	+ "This keyword is most often used to input files into upload forms. The file specified with "
			+ "_file_path_ must be available on the same host where the Selenium Server is running.\n\n"

			+ "Example:\n" + "| Choose File | my_upload_field | /home/user/files/trades.csv |\n")
	@ArgumentNames({ "locator", "filePath" })
	public void chooseFile(String locator, String filePath) {
		if (!new File(filePath).isFile()) {
			logging.info(String.format("File '%s' does not exist on the local file system", filePath));
		}
		element.elementFind(locator, true, true).get(0).sendKeys(filePath);
	}

	@RobotKeyword("Types the given _password_ into text field identified by _locator_.\n\n"

	+ "Difference between this keyword and `Input Text` is that this keyword does not log the given "
			+ "password. See `Introduction` for details about locating elements.")
	@ArgumentNames({ "locator", "text" })
	public void inputPassword(String locator, String text) {
		logging.info(String.format("Typing password into text field '%s'", locator));
		inputTextIntoTextField(locator, text);
	}

	@RobotKeyword("Types the given _text_ into text field identified by _locator_.\n\n"

	+ "See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Verifies text field identified by _locator_ is found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for text fields are id and name. See `Introduction` for details " + "about locating elements.\n")
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

	@RobotKeyword("Verifies text field identified by _locator_ is not found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for text fields are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainTextfield(String locator, String message, String loglevel) {
		element.pageShouldNotContainElement(locator, "text field", message, loglevel);
	}

	@RobotKeywordOverload
	public void textfieldShouldContain(String locator, String expected) {
		textfieldShouldContain(locator, expected, "");
	}

	@RobotKeyword("Verifies text field identified by _locator_ contains text _expected_.\n\n"

	+ "_message_ can be used to override default error message.\n\n"

	+ "Key attributes for text fields are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator", "expected", "message=NONE" })
	public void textfieldShouldContain(String locator, String expected, String message) {
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(expected)) {
			if (message == null) {
				message = String.format("Text field '%s' should have contained text '%s' but it contained '%s'",
						locator, expected, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, expected));
	}

	@RobotKeywordOverload
	public void textfieldValueShouldBe(String locator, String expected) {
		textfieldValueShouldBe(locator, expected, "");
	}

	@RobotKeyword("Verifies the value in text field identified by _locator_ is exactly " + "_expected_.\n\n"

	+ "_message_ can be used to override default error message.\n\n"

	+ "Key attributes for text fields are id and name. See `Introduction` for details " + "about locating elements.\n")
	@ArgumentNames({ "locator", "expected", "message=NONE" })
	public void textfieldValueShouldBe(String locator, String expected, String message) {
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(expected)) {
			if (message == null) {
				message = String.format("Value of text field '%s' should have been '%s' but was '%s'", locator,
						expected, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text field '%s' is '%s'.", locator, expected));
	}

	@RobotKeyword("Clicks a button identified by _locator_.\n\n"

	+ "Key attributes for buttons are id, name and value. See `Introduction` for details "
			+ "about locating elements.\n")
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

	@RobotKeyword("Verifies button identified by _locator_ is found on current page.\n\n"

	+ "This keyword searches for buttons created with either _input_ or _button_ tag.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for buttons are id, name and value. See `Introduction` for details "
			+ "about locating elements.\n")
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

	@RobotKeyword("Verifies button identified by _locator_ is not found on current page.\n\n"

	+ "This keyword searches for buttons created with either _input_ or _button_ tag.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_ " + "arguments.\n\n"

	+ "Key attributes for buttons are id, name and value. See `Introduction` for details "
			+ "about locating elements.\n")
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
