package com.github.samcarlberg.fxbehaviors;

import java.util.Objects;

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
  private InputBindings<B> inputBindings;

  private final EventHandler<Event> eventHandler = this::fireBindings;

  /**
   * Creates a new behavior object.
   *
   * @param control  the control to manipulate
   * @param bindings optional input bindings
   */
  public BehaviorBase(C control, InputBindings<B> bindings) {
    Objects.requireNonNull(control, "Control cannot be null");
    this.control = control;
    this.inputBindings = bindings;

    control.addEventHandler(InputEvent.ANY, eventHandler);
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

  private void fireBindings(Event event) {
    if (!event.isConsumed() && inputBindings != null) {
      inputBindings.fire(event, (B) this);
    }
  }
}
