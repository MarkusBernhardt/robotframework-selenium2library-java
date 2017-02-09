package com.github.markusbernhardt.selenium2library.utils;

/**
 * Utilities to convert Robot Framework time.
 */
public final class TimeUtils {
	private static final int SECONDS_TO_MILLISECS = 1000;
	private static final int MINUTES_TO_MILLISECS = 60 * SECONDS_TO_MILLISECS;
	private static final int HOURS_TO_MILLISECS = 60 * MINUTES_TO_MILLISECS;
	private static final int DAYS_TO_MILLISECS = 24 * HOURS_TO_MILLISECS;

	private static final String DAYS_PATTERN =  "(\\s*\\d+(\\.\\d+)?\\s*d(ays?)?)?";
	private static final String HOURS_PATTERN =  "(\\s*\\d+(\\.\\d+)?\\s*h(ours?)?)?";
	private static final String MINUTES_PATTERN =  "(\\s*\\d+(\\.\\d+)?\\s*m(in((ute)?s?)?)?)?";
	private static final String SECONDS_PATTERN =  "(\\s*\\d+(\\.\\d+)?\\s*s(ec((ond)?s?))?)?";
	private static final String MILLISECONDS_PATTERN =  "(\\s*\\d+(\\.\\d+)?\\s*(millis(ec((ond)?s?))?|ms))?";
	private static final String TIME_STRING_PATTERN = "-?(\\s*\\d+(\\.\\d+)?\\s*|" + DAYS_PATTERN + HOURS_PATTERN +
			MINUTES_PATTERN + SECONDS_PATTERN + MILLISECONDS_PATTERN + ")";

	private TimeUtils() {
		// this is a utility class
	}

	/**
	 * Converts a Robot Framework time string to milliseconds value.
	 * See http://robotframework.org/robotframework/latest/libraries/DateTime.html
	 *
	 * @param robotTimeString a valid time string
	 * @return the time in milliseconds
	 */
	public static int convertRobotTimeToMillis(String robotTimeString) {
		int sum = 0;
		if (!robotTimeString.matches(TIME_STRING_PATTERN)) {
			throw new IllegalArgumentException("Invalid time string " + robotTimeString);
		}
		String[] values = robotTimeString.replaceAll("(\\d)([dhms])", "$1 $2").split("\\s+");
		try {
			if (values.length == 1) {
				return Math.round(Float.parseFloat(values[0]) * SECONDS_TO_MILLISECS);
			}
			final int signum = values[0].startsWith("-") ? -1 : 1;
			if (values.length % 2 != 0) {
				throw new IllegalArgumentException("Invalid time string " + robotTimeString);
			}
			for (int i = 0; i < values.length - 1; i+=2) {
				final float value = Math.abs(Float.parseFloat(values[i]));
				final int multiplier = getMultiplier(values[i + 1]);
				sum += signum * Math.round(value * multiplier);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid time string " + robotTimeString, e);
		}
		return sum;
	}

	private static int getMultiplier(String specifier) {
		if ("days".equalsIgnoreCase(specifier)
			|| "day".equalsIgnoreCase(specifier)
			|| "d".equalsIgnoreCase(specifier)) {
			return DAYS_TO_MILLISECS;
		}
		if ("hours".equalsIgnoreCase(specifier)
			|| "hour".equalsIgnoreCase(specifier)
			|| "h".equalsIgnoreCase(specifier)) {
			return HOURS_TO_MILLISECS;
		}
		if ("minutes".equalsIgnoreCase(specifier)
				|| "minute".equalsIgnoreCase(specifier)
				|| "mins".equalsIgnoreCase(specifier)
				|| "min".equalsIgnoreCase(specifier)
				|| "m".equalsIgnoreCase(specifier)) {
			return MINUTES_TO_MILLISECS;
		}
		if ("seconds".equalsIgnoreCase(specifier)
				|| "second".equalsIgnoreCase(specifier)
				|| "secs".equalsIgnoreCase(specifier)
				|| "sec".equalsIgnoreCase(specifier)
				|| "s".equalsIgnoreCase(specifier)) {
			return SECONDS_TO_MILLISECS;
		}
		if ("milliseconds".equalsIgnoreCase(specifier)
				|| "millisecond".equalsIgnoreCase(specifier)
				|| "millis".equalsIgnoreCase(specifier)
				|| "ms".equalsIgnoreCase(specifier)) {
			return 1;
		}
		throw new IllegalArgumentException("Invalid time specifier " + specifier);
	}
}
