package com.github.samcarlberg.fxbehaviors;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;

import javafx.event.Event;

final class DefaultInputBindings<B extends BehaviorBase<?, B>> implements InputBindings<B> {

  private final BiPredicate<? super Event, B> filter;
  private final Set<? extends Binding<?, B>> bindings;

  DefaultInputBindings(Collection<? extends Binding<?, B>> bindings) {
    this((e, b) -> true, bindings);
  }

  DefaultInputBindings(BiPredicate<? super Event, B> filter, Collection<? extends Binding<?, B>> bindings) {
    this.filter = filter;
    this.bindings = Set.copyOf(bindings);
  }

  @Override
  public void fire(Event event, B behavior) {
    if (filter.test(event, behavior)) {
      bindings.stream()
          .filter(b -> b.getEventType().equals(event.getEventType()))
          .forEach(b -> ((Binding<Event, B>) b).fireIfMatches(event, behavior)); // Guaranteed to match, so cast is okay
    }
  }
}
