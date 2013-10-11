package com.github.markusbernhardt.selenium2library.keywords;

import org.openqa.selenium.JavascriptExecutor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Robotframework;

@RobotKeywords
public class Waiting extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	// ##############################
	// Keywords
	// ##############################
	@RobotKeywordOverload
	public void waitForCondition(String condition) {
		waitForCondition(condition, null);
	}

	@RobotKeywordOverload
	public void waitForCondition(String condition, String timestr) {
		waitForCondition(condition, timestr, null);
	}

	@RobotKeyword("Waits until the given _condition_ is true or _timeout_ expires.\n\n"

	+ "_code_ may contain multiple lines of code but must contain a return statement (with "
			+ "the value to be returned) at the end.\n\n"

			+ "The _condition_ can be arbitrary JavaScript expression but must contain a return "
			+ "statement (with the value to be returned) at the end. See `Execute JavaScript` for "
			+ "information about accessing the actual contents of the window through JavaScript.\n\n"

			+ "_error_ can be used to override the default error message.\n\n"

			+ "See `Introduction` for more information about _timeout_ and its default value.\n\n"

			+ "See also `Wait Until Page Contains`, `Wait Until Page Contains Element` and BuiltIn "
			+ "keyword _Wait Until Keyword Succeeds_.\n")
	@ArgumentNames({ "condition", "timestr=NONE", "error=NONE" })
	public void waitForCondition(final String condition, String timestr, String error) {
		if (error == null) {
			error = String.format("Condition '%s' did not become true in <TIMEOUT>", condition);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return Boolean.TRUE.equals(((JavascriptExecutor) browserManagement.getCurrentWebDriver())
						.executeScript(condition));
			}
		});
	}

	@RobotKeywordOverload
	public void waitUntilPageContains(String condition) {
		waitUntilPageContains(condition, null);
	}

	@RobotKeywordOverload
	public void waitUntilPageContains(String condition, String timestr) {
		waitUntilPageContains(condition, timestr, null);
	}

	@RobotKeyword("Waits until _text_ appears on current page.\n\n"

	+ "Fails if _timeout_ expires before the _text_ appears. See `Introduction` for more information "
			+ "about _timeout_ and its default value.\n\n"

			+ "_error_ can be used to override the default error message.\n\n"

			+ "See also `Wait Until Page Contains`, `Wait Until Page Contains Element` and BuiltIn "
			+ "keyword _Wait Until Keyword Succeeds_.\n")
	@ArgumentNames({ "condition", "timestr=NONE", "error=NONE" })
	public void waitUntilPageContains(final String text, String timestr, String error) {
		if (error == null) {
			error = String.format("Text '%s' did not appear in <TIMEOUT>", text);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return element.isTextPresent(text);
			}
		});
	}

	@RobotKeywordOverload
	public void waitUntilPageContainsElement(String condition) {
		waitUntilPageContainsElement(condition, null);
	}

	@RobotKeywordOverload
	public void waitUntilPageContainsElement(String condition, String timestr) {
		waitUntilPageContainsElement(condition, timestr, null);
	}

	@RobotKeyword("Waits until element specified with _locator_ appears on current page.\n\n"
			+ "Fails if _timeout_ expires before the _text_ appears. See `Introduction` for more information "
			+ "about _timeout_ and its default value.\n\n"

			+ "_error_ can be used to override the default error message.\n\n"

			+ "See also `Wait Until Page Contains`, `Wait Until Page Contains Element` and BuiltIn "
			+ "keyword _Wait Until Keyword Succeeds_.\n")
	@ArgumentNames({ "condition", "timestr=NONE", "error=NONE" })
	public void waitUntilPageContainsElement(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' did not appear in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return element.isElementPresent(locator);
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
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsVisible(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not visible in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return element.isVisible(locator);
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
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsNotVisible(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still visible in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !element.isVisible(locator);
			}
		});
	}
	
	@RobotKeywordOverload
	public void waitUntilElementIsClickable(String locator) {
		waitUntilElementIsClickable(locator, null, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsClickable(String locator, String timestr) {
		waitUntilElementIsClickable(locator, timestr, null);
	}

	@RobotKeyword
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsClickable(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not clickable in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return element.isClickable(locator);
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

	@RobotKeyword
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsNotClickable(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still clickable in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !element.isClickable(locator);
			}
		});
	}

	@RobotKeywordOverload
	public void waitUntilElementIsSuccessfullyClicked(String locator, String timestr) {
		waitUntilElementIsSuccessfullyClicked(locator, timestr, null);
	}

	@RobotKeywordOverload
	public void waitUntilElementIsSuccessfullyClicked(String locator) {
		waitUntilElementIsSuccessfullyClicked(locator, null);
	}

	@RobotKeyword
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsSuccessfullyClicked(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not successfully clicked in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				element.clickElement(locator);
				return true;
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
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsSelected(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' not selected in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return element.isSelected(locator);
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

	@RobotKeyword
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilElementIsNotSelected(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' still selected in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !element.isSelected(locator);
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
	@ArgumentNames({ "text", "timestr=", "error=NONE" })
	public void waitUntilPageNotContains(final String text, String timestr, String error) {
		if (error == null) {
			error = String.format("Text '%s' did not disappear in <TIMEOUT>", text);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !element.isTextPresent(text);
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
	@ArgumentNames({ "locator", "timestr=", "error=NONE" })
	public void waitUntilPageNotContainsElement(final String locator, String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' did not disappear in <TIMEOUT>", locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return !element.isElementPresent(locator);
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
	@ArgumentNames({ "title", "timestr=", "error=NONE" })
	public void waitUntilTitleContains(final String title, String timestr, String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = browserManagement.getTitle();
				return currentTitle != null && currentTitle.contains(title);
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
	@ArgumentNames({ "title", "timestr=", "error=NONE" })
	public void waitUntilTitleNotContains(final String title, String timestr, String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = browserManagement.getTitle();
				return currentTitle == null || !currentTitle.contains(title);
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
	@ArgumentNames({ "title", "timestr=", "error=NONE" })
	public void waitUntilTitleIs(final String title, String timestr, String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = browserManagement.getTitle();
				return currentTitle != null && currentTitle.equals(title);
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

	@RobotKeyword
	@ArgumentNames({ "title", "timestr=", "error=NONE" })
	public void waitUntilTitleIsNot(final String title, String timestr, String error) {
		if (error == null) {
			error = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				String currentTitle = browserManagement.getTitle();
				return currentTitle == null || !currentTitle.equals(title);
			}
		});
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected void waitUntil(String timestr, String error, WaitUntilFunction function) {
		double timeout = timestr != null ? Robotframework.timestrToSecs(timestr) : browserManagement.getTimeout();
		error = error.replace("<TIMEOUT>", Robotframework.secsToTimestr(timeout));
		long maxtime = System.currentTimeMillis() + (long) (timeout * 1000);
		for (;;) {
			try {
				if (function.isFinished()) {
					break;
				}
			} catch (Throwable t) {
			}
			if (System.currentTimeMillis() > maxtime) {
				throw new Selenium2LibraryNonFatalException(error);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	protected static interface WaitUntilFunction {

		boolean isFinished();
	}

}
