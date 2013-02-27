package org.robotframework.selenium2library.keywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;

public abstract class FormElement extends Element {

	// =================================================================
	// SECTION: FORMELEMENT - KEYWORDS
	// =================================================================

	public void submitForm() {
		submitForm(null);
	}

	public void submitForm(String locator) {
		info(String.format("Submitting form '%s'.", locator));
		if (locator == null) {
			locator = "xpath=//form";
		}
		List<WebElement> element = elementFind(locator, true, true, "form");
		element.get(0).submit();
	}

	public void checkboxShouldBeSelected(String locator) {
		info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			throw new AssertionError(String.format(
					"Checkbox '%s' should have been selected.", locator));
		}
	}

	public void checkboxShouldNotBeSelected(String locator) {
		info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			throw new AssertionError(String.format(
					"Checkbox '%s' should not have been selected.", locator));
		}
	}

	public void pageShouldContainCheckbox(String locator) {
		pageShouldContainCheckbox(locator, "");
	}

	public void pageShouldContainCheckbox(String locator, String message) {
		pageShouldContainCheckbox(locator, message, "INFO");
	}

	public void pageShouldContainCheckbox(String locator, String message,
			String loglevel) {
		pageShouldContainElement(locator, "checkbox", message, loglevel);
	}

	public void pageShouldNotContainCheckbox(String locator) {
		pageShouldNotContainCheckbox(locator, "");
	}

	public void pageShouldNotContainCheckbox(String locator, String message) {
		pageShouldNotContainCheckbox(locator, message, "INFO");
	}

	public void pageShouldNotContainCheckbox(String locator, String message,
			String loglevel) {
		pageShouldNotContainElement(locator, "checkbox", message, loglevel);
	}

	public void selectCheckbox(String locator) {
		info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			element.click();
		}
	}

	public void unselectCheckbox(String locator) {
		info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			element.click();
		}
	}

	public void pageShouldContainRadioButton(String locator) {
		pageShouldContainRadioButton(locator, "");
	}

	public void pageShouldContainRadioButton(String locator, String message) {
		pageShouldContainRadioButton(locator, message, "INFO");
	}

	public void pageShouldContainRadioButton(String locator, String message,
			String loglevel) {
		pageShouldContainElement(locator, "radio button", message, loglevel);
	}

	public void pageShouldNotContainRadioButton(String locator) {
		pageShouldNotContainRadioButton(locator, "");
	}

	public void pageShouldNotContainRadioButton(String locator, String message) {
		pageShouldNotContainRadioButton(locator, message, "INFO");
	}

	public void pageShouldNotContainRadioButton(String locator, String message,
			String loglevel) {
		pageShouldNotContainElement(locator, "radio button", message, loglevel);
	}

	public void radioButtonShouldBeSetTo(String groupName, String value) {
		info(String.format("Verifying radio button '%s' has selection '%s'.",
				groupName, value));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue == null || !actualValue.equals(value)) {
			throw new AssertionError(
					String.format(
							"Selection of radio button '%s' should have been '%s' but was '%s'",
							groupName, value, actualValue));
		}
	}

	public void radioButtonShouldNotBeSelected(String groupName) {
		info(String.format("Verifying radio button '%s' has no selection.",
				groupName));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue != null) {
			throw new AssertionError(
					String.format(
							"Radio button group '%s' should not have had selection, but '%s' was selected",
							groupName, actualValue));
		}
	}

	public void selectRadioButton(String groupName, String value) {
		info(String.format("Selecting '%s' from radio button '%s'.", value,
				groupName));
		WebElement element = getRadioButtonWithValue(groupName, value);
		if (!element.isSelected()) {
			element.click();
		}
	}

	public void chooseFile(String locator, String filePath) {
		if (!new File(filePath).isFile()) {
			info(String.format(
					"File '%s' does not exist on the local file system",
					filePath));
		}
		elementFind(locator, true, true).get(0).sendKeys(filePath);
	}

	public void inputPassword(String locator, String text) {
		info(String.format("Typing password into text field '%s'", locator));
		inputTextIntoTextField(locator, text);
	}

	public void inputText(String locator, String text) {
		info(String.format("Typing text '%s' into text field '%s'", text,
				locator));
		inputTextIntoTextField(locator, text);
	}

	public void pageShouldContainTextfield(String locator) {
		pageShouldContainTextfield(locator, "");
	}

	public void pageShouldContainTextfield(String locator, String message) {
		pageShouldContainTextfield(locator, message, "INFO");
	}

	public void pageShouldContainTextfield(String locator, String message,
			String loglevel) {
		pageShouldContainElement(locator, "text field", message, loglevel);
	}

	public void pageShouldNotContainTextfield(String locator) {
		pageShouldNotContainTextfield(locator, "");
	}

	public void pageShouldNotContainTextfield(String locator, String message) {
		pageShouldNotContainTextfield(locator, message, "INFO");
	}

	public void pageShouldNotContainTextfield(String locator, String message,
			String loglevel) {
		pageShouldNotContainElement(locator, "text field", message, loglevel);
	}

	public void textfieldShouldContain(String locator, String expected) {
		textfieldShouldContain(locator, expected, "");
	}

	public void textfieldShouldContain(String locator, String expected,
			String message) {
		String actual = getValue(locator, "text field");
		if (!actual.contains(expected)) {
			if (message == null) {
				message = String
						.format("Text field '%s' should have contained text '%s' but it contained '%s'",
								locator, expected, actual);
			}
			throw new AssertionError(message);
		}
		info(String.format("Text field '%s' contains text '%s'.", locator,
				expected));
	}

	public void textfieldShouldBe(String locator, String expected) {
		textfieldShouldBe(locator, expected, "");
	}

	public void textfieldShouldBe(String locator, String expected,
			String message) {
		String actual = getValue(locator, "text field");
		if (!actual.contains(expected)) {
			if (message == null) {
				message = String
						.format("Value of text field '%s' should have been '%s' but was '%s'",
								locator, expected, actual);
			}
			throw new AssertionError(message);
		}
		info(String.format("Content of text field '%s' is '%s'.", locator,
				expected));
	}

	public void clickButton(String locator) {
		info(String.format("Clicking button '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, false, "input");
		if (elements.size() == 0) {
			elements = elementFind(locator, true, true, "button");
		}
		elements.get(0).click();
	}

	public void pageShouldContainButton(String locator) {
		pageShouldContainButton(locator, "");
	}

	public void pageShouldContainButton(String locator, String message) {
		pageShouldContainButton(locator, message, "INFO");
	}

	public void pageShouldContainButton(String locator, String message,
			String loglevel) {
		try {
			pageShouldContainElement(locator, "input", message, loglevel);
		} catch (AssertionError ae) {
			pageShouldContainElement(locator, "button", message, loglevel);
		}
	}

	public void pageShouldNotContainButton(String locator) {
		pageShouldNotContainButton(locator, "");
	}

	public void pageShouldNotContainButton(String locator, String message) {
		pageShouldNotContainButton(locator, message, "INFO");
	}

	public void pageShouldNotContainButton(String locator, String message,
			String loglevel) {
		pageShouldNotContainElement(locator, "input", message, loglevel);
		pageShouldNotContainElement(locator, "button", message, loglevel);
	}

	// =================================================================
	// SECTION: FORMELEMENT - PROTECTED HELPERS
	// =================================================================

	protected WebElement getCheckbox(String locator) {
		return elementFind(locator, true, true, "input").get(0);
	}

	protected List<WebElement> getRadioButtons(String groupName) {
		String xpath = String.format(
				"xpath=//input[@type='radio' and @name='%s']", groupName);
		debug("Radio group locator: " + xpath);
		return elementFind(xpath, false, true);
	}

	protected WebElement getRadioButtonWithValue(String groupName, String value) {
		String xpath = String
				.format("xpath=//input[@type='radio' and @name='%s' and (@value='%s' or @id='%s')]",
						groupName, value, value);
		debug("Radio group locator: " + xpath);
		return elementFind(xpath, true, true).get(0);
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
		WebElement element = elementFind(locator, true, true).get(0);
		element.clear();
		element.sendKeys(text);
	}

	@Override
	protected boolean isFormElement(WebElement element) {
		if (element == null) {
			return false;
		}
		String tag = element.getTagName().toLowerCase();
		return tag == "input" || tag == "select" || tag == "textarea"
				|| tag == "button";
	}

}
