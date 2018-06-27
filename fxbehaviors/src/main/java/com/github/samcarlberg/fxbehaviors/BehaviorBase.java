package com.github.samcarlberg.fxbehaviors;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.InputEvent;

/**
 * Defines interactions with a control. Skins should use behaviors to manipulate the control rather than doing so
 * directly.
 *
 * @param <C> the type of control to define behavior for
 * @param <B> the self type
 */
public class BehaviorBase<C extends Control, B extends BehaviorBase<C, B>> {

  private C control;
  private final Collection<Binding<?, B>> bindings;

  private final EventHandler<Event> eventHandler = event -> {
    if (!event.isConsumed()) {
      for (Binding<?, B> binding : getBindings()) {
        if (binding.getEventType().equals(event.getEventType())) {
          // The binding is guaranteed to match, so this raw cast is safe
          ((Binding) binding).fireIfMatches(event, this);
        }
      }
    }
  };

  /**
   * Creates a new behavior object.
   *
   * @param control  the control to manipulate
   * @param bindings optional input bindings
   */
  public BehaviorBase(C control, Collection<? extends Binding<?, B>> bindings) {
    Objects.requireNonNull(control, "Control cannot be null");
    this.control = control;
    this.bindings = bindings == null || bindings.isEmpty()
        ? Set.of()
        : Set.copyOf(bindings);

    control.addEventHandler(InputEvent.ANY, eventHandler);
  }

  private Collection<Binding<?, B>> getBindings() {
    return bindings;
  }

  /**
   * Gets the control this behavior manipulates.
   */
  public final C getControl() {
    return control;
  }

  /**
   * Disposes this behavior. Subclasses should be sure to call {@code super.dispose()} if this method is overridden.
   */
  public void dispose() {
    control.removeEventHandler(InputEvent.ANY, eventHandler);
    control = null;
  }

}
