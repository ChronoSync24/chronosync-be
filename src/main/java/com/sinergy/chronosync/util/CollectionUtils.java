package com.sinergy.chronosync.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for working with collections.
 */
public class CollectionUtils {

	/**
	 * Checks if the given collection is null or empty.
	 *
	 * @param collection The collection to check.
	 * @param <T> The type of the collection elements.
	 * @return true if the collection is null or empty, false otherwise.
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Concatenates two collections and returns a new collection containing all elements of both.
	 *
	 * @param first The first collection.
	 * @param second The second collection.
	 * @param <T> The type of the collection elements.
	 * @return A new collection containing all elements of both input collections.
	 */
	public static <T> List<T> concat(Collection<T> first, Collection<T> second) {
		if (first == null && second == null) {
			return Collections.emptyList();
		}

		if (first == null) {
			return List.copyOf(second);
		}

		if (second == null) {
			return List.copyOf(first);
		}

		List<T> result = new ArrayList<>(first);
		result.addAll(second);
		return result;
	}
}

