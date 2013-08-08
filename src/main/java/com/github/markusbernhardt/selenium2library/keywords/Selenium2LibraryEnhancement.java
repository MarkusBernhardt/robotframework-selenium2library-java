package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.ElementFinder;

public class Selenium2LibraryEnhancement extends Waiting {

	// ##############################
	// Keywords
	// ##############################

	public String getSystemInfo() {
		return String
				.format("      os.name: '%s'\n      os.arch: '%s'\n   os.version: '%s'\n java.version: '%s'",
						System.getProperty("os.name"),
						System.getProperty("os.arch"),
						System.getProperty("os.version"),
						System.getProperty("java.version"));
	}

	public String logSystemInfo() {
		String actual = getSystemInfo();
		info(actual);
		return actual;
	}

	public String getRemoteCapabilities() {
		if (webDriverCache.getCurrent() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) webDriverCache.getCurrent())
					.getCapabilities().toString();
		} else {
			return "No remote session id";
		}
	}

	public String logRemoteCapabilities() {
		String actual = getRemoteCapabilities();
		info(actual);
		return actual;
	}

	public String getRemoteSessionId() {
		if (webDriverCache.getCurrent() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) webDriverCache.getCurrent())
					.getSessionId().toString();
		} else {
			return "No remote session id";
		}
	}

	public String logRemoteSessionId() {
		String actual = getRemoteSessionId();
		info(actual);
		return actual;
	}

	public void addLocationStrategy(String strategyName,
			String functionDefinition) {
		addLocationStrategy(strategyName, functionDefinition, null);
	}

	public void addLocationStrategy(String strategyName,
			String functionDefinition, String delimiter) {
		ElementFinder.addLocationStrategy(strategyName, functionDefinition,
				delimiter);
	}

	public void waitUntilPageNotContains(String condition) {
		waitUntilPageNotContains(condition, null);
	}

	public void waitUntilPageNotContains(String condition, String timestr) {
		waitUntilPageNotContains(condition, timestr, null);
	}

	public void waitUntilPageNotContains(final String text, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Text '%s' did not disappear in <TIMEOUT>",
					text);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !isTextPresent(text);
			}
		});
	}

	public void waitUntilPageNotContainsElement(String locator) {
		waitUntilPageNotContainsElement(locator, null);
	}

	public void waitUntilPageNotContainsElement(String locator, String timestr) {
		waitUntilPageNotContainsElement(locator, timestr, null);
	}

	public void waitUntilPageNotContainsElement(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format(
					"Element '%s' did not disappear in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !isElementPresent(locator);
			}
		});
	}

	public void waitUntilElementIsVisible(String locator) {
		waitUntilElementIsVisible(locator, null);
	}

	public void waitUntilElementIsVisible(String locator, String timestr) {
		waitUntilElementIsVisible(locator, timestr, null);
	}

	public void waitUntilElementIsVisible(final String locator, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Element '%s' not visible in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return isVisible(locator);
			}
		});
	}

	public void waitUntilElementIsNotVisible(String locator) {
		waitUntilElementIsNotVisible(locator, null);
	}

	public void waitUntilElementIsNotVisible(String locator, String timestr) {
		waitUntilElementIsNotVisible(locator, timestr, null);
	}

	public void waitUntilElementIsNotVisible(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still visible in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !isVisible(locator);
			}
		});
	}

	public void waitUntilTitleContains(String title) {
		waitUntilTitleContains(title, null);
	}

	public void waitUntilTitleContains(String title, String timestr) {
		waitUntilTitleContains(title, timestr, null);
	}

	public void waitUntilTitleContains(final String title, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>",
					title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = getTitle();
				return currentTitle != null && currentTitle.contains(title);
			}
		});
	}

	public void waitUntilTitleNotContains(String title) {
		waitUntilTitleNotContains(title, null);
	}

	public void waitUntilTitleNotContains(String title, String timestr) {
		waitUntilTitleNotContains(title, timestr, null);
	}

	public void waitUntilTitleNotContains(final String title, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>",
					title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = getTitle();
				return currentTitle == null || !currentTitle.contains(title);
			}
		});
	}

	public void waitUntilTitleIs(String title) {
		waitUntilTitleIs(title, null);
	}

	public void waitUntilTitleIs(String title, String timestr) {
		waitUntilTitleIs(title, timestr, null);
	}

	public void waitUntilTitleIs(final String title, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>",
					title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = getTitle();
				return currentTitle != null && currentTitle.equals(title);
			}
		});
	}

	public void waitUntilTitleIsNot(String title) {
		waitUntilTitleIsNot(title, null);
	}

	public void waitUntilTitleIsNot(String title, String timestr) {
		waitUntilTitleIsNot(title, timestr, null);
	}

	public void waitUntilTitleIsNot(final String title, String timestr,
			String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>",
					title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = getTitle();
				return currentTitle == null || !currentTitle.equals(title);
			}
		});
	}

	public void elementShouldBeSelected(String locator) {
		this.elementShouldBeSelected(locator, "");
	}

	public void elementShouldBeSelected(String locator, String message) {
		info(String.format("Verifying element '%s' is selected.", locator));
		boolean selected = isSelected(locator);

		if (!selected) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should be selected, but it is not.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	public void elementShouldNotBeSelected(String locator) {
		this.elementShouldNotBeSelected(locator, "");
	}

	public void elementShouldNotBeSelected(String locator, String message) {
		info(String.format("Verifying element '%s' is not selected.", locator));
		boolean selected = isSelected(locator);

		if (selected) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should not be selected, but it is.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	public void waitUntilElementIsSelected(String locator) {
		waitUntilElementIsSelected(locator, null);
	}

	public void waitUntilElementIsSelected(String locator, String timestr) {
		waitUntilElementIsSelected(locator, timestr, null);
	}

	public void waitUntilElementIsSelected(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not selected in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return isSelected(locator);
			}
		});
	}

	public void waitUntilElementIsNotSelected(String locator) {
		waitUntilElementIsNotSelected(locator, null);
	}

	public void waitUntilElementIsNotSelected(String locator, String timestr) {
		waitUntilElementIsNotSelected(locator, timestr, null);
	}

	public void waitUntilElementIsNotSelected(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still selected in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !isSelected(locator);
			}
		});
	}

	public void elementShouldBeClickable(String locator) {
		this.elementShouldBeClickable(locator, "");
	}

	public void elementShouldBeClickable(String locator, String message) {
		info(String.format("Verifying element '%s' is clickable.", locator));
		boolean clickable = isClickable(locator);

		if (!clickable) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should be clickable, but it is not.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	public void elementShouldNotBeClickable(String locator) {
		this.elementShouldNotBeClickable(locator, "");
	}

	public void elementShouldNotBeClickable(String locator, String message) {
		info(String.format("Verifying element '%s' is not clickable.", locator));
		boolean clickable = isClickable(locator);

		if (clickable) {
			if (message == null || message.equals("")) {
				message = String.format(
						"Element '%s' should not be clickable, but it is.",
						locator);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	public void waitUntilElementIsClickable(String locator) {
		waitUntilElementIsClickable(locator, null);
	}

	public void waitUntilElementIsClickable(String locator, String timestr) {
		waitUntilElementIsClickable(locator, timestr, null);
	}

	public void waitUntilElementIsClickable(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not clickable in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return isClickable(locator);
			}
		});
	}

	public void waitUntilElementIsSuccessfullyClicked(String locator) {
		waitUntilElementIsSuccessfullyClicked(locator, null);
	}

	public void waitUntilElementIsSuccessfullyClicked(String locator,
			String timestr) {
		waitUntilElementIsSuccessfullyClicked(locator, timestr, null);
	}

	public void waitUntilElementIsSuccessfullyClicked(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format(
					"Element '%s' not successfully clicked in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				clickElement(locator);
				return true;
			}
		});
	}

	public void waitUntilElementIsNotClickable(String locator) {
		waitUntilElementIsNotClickable(locator, null);
	}

	public void waitUntilElementIsNotClickable(String locator, String timestr) {
		waitUntilElementIsNotClickable(locator, timestr, null);
	}

	public void waitUntilElementIsNotClickable(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still clickable in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !isClickable(locator);
			}
		});
	}

	public void elementShouldNotContain(String locator, String expected) {
		this.elementShouldNotContain(locator, expected, "");
	}

	public void elementShouldNotContain(String locator, String expected,
			String message) {
		String actual = fetchText(locator);

		if (actual.toLowerCase().contains(expected.toLowerCase())) {
			info(String.format("Element Should Not Contain: %s => FAILED",
					expected));
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Element should not have contained text '%s' but its text was %s.",
							expected, actual));
		} else {
			info(String
					.format("Element Should Not Contain: %s => OK", expected));
		}
	}

	public void elementTextShouldNotBe(String locator, String expected) {
		this.elementTextShouldNotBe(locator, expected, "");
	}

	public void elementTextShouldNotBe(String locator, String expected,
			String message) {
		info(String.format(
				"Verifying element '%s' contains exactly text '%s'.", locator,
				expected));

		List<WebElement> elements = elementFind(locator, true, true);
		String actual = elements.get(0).getText();

		if (expected.equals(actual)) {
			if (message == null || message.equals("")) {
				message = String
						.format("The text of element '%s' should have been '%s', but it was '%s'.",
								locator, expected, actual);
			}
			throw new Selenium2LibraryNonFatalException(message);
		}
	}
	
	public void setLogDirectory(String logDirectory) throws Exception {
		File file = new File(logDirectory);

		if(file.exists() && file.isDirectory() && file.canWrite()) {
			Logging.setLogDir(file.getAbsolutePath());
		} else {
			throw new Exception("Location given as parameter: " + logDirectory + " must exist and must be a writeable directory!");
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected boolean isClickable(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);
		if (elements.size() == 0) {
			return false;
		}
		WebElement element = elements.get(0);
		return element.isDisplayed() && element.isEnabled();
	}

	protected boolean isSelected(String locator) {
		List<WebElement> elements = elementFind(locator, true, false);
		if (elements.size() == 0) {
			return false;
		}
		WebElement element = elements.get(0);
		return element.isSelected();
	}

}
