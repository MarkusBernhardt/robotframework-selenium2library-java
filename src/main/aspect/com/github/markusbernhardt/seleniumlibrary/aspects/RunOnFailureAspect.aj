package com.github.markusbernhardt.seleniumlibrary.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywords;

public aspect RunOnFailureAspect {

	private static ThreadLocal<Throwable> lastThrowable = new ThreadLocal<Throwable>();

	pointcut handleThrowable() : 
    execution(public * com.github.markusbernhardt.seleniumlibrary.keywords.*.*(..));

	after() throwing(Throwable t) : handleThrowable() {
		if (lastThrowable.get() == t) {
			// Already handled this Throwable
			return;
		}

		((RunOnFailureKeywords) thisJoinPoint.getTarget()).runOnFailureByAspectJ();
		lastThrowable.set(t);
	}
}
