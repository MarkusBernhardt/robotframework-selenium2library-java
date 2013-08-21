package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.ElementFinder;

@RobotKeywords
public class Selenium2LibraryEnhancement extends Waiting {
	
	public Selenium2LibraryEnhancement() {}

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

	@RobotKeyword
	public String getRemoteCapabilities() {
		if (webDriverCache.getCurrent() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) webDriverCache.getCurrent())
					.getCapabilities().toString();
		} else {
			return "No remote session id";
		}
	}

	@RobotKeyword
	public String logRemoteCapabilities() {
		String actual = getRemoteCapabilities();
		info(actual);
		return actual;
	}

	@RobotKeyword
	public String getRemoteSessionId() {
		if (webDriverCache.getCurrent() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) webDriverCache.getCurrent())
					.getSessionId().toString();
		} else {
			return "No remote session id";
		}
	}

	@RobotKeyword
	public String logRemoteSessionId() {
		String actual = getRemoteSessionId();
		info(actual);
		return actual;
	}

	@RobotKeywordOverload
	public void addLocationStrategy(String strategyName,
			String functionDefinition) {
		addLocationStrategy(strategyName, functionDefinition, null);
	}

	@RobotKeyword
	@ArgumentNames({"strategyName", "functionDefinition", "delimiter=NONE"})
	public void addLocationStrategy(String strategyName,
			String functionDefinition, String delimiter) {
		ElementFinder.addLocationStrategy(strategyName, functionDefinition,
				delimiter);
	}

	@RobotKeyword
	@ArgumentNames({"text", "timestr=", "error=NONE"})
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
	
	@RobotKeywordOverload
	public void waitUntilPageNotContains(String condition, String timestr) {
		waitUntilPageNotContains(condition, timestr, null);
	}
	
	@RobotKeywordOverload
	public void waitUntilPageNotContains(String condition) {
		waitUntilPageNotContains(condition, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilPageNotContainsElement(String locator, String timestr) {
		waitUntilPageNotContainsElement(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilPageNotContainsElement(String locator) {
		waitUntilPageNotContainsElement(locator, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsVisible(String locator, String timestr) {
		waitUntilElementIsVisible(locator, timestr, null);
	}
	
	@RobotKeywordOverload
	public void waitUntilElementIsVisible(String locator) {
		waitUntilElementIsVisible(locator, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsNotVisible(String locator, String timestr) {
		waitUntilElementIsNotVisible(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsNotVisible(String locator) {
		waitUntilElementIsNotVisible(locator, null);
	}
	
	@RobotKeyword
	@ArgumentNames({"title", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilTitleContains(String title, String timestr) {
		waitUntilTitleContains(title, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilTitleContains(String title) {
		waitUntilTitleContains(title, null, null);
	}

	@RobotKeyword
	@ArgumentNames({"title", "timestr=", "error=NONE"})
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
	
	@RobotKeywordOverload
	public void waitUntilTitleNotContains(String title, String timestr) {
		waitUntilTitleNotContains(title, timestr, null);
	}	

	@RobotKeywordOverload
	public void waitUntilTitleNotContains(String title) {
		waitUntilTitleNotContains(title, null, null);
	}

	@RobotKeyword
	@ArgumentNames({"title", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilTitleIs(String title, String timestr) {
		waitUntilTitleIs(title, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilTitleIs(String title) {
		waitUntilTitleIs(title, null);
	}
	
	@RobotKeyword
	@ArgumentNames({"title", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilTitleIsNot(String title, String timestr) {
		waitUntilTitleIsNot(title, timestr, null);
	}
	
	@RobotKeywordOverload
	public void waitUntilTitleIsNot(String title) {
		waitUntilTitleIsNot(title, null, null);
	}

	@RobotKeywordOverload
	public void elementShouldBeSelected(String locator) {
		this.elementShouldBeSelected(locator, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "message=NONE"})
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

	@RobotKeywordOverload
	public void elementShouldNotBeSelected(String locator) {
		this.elementShouldNotBeSelected(locator, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "message=NONE"})
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

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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
	
	@RobotKeywordOverload
	public void waitUntilElementIsSelected(String locator, String timestr) {
		waitUntilElementIsSelected(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsSelected(String locator) {
		waitUntilElementIsSelected(locator, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsNotSelected(String locator, String timestr) {
		waitUntilElementIsNotSelected(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsNotSelected(String locator) {
		waitUntilElementIsNotSelected(locator, null);
	}

	@RobotKeywordOverload
	public void elementShouldBeClickable(String locator) {
		this.elementShouldBeClickable(locator, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "message=NONE"})
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

	@RobotKeywordOverload
	public void elementShouldNotBeClickable(String locator) {
		this.elementShouldNotBeClickable(locator, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "message=NONE"})
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

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsClickable(String locator, String timestr) {
		waitUntilElementIsClickable(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsClickable(String locator) {
		waitUntilElementIsClickable(locator, null, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsSuccessfullyClicked(String locator,
			String timestr) {
		waitUntilElementIsSuccessfullyClicked(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsSuccessfullyClicked(String locator) {
		waitUntilElementIsSuccessfullyClicked(locator, null);
	}

	@RobotKeyword
	@ArgumentNames({"locator", "timestr=", "error=NONE"})
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

	@RobotKeywordOverload
	public void waitUntilElementIsNotClickable(String locator, String timestr) {
		waitUntilElementIsNotClickable(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsNotClickable(String locator) {
		waitUntilElementIsNotClickable(locator, null);
	}

	@RobotKeywordOverload
	public void elementShouldNotContain(String locator, String expected) {
		this.elementShouldNotContain(locator, expected, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "expected", "message=NONE"})
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

	@RobotKeywordOverload
	public void elementTextShouldNotBe(String locator, String expected) {
		this.elementTextShouldNotBe(locator, expected, "");
	}

	@RobotKeyword
	@ArgumentNames({"locator", "expected", "message=NONE"})
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
	
	@RobotKeyword
	@ArgumentNames({"logDirectory"})
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
