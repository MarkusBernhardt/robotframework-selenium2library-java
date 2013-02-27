package org.robotframework.selenium2library.keywords;

import org.openqa.selenium.JavascriptExecutor;

public abstract class Waiting extends TableElement {

	// =================================================================
	// SECTION: WAITING - KEYWORDS
	// =================================================================

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

	// =================================================================
	// SECTION: WAITING - PROTECTED HELPERS
	// =================================================================

	protected void waitUntil(String timestr, String error,
			WaitUntilFunction function) {
		double timeout = timestr != null ? timestrToSecs(timestr)
				: this.timeout;
		error = error.replace("<TIMEOUT>", secsToTimestr(timeout));
		long maxtime = System.currentTimeMillis() + (long) (timeout * 1000);
		while (!function.isFinished()) {
			if (System.currentTimeMillis() > maxtime) {
				throw new AssertionError(error);
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
