package com.github.samcarlberg.fxbehaviors;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;

import javafx.event.Event;

public interface InputBindings<B extends BehaviorBase<?, B>> {

  /**
   * Creates a new set of input bindings with no filter.
   *
   * @param bindings the bindings
   */
  static <B extends BehaviorBase<?, B>> InputBindings<B> of(Collection<? extends Binding<?, B>> bindings) {
    return new DefaultInputBindings<>(bindings);
  }

  /**
   * Creates a new set of input bindings with no filter.
   *
   * @param bindings the bindings
   */
  @SafeVarargs
  static <B extends BehaviorBase<?, B>> InputBindings<B> of(Binding<?, B>... bindings) {
    return new DefaultInputBindings<>(Set.of(bindings));
  }


  /**
   * Creates a new set of input bindings.
   *
   * @param filter   an event filter to use. If an event is fired that does not pass this filter, then no bindings will
   *                 fire even if they match that event
   * @param bindings the bindings
   */
  static <B extends BehaviorBase<?, B>> InputBindings<B> of(BiPredicate<? super Event, B> filter,
                                                            Collection<? extends Binding<?, B>> bindings) {
    return new DefaultInputBindings<>(filter, bindings);
  }


  /**
   * Creates a new set of input bindings.
   *
   * @param filter   an event filter to use. If an event is fired that does not pass this filter, then no bindings will
   *                 fire even if they match that event
   * @param bindings the bindings
   */
  @SafeVarargs
  static <B extends BehaviorBase<?, B>> InputBindings<B> of(BiPredicate<? super Event, B> filter,
                                                            Binding<?, B>... bindings) {
    return new DefaultInputBindings<>(filter, Set.of(bindings));
  }

  /**
   * Combines multiple input bindings.
   *
   * @param first  the first set of bindings
   * @param second the second set of bindings
   * @param more   optional, extra bindings to combine
   */
  static <B extends BehaviorBase<?, B>> InputBindings<B> combine(InputBindings<B> first,
                                                                 InputBindings<B> second,
                                                                 InputBindings<B>... more) {
    return new CombinedInputBindings<>(first, second, more);
  }

  /**
   * Fires the bindings in response to an event.
   *
   * @param event    the event that was fired
   * @param behavior the behavior on which to fire the bindings
   */
  void fire(Event event, B behavior);

}
