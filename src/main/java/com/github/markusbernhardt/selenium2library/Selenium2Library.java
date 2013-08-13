package com.github.markusbernhardt.selenium2library;

import java.io.File;
import java.util.ResourceBundle;

import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.keywords.Selenium2LibraryEnhancement;

/**
 * Robotframework Library. All public methods are keywords.
 */
@RobotKeywords
public class Selenium2Library extends Selenium2LibraryEnhancement {

	/**
	 * The actual version of this library. Loaded from maven project.
	 */
	public static String ROBOT_LIBRARY_VERSION;

	static {
		/**
		 * Load the version from file
		 */
		try {
			ROBOT_LIBRARY_VERSION = ResourceBundle.getBundle(
					Selenium2Library.class.getCanonicalName().replace(".",
							File.separator)).getString("version");
		} catch (RuntimeException e) {
			ROBOT_LIBRARY_VERSION = "unknown";
		}
	}

	/**
	 * Default constructor
	 */
	public Selenium2Library() {
		this(5.0);
	}

	/**
	 * Constructor
	 * 
	 * @param timeout
	 *            Default timeout in seconds for all wait methods
	 */
	public Selenium2Library(double timeout) {
		this(timeout, 0.0);
	}

	/**
	 * Constructor
	 * 
	 * @param timeout
	 *            Default timeout in seconds for all wait methods
	 * @param implicitWait
	 *            Selenium implicit wait time in seconds
	 */
	public Selenium2Library(double timeout, double implicitWait) {
		this(timeout, implicitWait, "Capture Page Screenshot");
	}

	/**
	 * Constructor
	 * 
	 * @param timeout
	 *            Default timeout in seconds for all wait methods
	 * @param implicitWait
	 *            Selenium implicit wait time in seconds
	 * @param runOnFailureKeyword
	 *            Keyword to run opn failure
	 */
	public Selenium2Library(double timeout, double implicitWait,
			String runOnFailureKeyword) {
		this.timeout = timeout;
		this.implicitWait = implicitWait;
		this.runOnFailureKeyword = runOnFailureKeyword;
	}

	/**
	 * This method is called by the
	 * com.github.markusbernhardt.selenium2library.aspects.RunOnFailureAspect in
	 * case a exception is thrown in one of the public methods of a keyword
	 * class.
	 */
	public void runOnFailureByAspectJ() {
		runOnFailure();
	}

}
