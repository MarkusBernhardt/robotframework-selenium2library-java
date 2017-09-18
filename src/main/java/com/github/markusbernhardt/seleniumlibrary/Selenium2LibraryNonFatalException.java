package com.github.markusbernhardt.seleniumlibrary;

/**
 * A raised exception of this type marks the step as failed, but does not end
 * all test executions.
 */
@SuppressWarnings("serial")
public class Selenium2LibraryNonFatalException extends RuntimeException {

	/**
	 * Mark this exception as non fatal
	 */
	public static final boolean ROBOT_EXIT_ON_FAILURE = false;

	public Selenium2LibraryNonFatalException() {
		super();
	}

	public Selenium2LibraryNonFatalException(String string) {
		super(string);
	}

	public Selenium2LibraryNonFatalException(Throwable t) {
		super(t);
	}

	public Selenium2LibraryNonFatalException(String string, Throwable t) {
		super(string, t);
	}
}
