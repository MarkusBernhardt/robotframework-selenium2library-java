package com.github.markusbernhardt.seleniumlibrary;

/**
 * A raised exception of this type marks the step as failed, but does not end
 * all test executions.
 */
@SuppressWarnings("serial")
public class SeleniumLibraryNonFatalException extends RuntimeException {

	/**
	 * Mark this exception as non fatal
	 */
	public static final boolean ROBOT_EXIT_ON_FAILURE = false;

	public SeleniumLibraryNonFatalException() {
		super();
	}

	public SeleniumLibraryNonFatalException(String string) {
		super(string);
	}

	public SeleniumLibraryNonFatalException(Throwable t) {
		super(t);
	}

	public SeleniumLibraryNonFatalException(String string, Throwable t) {
		super(string, t);
	}
}
