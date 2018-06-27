package com.github.samcarlberg.fxbehaviors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.event.EventType;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public final class MouseBinding<B extends BehaviorBase<?, B>> extends Binding<MouseEvent, B> {

  private final Collection<MouseInput> inputs;

  public MouseBinding(Collection<MouseInput> inputs, EventType<MouseEvent> eventType, Consumer<B> action) {
    super(eventType, action);
    this.inputs = List.copyOf(inputs);
  }

  @Override
  protected boolean match(MouseEvent event) {
    return (getEventType() == MouseEvent.ANY || getEventType().equals(event.getEventType()))
        && inputs.stream().anyMatch(input -> input.match(event));
  }

  /**
   * Creates a new mouse binding builder as an alternative to the constructor.
   *
   * @param <B> the type of the behavior on which the built binding should call an action
   *
   * @return a new builder
   */
  public static <B extends BehaviorBase<?, B>> MouseBindingBuilder<B> builder() {
    return new MouseBindingBuilder<>();
  }

  public static final class MouseBindingBuilder<B extends BehaviorBase<?, B>>
      extends Builder<MouseEvent, B, MouseBinding<B>> {

    private static final Collection<MouseInput> DEFAULT_INPUTS = List.of(MouseInput.PRIMARY_BUTTON);
    private Collection<MouseInput> customInputs = null;

    MouseBindingBuilder() {
      // private constructor - use MouseBinding.builder() to instantiate outside this class
      // Default the event type to MOUSE_PRESSED to be similar to key binding's KEY_PRESSED default
      eventType = MouseEvent.MOUSE_PRESSED;
    }

    public MouseBindingBuilder<B> withMouseInput(MouseInput input) {
      if (customInputs == null) {
        customInputs = new ArrayList<>();
      }
      customInputs.add(input);
      return this;
    }

    public MouseBindingBuilder<B> withMouseInput(MouseButton button, KeyCombination keys) {
      return withMouseInput(new MouseInput(button, keys));
    }

    public MouseBindingBuilder<B> withMouseButton(MouseButton button) {
      return withMouseInput(button, null);
    }

    @Override
    public MouseBindingBuilder<B> onEvent(EventType<MouseEvent> eventType) {
      super.onEvent(eventType);
      return this;
    }

    @Override
    public MouseBindingBuilder<B> withAction(Consumer<B> action) {
      super.withAction(action);
      return this;
    }

    @Override
    public MouseBinding<B> build() {
      return new MouseBinding<>(customInputs == null ? DEFAULT_INPUTS : customInputs, eventType, action);
    }
  }

  /**
   * Represents user input on a mouse event. A mouse input keeps track of the mouse button (primary, secondary, or
   * middle), as well as the states of the various modifier keys.
   */
  public static final class MouseInput {

    private final MouseButton button;
    private final KeyCombination keys;

    private static final KeyCombination BLANK = KeyCombination.NO_MATCH;

    public static final MouseInput PRIMARY_BUTTON = new MouseInput(MouseButton.PRIMARY);
    public static final MouseInput SECONDARY_BUTTON = new MouseInput(MouseButton.SECONDARY);
    public static final MouseInput MIDDLE_BUTTON = new MouseInput(MouseButton.MIDDLE);

    /**
     * Creates a new mouse input with no modifier keys pressed.
     *
     * @param button the button that is pressed
     */
    public MouseInput(MouseButton button) {
      this(button, BLANK);
    }

    /**
     * Creates a new mouse input.
     *
     * @param button       the button that is pressed
     * @param modifierKeys the combination of modifier keys for the input
     */
    public MouseInput(MouseButton button, KeyCombination modifierKeys) {
      this.button = button;
      this.keys = modifierKeys == null ? BLANK : modifierKeys;
    }

    /**
     * Checks if this input matches a mouse event.
     *
     * @param event the event to check
     *
     * @return true if this input matches the event, false if not
     */
    public boolean match(MouseEvent event) {
      return event.getButton().equals(button)
          && modifiersMatch(event);
    }

    private boolean modifiersMatch(MouseEvent event) {
      return (event.isAltDown() == (keys.getAlt() != KeyCombination.ModifierValue.UP))
          && (event.isControlDown() == (keys.getControl() != KeyCombination.ModifierValue.UP))
          && (event.isMetaDown() == (keys.getMeta() != KeyCombination.ModifierValue.UP))
          && (event.isShiftDown() == (keys.getShift() != KeyCombination.ModifierValue.UP))
          && (event.isShortcutDown() == (keys.getShortcut() != KeyCombination.ModifierValue.UP));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      MouseInput that = (MouseInput) o;
      return this.button == that.button
          && Objects.equals(this.keys, that.keys);
    }

    @Override
    public int hashCode() {
      return Objects.hash(button, keys);
    }
  }


}
