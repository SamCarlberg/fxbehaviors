package com.github.samcarlberg.fxbehaviors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Stubs {

  public static <T> List<T> listOf(T... elements) {
    List<T> list = new ArrayList<>();
    Collections.addAll(list, elements);
    return list;
  }

  public static <T> List<T> listOf(Collection<? extends T> collection) {
    return new ArrayList<>(collection);
  }

  public static <T> Set<T> setOf(T... elements) {
    Set<T> set = new HashSet<>();
    Collections.addAll(set, elements);
    return set;
  }

  public static <T> Set<T> setOf(Collection<? extends T> collection) {
    return new HashSet<>(collection);
  }

}
