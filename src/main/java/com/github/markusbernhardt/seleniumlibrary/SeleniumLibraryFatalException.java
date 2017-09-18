package com.github.markusbernhardt.seleniumlibrary;

/**
 * A raised exception of this type ends all test executions.
 */
@SuppressWarnings("serial")
public class SeleniumLibraryFatalException extends RuntimeException {

	/**
	 * Mark this exception as fatal
	 */
	public static final boolean ROBOT_EXIT_ON_FAILURE = true;

	public SeleniumLibraryFatalException() {
		super();
	}

	public SeleniumLibraryFatalException(String string) {
		super(string);
	}

	public SeleniumLibraryFatalException(Throwable t) {
		super(t);
	}

	public SeleniumLibraryFatalException(String string, Throwable t) {
		super(string, t);
	}
}
