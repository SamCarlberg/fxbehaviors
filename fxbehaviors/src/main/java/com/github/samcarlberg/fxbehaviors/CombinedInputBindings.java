package com.github.samcarlberg.fxbehaviors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.event.Event;

class CombinedInputBindings<B extends BehaviorBase<?, B>> implements InputBindings<B> {

  private final List<InputBindings<B>> bindings;

  CombinedInputBindings(InputBindings<B> a, InputBindings<B> b, InputBindings<B>... more) {
    bindings = new ArrayList<>();
    bindings.add(a);
    bindings.add(b);
    Collections.addAll(bindings, more);
  }

  @Override
  public void fire(Event event, B behavior) {
    for (InputBindings<B> binding : bindings) {
      binding.fire(event, behavior);
    }
  }
}
