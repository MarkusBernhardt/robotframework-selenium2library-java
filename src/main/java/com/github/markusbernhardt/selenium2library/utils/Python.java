package com.github.markusbernhardt.selenium2library.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Python {

	public static String join(String glue, String[] strings) {
		return join(glue, Arrays.asList(strings));
	}

	public static String join(String glue, Iterable<String> strings) {
		if (strings == null) {
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		String verkett = "";
		for (String string : strings) {
			stringBuilder.append(verkett);
			stringBuilder.append(string);
			verkett = glue;
		}
		return stringBuilder.toString();
	}

	public static <A, B> Map<A, B> zip(List<A> keys, List<B> values) {
		if (keys.size() != values.size()) {
			return null;
		}

		Map<A, B> map = new HashMap<A, B>();
		Iterator<B> valueIterator = values.listIterator();
		for (A key : keys) {
			map.put(key, valueIterator.next());
		}
		return map;
	}

	public static String osPathBasename(String path) {
		int index = path.lastIndexOf(File.separatorChar) + 1;
		return path.substring(index);
	}

	public static String osPathDirname(String path) {
		int index = path.lastIndexOf(File.separatorChar) + 1;
		String head = path.substring(0, index);
		if (head.length() != 0) {
			String regex = "";
			if (File.separatorChar == '/') {
				regex = String.format("/{%d}", head.length());
			} else {
				regex = String.format("\\\\{%d}", head.length());
			}
			if (!head.matches(regex)) {
				head = rstrip(head, File.separatorChar);
			}
		}
		return head;
	}

	public static String[] osPathSplitDrive(String path) {
		String[] array = new String[2];
		if (File.separatorChar == '/') {
			array[0] = "";
			array[1] = path;
		} else {
			int index = path.indexOf(':') + 1;
			array[0] = path.substring(0, index);
			array[1] = path.substring(index);
		}
		return array;
	}

	public static String lstrip(String string) {
		return lstrip(string, ' ');
	}

	public static String lstrip(String string, char trimChar) {
		int stringLength = string.length();

		int i;
		for (i = 0; i < stringLength && string.charAt(i) == trimChar; i++) {
			/*
			 * Increment i until it is at the location of the first char that
			 * does not match the trimChar given.
			 */
		}

		if (i == 0) {
			return string;
		} else {
			return string.substring(i);
		}
	}

	public static String rstrip(String string) {
		return rstrip(string, ' ');
	}

	public static String rstrip(String string, char trimChar) {
		int lastChar = string.length() - 1;

		int i;
		for (i = lastChar; i >= 0 && string.charAt(i) == trimChar; i--) {
			/*
			 * Decrement i until it is equal to the first char that does not
			 * match the trimChar given.
			 */
		}

		if (i < lastChar) {
			// the +1 is so we include the char at i
			return string.substring(0, i + 1);
		} else {
			return string;
		}
	}
}
