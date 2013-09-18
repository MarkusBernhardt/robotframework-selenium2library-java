package com.github.markusbernhardt.selenium2library;

import com.github.markusbernhardt.selenium2library.keywords.Selenium2LibraryEnhancement;

/**
 * Robotframework Library. All public methods are keywords.
 */
public class Selenium2Library extends Selenium2LibraryEnhancement {

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
