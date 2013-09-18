package com.github.markusbernhardt.selenium2library.javalibcore;

import org.robotframework.javalib.beans.annotation.KeywordBeanLoader;

public class InjectingKeywordBeanLoader extends KeywordBeanLoader {

	public InjectingKeywordBeanLoader(String keywordPattern,
			ClassLoader loader) {
		super(keywordPattern, loader);
	}

}
