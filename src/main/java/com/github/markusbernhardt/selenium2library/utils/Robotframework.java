package com.github.markusbernhardt.selenium2library.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

public abstract class Robotframework {

	public static String getLinkPath(File target, File base) {
		String path = getPathname(target, base);
		return encodeURLComponent(path);
	}

	public static String encodeURLComponent(final String s) {
		if (s == null) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < s.length(); i++) {
				final char c = s.charAt(i);
				if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9'))
						|| (c == '-') || (c == '.') || (c == '_') || (c == '~')) {
					sb.append(c);
				} else {
					final byte[] bytes = ("" + c).getBytes("UTF-8");
					for (byte b : bytes) {
						sb.append('%');

						int upper = (((int) b) >> 4) & 0xf;
						sb.append(Integer.toHexString(upper).toUpperCase(Locale.US));

						int lower = ((int) b) & 0xf;
						sb.append(Integer.toHexString(lower).toUpperCase(Locale.US));
					}
				}
			}

			return sb.toString();
		} catch (UnsupportedEncodingException uee) {
			throw new Selenium2LibraryNonFatalException(uee);
		}
	}

	public static String getPathname(File target, File base) {
		String targetName = target.getAbsolutePath();
		String baseName = base.getAbsolutePath();
		if (base.isFile()) {
			baseName = Python.osPathDirname(baseName);
		}
		if (baseName.equals(targetName)) {
			return Python.osPathBasename(targetName);
		}

		String[] splittedBaseName = Python.osPathSplitDrive(baseName);
		if (!Python.osPathSplitDrive(targetName)[0].equals(splittedBaseName[0])) {
			return targetName;
		}

		int commonLen = commonPath(baseName, targetName).length();
		if (splittedBaseName[1].equals(File.separator)) {
			return targetName.substring(commonLen);
		}

		if (commonLen == splittedBaseName[0].length() + File.separator.length()) {
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

	public static String commonPath(String p1, String p2) {
		while (p1.length() > 0 && p2.length() > 0) {
			if (p1.equals(p2)) {
				return p1;
			}
			if (p1.length() > p2.length()) {
				p1 = Python.osPathDirname(p1);
			} else {
				p2 = Python.osPathDirname(p2);
			}
		}
		return "";
	}

	public static String secsToTimestr(double double_secs) {
		TimestrHelper secsToTimestrHelper = new TimestrHelper(double_secs);
		return secsToTimestrHelper.getValue();
	}

	public static double timestrToSecs(String timestr) {
		timestr = normalizeTimestr(timestr);
		if (timestr.length() == 0) {
			throw new Selenium2LibraryNonFatalException("Invalid timestr: " + timestr);
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
			throw new Selenium2LibraryNonFatalException("Invalid timestr: " + timestr);
		}
		return sign * (millis / 1000 + secs + mins * 60 + hours * 60 * 60 + days * 60 * 60 * 24);
	}

	public static String normalizeTimestr(String timestr) {
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

}
