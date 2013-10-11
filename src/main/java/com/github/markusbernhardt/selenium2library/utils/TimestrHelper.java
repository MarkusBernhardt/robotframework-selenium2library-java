package com.github.markusbernhardt.selenium2library.utils;

import java.util.ArrayList;
import java.util.List;

public class TimestrHelper {

	protected boolean compact;
	protected List<String> ret = new ArrayList<String>();
	protected String sign;
	protected int millis;
	protected int secs;
	protected int mins;
	protected int hours;
	protected int days;

	public TimestrHelper(double double_secs) {
		this(double_secs, false);
	}

	public TimestrHelper(double double_secs, boolean compact) {
		this.compact = compact;
		secsToComponents(double_secs);
		addItem(days, "d", "day");
		addItem(hours, "h", "hour");
		addItem(mins, "min", "minute");
		addItem(secs, "s", "second");
		addItem(millis, "ms", "millisecond");
	}

	public String getValue() {
		if (ret.size() > 0) {
			return sign + Python.join(" ", ret);
		}
		return compact ? "0s" : "0 seconds";
	}

	protected int doubleSecsToSecs(double double_secs) {
		return (int) double_secs;
	}

	protected int doubleSecsToMillis(double double_secs) {
		int int_secs = doubleSecsToSecs(double_secs);
		return (int) Math.round((double_secs - int_secs) * 1000);
	}

	protected void addItem(int value, String compactSuffix, String longSuffix) {
		if (value == 0) {
			return;
		}
		String suffix = compactSuffix;
		if (!compact) {
			suffix = String.format(" %s%s", longSuffix, pluralOrNot(value));
		}
		ret.add(String.format("%d%s", value, suffix));
	}

	protected void secsToComponents(double double_secs) {
		if (double_secs < 0) {
			sign = "- ";
			double_secs = Math.abs(double_secs);
		} else {
			sign = "";
		}
		int int_secs = doubleSecsToSecs(double_secs);
		millis = doubleSecsToMillis(double_secs);
		secs = int_secs % 60;
		mins = int_secs / 60 % 60;
		hours = int_secs / (60 * 60) % 24;
		days = int_secs / (60 * 60 * 24);
	}

	protected String pluralOrNot(int value) {
		return value == 1 ? "" : "s";
	}

}
