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

/**
 * Binds key inputs to actions to fire when a certain key event happens. {@link KeyEvent#KEY_TYPED KEY_TYPED} events are
 * not accepted, as they will match any key combinations. {@link KeyEvent#KEY_PRESSED}, {@link KeyEvent#KEY_RELEASED},
 * and {@link KeyEvent#ANY} will all work.
 *
 * <p>Users are encouraged to use the {@link #builder() builder} to construct new instances instead of the constructor.
 *
 * @param <B> the type of the behavior on which bindings should call actions
 */
public final class KeyBinding<B extends BehaviorBase<?, B>> extends Binding<KeyEvent, B> {

  private final List<KeyCombination> keyCombinations; // the bound key combinations

  /**
   * Creates a new key binding.
   *
   * @param keyCombinations the key combinations that can cause this binding to fire
   * @param eventType       the type of event that can cause this binding to fire
   * @param action          the action to run when this binding is fired
   */
  public KeyBinding(Collection<KeyCombination> keyCombinations, EventType<KeyEvent> eventType, Consumer<B> action) {
    super(eventType, action);
    Objects.requireNonNull(keyCombinations, "Key combinations cannot be null");
    int index = 0;
    for (KeyCombination keyCombination : keyCombinations) {
      Objects.requireNonNull(keyCombination, "Null key combination at index " + index);
      index++;
    }

    if (eventType == KeyEvent.KEY_TYPED) {
      throw new IllegalArgumentException("KEY_TYPED events do not trigger key combinations");
    }

    this.keyCombinations = List.copyOf(keyCombinations);
  }

  @Override
  protected boolean match(KeyEvent event) {
    EventType<KeyEvent> eventType = getEventType();
    return (eventType == KeyEvent.ANY || eventType.equals(event.getEventType()))
        && (keyCombinations.isEmpty()
        || keyCombinations.stream()
        .anyMatch(kc -> kc.match(event)));
  }

  @Override
  public String toString() {
    return String.format("KeyBinding(keyCombinations=%s, eventType=%s)", keyCombinations, getEventType());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    KeyBinding<?> that = (KeyBinding<?>) obj;
    return this.keyCombinations.equals(that.keyCombinations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), keyCombinations);
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
  public static final class KeyBindingBuilder<B extends BehaviorBase<?, B>>
      extends Builder<KeyEvent, B, KeyBinding<B>> {

    private final Collection<KeyCombination> keyCombinations = new ArrayList<>();

    /**
     * Package-private constructor - use {@link KeyBinding#builder KeyBinding.builder()} to create new builders.
     */
    KeyBindingBuilder() {
      super();
      onEvent(KeyEvent.KEY_PRESSED);
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

    @Override
    public KeyBindingBuilder<B> onEvent(EventType<KeyEvent> eventType) {
      super.onEvent(eventType);
      return this;
    }

    @Override
    public KeyBindingBuilder<B> withAction(Consumer<B> action) {
      super.withAction(action);
      return this;
    }

    @Override
    public KeyBinding<B> build() {
      return new KeyBinding<>(keyCombinations, eventType, action);
    }
  }

}
