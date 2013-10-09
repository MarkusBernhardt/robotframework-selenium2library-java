package com.github.markusbernhardt.selenium2library.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywords;

public aspect RunOnFailureAspect {

	private static ThreadLocal<Throwable> lastThrowable = new ThreadLocal<Throwable>();

	pointcut handleThrowable() : 
    execution(public * com.github.markusbernhardt.selenium2library.keywords.*.*(..));

	after() throwing(Throwable t) : handleThrowable() {
		if (lastThrowable.get() == t) {
			// Already handled this Throwable
			return;
		}

		((RunOnFailureKeywords) thisJoinPoint.getTarget()).runOnFailureByAspectJ();
		lastThrowable.set(t);
	}
}
