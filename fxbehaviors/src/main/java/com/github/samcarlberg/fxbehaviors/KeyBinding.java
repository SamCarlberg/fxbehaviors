package com.github.samcarlberg.fxbehaviors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Builder;

/**
 * Binds key inputs to actions to fire when a certain key event happens. {@link KeyEvent#KEY_TYPED KEY_TYPED} events are
 * not accepted, as they will match any key combinations. {@link KeyEvent#KEY_PRESSED}, {@link KeyEvent#KEY_RELEASED},
 * and {@link KeyEvent#ANY} will all work.
 *
 * <p>Users are encouraged to use the {@link #builder() builder} to construct new instances instead of the constructor.
 *
 * @param <B> the type of the behavior on which bindings should call actions
 */
public final class KeyBinding<B extends BehaviorBase<?, B>> {

  private final List<KeyCombination> keyCombinations; // the bound key combinations
  private final EventType<KeyEvent> eventType; // the type of event to fire on
  private final Consumer<B> action; // the actual action to fire

  /**
   * Creates a new key binding.
   *
   * @param keyCombinations the key combinations that can cause this binding to fire
   * @param eventType       the type of event that can cause this binding to fire
   * @param action          the action to run when this binding is fired
   */
  public KeyBinding(Collection<KeyCombination> keyCombinations, EventType<KeyEvent> eventType, Consumer<B> action) {
    Objects.requireNonNull(keyCombinations, "Key combinations cannot be null");
    int index = 0;
    for (KeyCombination keyCombination : keyCombinations) {
      Objects.requireNonNull(keyCombination, "Null key combination at index " + index);
      index++;
    }
    Objects.requireNonNull(eventType, "Event type cannot be null");
    Objects.requireNonNull(action, "Action cannot be null");

    if (eventType == KeyEvent.KEY_TYPED) {
      throw new IllegalArgumentException("KEY_TYPED events do not trigger key combinations");
    }

    this.keyCombinations = List.copyOf(keyCombinations);
    this.eventType = eventType;
    this.action = action;
  }

  /**
   * Checks if this binding can be fired as a result of an event.
   *
   * @param event the event to check
   *
   * @return true if this binding can be fired, false if not
   */
  private boolean matches(KeyEvent event) {
    return (eventType == KeyEvent.ANY || eventType.equals(event.getEventType()))
        && keyCombinations.stream()
        .anyMatch(kc -> kc.match(event));
  }

  /**
   * Fires this key binding if it matches a key event. Otherwise, this will do nothing.
   *
   * @param event the event to check
   */
  public void fireIfMatches(KeyEvent event, B behavior) {
    if (matches(event)) {
      action.accept(behavior);
    }
  }

  @Override
  public String toString() {
    return String.format("KeyBinding(keyCombinations=%s, eventType=%s)", keyCombinations, eventType);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    KeyBinding that = (KeyBinding) obj;
    return this.keyCombinations.equals(that.keyCombinations)
        && this.eventType.equals(that.eventType)
        && this.action.equals(that.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyCombinations, eventType, action);
  }

  /**
   * Creates a new key binding builder as an alternative to the constructor.
   *
   * @param <B> the type of the behavior on which the built binding should call an action
   *
   * @return a new builder
   */
  public static <B extends BehaviorBase<?, B>> KeyBindingBuilder<B> builder() {
    return new KeyBindingBuilder<>();
  }

  /**
   * A builder for key bindings.
   */
  public static final class KeyBindingBuilder<B extends BehaviorBase<?, B>> implements Builder<KeyBinding<B>> {

    private Collection<KeyCombination> keyCombinations = new ArrayList<>();
    private EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    private Consumer<B> action;

    /**
     * Package-private constructor - use {@link KeyBinding#builder KeyBinding.builder()} to create new builders.
     */
    KeyBindingBuilder() {
    }

    /**
     * Adds a single key with no modifiers. This is equivalent to {@code withKey(key, KeyCombination.ALT_UP,
     * KeyCombination.CONTROL_UP, KeyCombination.SHIFT_UP, KeyCombination.SHORTCUT_UP)}.
     *
     * @param keyCode the key to fire on
     *
     * @return this builder
     */
    public KeyBindingBuilder<B> withKey(KeyCode keyCode) {
      keyCombinations.add(new KeyCodeCombination(keyCode));
      return this;
    }

    /**
     * Adds a single key combination. This is equivalent to {@code withKey(new KeyCodeCombination(key, modifiers)}.
     *
     * @param key       the key to fire on
     * @param modifiers the key modifiers that should be active to trigger this specific key combination
     *
     * @return this builder
     */
    public KeyBindingBuilder<B> withKey(KeyCode key, KeyCombination.Modifier... modifiers) {
      keyCombinations.add(new KeyCodeCombination(key, modifiers));
      return this;
    }

    /**
     * Sets the event type to fire on. Default value {@link KeyEvent#KEY_PRESSED}.
     *
     * @param eventType the event type to fire on
     *
     * @return this builder
     */
    public KeyBindingBuilder<B> onEvent(EventType<KeyEvent> eventType) {
      this.eventType = eventType;
      return this;
    }

    /**
     * Sets the action to run when the key binding fires. This <b>must</b> be specified before calling {@link #build()}.
     *
     * @param action the action to run when the key binding fires
     *
     * @return this builder
     */
    public KeyBindingBuilder<B> withAction(Consumer<B> action) {
      this.action = action;
      return this;
    }

    @Override
    public KeyBinding<B> build() {
      return new KeyBinding<>(this.keyCombinations, this.eventType, this.action);
    }
  }

}
