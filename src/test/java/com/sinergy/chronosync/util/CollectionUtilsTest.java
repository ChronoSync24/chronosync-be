package com.sinergy.chronosync.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CollectionUtils} class.
 *
 * <p>Tests the utility methods for working with collections, including checking
 * if a collection is null or empty, and concatenating two collections.</p>
 */
class CollectionUtilsTest {

	/**
	 * Tests the {@link CollectionUtils#isEmpty(Collection)} method.
	 *
	 * <p>Verifies that null collections, empty collections, and non-empty collections
	 * are correctly identified.</p>
	 */
	@Test
	void testIsEmpty() {
		assertTrue(CollectionUtils.isEmpty(Collections.emptyList()));
		assertFalse(CollectionUtils.isEmpty(List.of("item1")));
	}

	/**
	 * Tests the {@link CollectionUtils#concat(Collection, Collection)} method.
	 *
	 * <p>Verifies that the method handles null inputs and concatenates collections correctly.</p>
	 */
	@Test
	void testConcat() {
		assertEquals(Collections.emptyList(), CollectionUtils.concat(null, null));
		assertEquals(List.of("item2", "item3"), CollectionUtils.concat(null, List.of("item2", "item3")));
		assertEquals(List.of("item1", "item2"), CollectionUtils.concat(List.of("item1", "item2"), null));
		assertEquals(
			List.of("item1", "item2", "item3", "item4"),
			CollectionUtils.concat(
				List.of("item1", "item2"),
				List.of("item3", "item4")
			)
		);
	}
}
