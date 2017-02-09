package com.github.markusbernhardt.selenium2library.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUtilsTest {

	@Test
	public void whenNoSpecifier_ShouldConvertSeconds() {
		assertEquals(1000, TimeUtils.convertRobotTimeToMillis("1"));
		assertEquals(-2000, TimeUtils.convertRobotTimeToMillis("-2"));
		assertEquals(500, TimeUtils.convertRobotTimeToMillis("0.5"));
	}

	@Test
	public void whenMillisecondsSpecified_ShouldConvertMillisecsonds() {
		assertEquals(-10, TimeUtils.convertRobotTimeToMillis("-10ms"));
		assertEquals(1, TimeUtils.convertRobotTimeToMillis("0.8 milliseconds"));
		assertEquals(1, TimeUtils.convertRobotTimeToMillis("1 millisecond"));
		assertEquals(1, TimeUtils.convertRobotTimeToMillis("1 millis"));
	}

	@Test
	public void whenSecondsSpecified_ShouldConvertSeconds() {
		assertEquals(-10000, TimeUtils.convertRobotTimeToMillis("-10seconds"));
		assertEquals(800, TimeUtils.convertRobotTimeToMillis("0.8 s"));
		assertEquals(1000, TimeUtils.convertRobotTimeToMillis("1 sec"));
		assertEquals(2000, TimeUtils.convertRobotTimeToMillis("2second"));
	}

	@Test
	public void whenMinutesSpecified_ShouldConvertMinutes() {
		assertEquals(-600000, TimeUtils.convertRobotTimeToMillis("-10min"));
		assertEquals(48000, TimeUtils.convertRobotTimeToMillis("0.8 m"));
		assertEquals(60000, TimeUtils.convertRobotTimeToMillis("1 minute"));
		assertEquals(120000, TimeUtils.convertRobotTimeToMillis("2minutes"));
	}

	@Test
	public void whenHoursSpecified_ShouldConvertHours() {
		assertEquals(-36000000, TimeUtils.convertRobotTimeToMillis("-10h"));
		assertEquals(3600000, TimeUtils.convertRobotTimeToMillis("1 hour"));
		assertEquals(5400000, TimeUtils.convertRobotTimeToMillis("1.5 hours"));
	}

	@Test
	public void whenDaysSpecified_ShouldConvertDays() {
		assertEquals(-864000000, TimeUtils.convertRobotTimeToMillis("-10days"));
		assertEquals(129600000, TimeUtils.convertRobotTimeToMillis("1.5d"));
		assertEquals(86400000, TimeUtils.convertRobotTimeToMillis("1 day"));
	}

	@Test
	public void whenComplexSpecified_ShouldConvertComplex() {
		assertEquals(151264015, TimeUtils.convertRobotTimeToMillis("1.5d 6 hours 1 minute 4 secs 15ms"));
		assertEquals(-151264015, TimeUtils.convertRobotTimeToMillis("-1.5d 6 hours 1 minute 4 secs 15ms"));
	}

}
