package com.github.samcarlberg.fxbehaviors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stubs for methods added in Java 9+ that are unavailable for use in Java 8.
 */
class Stubs {

  /**
   * Stub for {@code List.of(T...)}.
   *
   * @param elements the elements in the list
   * @param <T>      the type of the elements in the list
   *
   * @return a new list containing the given elements
   */
  @SafeVarargs
  public static <T> List<T> listOf(T... elements) {
    List<T> list = new ArrayList<>();
    Collections.addAll(list, elements);
    return list;
  }

  /**
   * Stub for {@code List.of(Collection<T>)}.
   *
   * @param collection a collection of elements to add to a list
   * @param <T>        the type of elements in the list
   *
   * @return a new list containing the elements in the given collection
   */
  public static <T> List<T> listOf(Collection<? extends T> collection) {
    return new ArrayList<>(collection);
  }

  /**
   * Stub for {@code Set.of(T...)}.
   *
   * @param elements the elements in the set
   * @param <T>      the type of the elements in the set
   *
   * @return a new set containing the given elements
   */
  @SafeVarargs
  public static <T> Set<T> setOf(T... elements) {
    Set<T> set = new HashSet<>();
    Collections.addAll(set, elements);
    return set;
  }

  /**
   * Stub for {@code Set.of(Collection<T>)}.
   *
   * @param collection a collection of elements to add to a set
   * @param <T>        the type of elements in the set
   *
   * @return a new set containing the elements in the given collection
   */
  public static <T> Set<T> setOf(Collection<? extends T> collection) {
    return new HashSet<>(collection);
  }

}
