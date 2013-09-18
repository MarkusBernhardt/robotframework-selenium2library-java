package com.github.markusbernhardt.selenium2library.javalibcore;

import java.util.List;

import org.robotframework.javalib.library.AnnotationLibrary;

public class InjectingAnnotationLibrary extends AnnotationLibrary {

	public InjectingAnnotationLibrary() {
		super();
	}

	public InjectingAnnotationLibrary(List<String> keywordPatterns) {
		super(keywordPatterns);
	}

	public InjectingAnnotationLibrary(String keywordPattern) {
		super(keywordPattern);
	}

}
