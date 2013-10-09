package com.github.markusbernhardt.selenium2library;

public interface RunOnFailureKeywords {

	/**
	 * This method is called by the
	 * com.github.markusbernhardt.selenium2library.aspects.RunOnFailureAspect in
	 * case a exception is thrown in one of the public methods of a keyword
	 * class.
	 */
	void runOnFailureByAspectJ();

}
