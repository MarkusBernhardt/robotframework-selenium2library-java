package com.github.markusbernhardt.selenium2library.keywords;

import org.openqa.selenium.JavascriptExecutor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Robotframework;

@RobotKeywords
public abstract class Waiting extends TableElement {

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
	public void waitForCondition(final String condition, String timestr,
			String error) {
		if (error == null) {
			error = String.format(
					"Condition '%s' did not become true in <TIMEOUT>",
					condition);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return Boolean.TRUE.equals(((JavascriptExecutor) webDriverCache
						.getCurrent()).executeScript(condition));
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
	public void waitUntilPageContains(final String text, String timestr,
			String error) {
		if (error == null) {
			error = String
					.format("Text '%s' did not appear in <TIMEOUT>", text);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return isTextPresent(text);
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
	public void waitUntilPageContainsElement(final String locator,
			String timestr, String error) {
		if (error == null) {
			error = String.format("Element '%s' did not appear in <TIMEOUT>",
					locator);
		}
		waitUntil(timestr, error, new WaitUntilFunction() {

			@Override
			public boolean isFinished() {
				return isElementPresent(locator);
			}
		});
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected void waitUntil(String timestr, String error,
			WaitUntilFunction function) {
		double timeout = timestr != null ? Robotframework
				.timestrToSecs(timestr) : this.timeout;
		error = error.replace("<TIMEOUT>",
				Robotframework.secsToTimestr(timeout));
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
