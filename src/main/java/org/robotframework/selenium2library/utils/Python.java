package org.robotframework.selenium2library.utils;

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
}
