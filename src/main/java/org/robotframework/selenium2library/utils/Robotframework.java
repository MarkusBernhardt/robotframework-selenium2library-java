package org.robotframework.selenium2library.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.robotframework.selenium2library.Selenium2Library;

public abstract class Robotframework extends Python {

	protected String getLinkPath(File target, File base) {
		String path = getPathname(target, base);
		path = new File(path).getAbsolutePath();
		String url;
		try {
			url = URLEncoder.encode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
		url = "file:" + url;
		// At least Jython seems to use 'C|/Path' and not 'C:/Path'
		if (File.separatorChar == '\\' && url.contains("|/")) {
			url = url.replaceFirst("|/", ":/");
		}
		return url.replace("%5C", "/").replace("%3A", ":").replace('|', ':');
	}

	protected String getPathname(File target, File base) {
		String targetName = target.getAbsolutePath();
		String baseName = base.getAbsolutePath();
		if (base.isFile()) {
			baseName = baseName.substring(0,
					baseName.lastIndexOf(File.separatorChar));
		}
		if (baseName.equals(targetName)) {
			return targetName.substring(targetName
					.lastIndexOf(File.separatorChar) + 1);
		}

		String baseDrive = baseName.substring(0, baseName.lastIndexOf(':') + 1);
		String basePath = baseName.substring(baseName.lastIndexOf(':') + 1);
		// if in Windows and base and link on different drives
		if (!targetName.substring(0, targetName.lastIndexOf(':') + 1).equals(
				baseDrive)) {
			return targetName;
		}

		int commonLen = commonPath(baseName, targetName).length();
		if (basePath.equals(File.separator)) {
			return targetName.substring(commonLen);
		}
		if (commonLen == baseDrive.length() + File.separator.length()) {
			commonLen -= File.separator.length();
		}

		baseName = baseName.substring(commonLen);
		StringBuilder builder = new StringBuilder();
		int index = -1;
		while ((index = baseName.indexOf(File.separatorChar, index + 1)) != -1) {
			builder.append("..");
			builder.append(File.separator);
		}
		builder.append(targetName.substring(commonLen + 1));
		return builder.toString();
	}

	protected String commonPath(String p1, String p2) {
		while (p1.length() > 0 && p2.length() > 0) {
			if (p1.equals(p2)) {
				return p1;
			}
			if (p1.length() > p2.length()) {
				p1 = p1.substring(0, p1.lastIndexOf(File.separatorChar));
			} else {
				p2 = p2.substring(0, p2.lastIndexOf(File.separatorChar));
			}
		}
		return "";
	}

	protected String secsToTimestr(double double_secs) {
		SecsToTimestrHelper secsToTimestrHelper = new SecsToTimestrHelper(
				double_secs);
		return secsToTimestrHelper.getValue();
	}

	protected double timestrToSecs(String timestr) {
		timestr = normalizeTimestr(timestr);
		if (timestr.length() == 0) {
			throw new RuntimeException("Invalid timestr: " + timestr);
		}

		try {
			return Double.parseDouble(timestr);
		} catch (NumberFormatException nfe) {
			// Do nothing. No number. Try something else
		}

		int millis = 0;
		int secs = 0;
		int mins = 0;
		int hours = 0;
		int days = 0;
		int sign = 0;
		if (timestr.charAt(0) == '-') {
			sign = -1;
			timestr = timestr.substring(1);
		} else {
			sign = 1;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (char c : timestr.toCharArray()) {
			switch (c) {
			case 'x':
				millis = Integer.parseInt(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				break;
			case 's':
				secs = Integer.parseInt(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				break;
			case 'm':
				mins = Integer.parseInt(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				break;
			case 'h':
				hours = Integer.parseInt(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				break;
			case 'p':
				days = Integer.parseInt(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				break;
			default:
				stringBuilder.append(c);
			}
		}
		if (stringBuilder.length() != 0) {
			throw new RuntimeException("Invalid timestr: " + timestr);
		}
		return sign
				* (millis / 1000 + secs + mins * 60 + hours * 60 * 60 + days * 60 * 60 * 24);
	}

	protected String normalizeTimestr(String timestr) {
		timestr = timestr.toLowerCase().replace(" ", "");
		timestr = timestr.replace("milliseconds", "ms");
		timestr = timestr.replace("millisecond", "ms");
		timestr = timestr.replace("millis", "ms");
		timestr = timestr.replace("seconds", "s");
		timestr = timestr.replace("second", "s");
		timestr = timestr.replace("secs", "s");
		timestr = timestr.replace("sec", "s");
		timestr = timestr.replace("minutes", "m");
		timestr = timestr.replace("minute", "m");
		timestr = timestr.replace("mins", "m");
		timestr = timestr.replace("min", "m");
		timestr = timestr.replace("hours", "h");
		timestr = timestr.replace("hour", "h");
		timestr = timestr.replace("days", "d");
		timestr = timestr.replace("day", "d");
		// 1) 'ms' -> 'x' to ease processing later
		// 2) 'd' -> 'p' because float('1d') returns 1.0 in Jython (bug
		// submitted)
		timestr = timestr.replace("ms", "x");
		timestr = timestr.replace("d", "p");
		return timestr;
	}

	private static class SecsToTimestrHelper {

		private boolean compact;
		private List<String> ret = new ArrayList<String>();
		private String sign;
		private int millis;
		private int secs;
		private int mins;
		private int hours;
		private int days;

		public SecsToTimestrHelper(double double_secs) {
			this(double_secs, false);
		}

		public SecsToTimestrHelper(double double_secs, boolean compact) {
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
				return sign + Selenium2Library.join(" ", ret);
			}
			return compact ? "0s" : "0 seconds";
		}

		private int doubleSecsToSecs(double double_secs) {
			return (int) double_secs;
		}

		private int doubleSecsToMillis(double double_secs) {
			int int_secs = doubleSecsToSecs(double_secs);
			return (int) Math.round((double_secs - int_secs) * 1000);
		}

		private void addItem(int value, String compactSuffix, String longSuffix) {
			if (value == 0) {
				return;
			}
			String suffix = compactSuffix;
			if (!compact) {
				suffix = String.format(" %s%s", longSuffix, pluralOrNot(value));
			}
			ret.add(String.format("%d%s", value, suffix));
		}

		private void secsToComponents(double double_secs) {
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

		private String pluralOrNot(int value) {
			return value == 1 ? "" : "s";
		}
	}

}
