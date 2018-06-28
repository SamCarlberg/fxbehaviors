package com.github.samcarlberg.fxbehaviors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;

import javafx.event.Event;

import static com.github.samcarlberg.fxbehaviors.Stubs.listOf;
import static com.github.samcarlberg.fxbehaviors.Stubs.setOf;

public final class InputBindings<B extends BehaviorBase<?, B>> {

  private final BiPredicate<? super Event, B> filter;
  private final Set<Binding<?, B>> bindings;

  /**
   * Creates a new set of input bindings with no filter.
   *
   * @param bindings the bindings
   */
  public static <B extends BehaviorBase<?, B>> InputBindings<B> of(Collection<? extends Binding<?, B>> bindings) {
    return new InputBindings<>(bindings);
  }

  /**
   * Creates a new set of input bindings with no filter.
   *
   * @param bindings the bindings
   */
  @SafeVarargs
  public static <B extends BehaviorBase<?, B>> InputBindings<B> of(Binding<?, B>... bindings) {
    return new InputBindings<>(listOf(bindings));
  }


  /**
   * Creates a new set of input bindings.
   *
   * @param filter   an event filter to use. If an event is fired that does not pass this filter, then no bindings will
   *                 fire even if they match that event
   * @param bindings the bindings
   */
  public static <B extends BehaviorBase<?, B>> InputBindings<B> of(BiPredicate<? super Event, B> filter,
                                                                   Collection<? extends Binding<?, B>> bindings) {
    return new InputBindings<>(filter, bindings);
  }


  /**
   * Creates a new set of input bindings.
   *
   * @param filter   an event filter to use. If an event is fired that does not pass this filter, then no bindings will
   *                 fire even if they match that event
   * @param bindings the bindings
   */
  @SafeVarargs
  public static <B extends BehaviorBase<?, B>> InputBindings<B> of(BiPredicate<? super Event, B> filter,
                                                                   Binding<?, B>... bindings) {
    return new InputBindings<>(filter, Arrays.asList(bindings));
  }

  private InputBindings(Collection<? extends Binding<?, B>> bindings) {
    this((e, b) -> true, bindings);
  }

  private InputBindings(BiPredicate<? super Event, B> filter, Collection<? extends Binding<?, B>> bindings) {
    this.filter = filter;
    this.bindings = setOf(bindings);
  }

  /**
   * Fires the bindings in response to an event if the filter is passed.
   *
   * @param event    the event that was fired
   * @param behavior the behavior on which to fire the bindings
   */
  public void fire(Event event, B behavior) {
    if (filter.test(event, behavior)) {
      bindings.stream()
          .filter(b -> b.getEventType().equals(event.getEventType()))
          .forEach(b -> ((Binding<Event, B>) b).fireIfMatches(event, behavior)); // Guaranteed to match, so cast is okay
    }
  }

}
