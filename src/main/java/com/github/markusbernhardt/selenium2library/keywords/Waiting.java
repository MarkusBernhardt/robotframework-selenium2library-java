package com.github.markusbernhardt.selenium2library.keywords;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Robotframework;

public abstract class Waiting extends TableElement {

	// ##############################
	// Keywords
	// ##############################

	public void waitForCondition(String condition) {
		waitForCondition(condition, null);
	}

	public void waitForCondition(String condition, String timestr) {
		waitForCondition(condition, timestr, null);
	}

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

	public void waitUntilPageContains(String condition) {
		waitUntilPageContains(condition, null);
	}

	public void waitUntilPageContains(String condition, String timestr) {
		waitUntilPageContains(condition, timestr, null);
	}

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

	public void waitUntilPageContainsElement(String condition) {
		waitUntilPageContainsElement(condition, null);
	}

	public void waitUntilPageContainsElement(String condition, String timestr) {
		waitUntilPageContainsElement(condition, timestr, null);
	}

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
			} catch (StaleElementReferenceException e) {
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
