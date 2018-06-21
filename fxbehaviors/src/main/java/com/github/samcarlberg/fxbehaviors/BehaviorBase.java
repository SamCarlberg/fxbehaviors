package com.github.samcarlberg.fxbehaviors;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyEvent;

/**
 * Defines interactions with a control. Skins should use behaviors to manipulate the control rather than doing so
 * directly.
 *
 * @param <C> the type of control to define behavior for
 * @param <B> the self type
 */
public class BehaviorBase<C extends Control, B extends BehaviorBase<C, B>> {

  private C control;
  private final Set<KeyBinding<B>> keyBindings;

  private final EventHandler<KeyEvent> keyEventHandler = event -> {
    if (!event.isConsumed()) {
      for (KeyBinding<B> binding : getKeyBindings()) {
        binding.fireIfMatches(event, (B) this);
      }
    }
  };

  /**
   * Creates a new behavior object.
   *
   * @param control     the control to manipulate
   * @param keyBindings optional key bindings
   */
  public BehaviorBase(C control, Collection<KeyBinding<B>> keyBindings) {
    Objects.requireNonNull(control, "Control cannot be null");
    this.control = control;
    this.keyBindings = keyBindings == null || keyBindings.isEmpty()
        ? Set.of()
        : Set.copyOf(keyBindings); // will check for null and duplicate elements

    control.addEventHandler(KeyEvent.ANY, keyEventHandler);
  }

  /**
   * Gets the key bindings for this behavior. The returned set is unmodifiable.
   *
   * @return the set of key bindings for this behavior
   */
  public final Set<KeyBinding<B>> getKeyBindings() {
    return keyBindings;
  }

  /**
   * Gets the control this behavior manipulates.
   */
  public final C getControl() {
    return control;
  }

  public void dispose() {
    control.removeEventHandler(KeyEvent.ANY, keyEventHandler);
    control = null;
  }

}
