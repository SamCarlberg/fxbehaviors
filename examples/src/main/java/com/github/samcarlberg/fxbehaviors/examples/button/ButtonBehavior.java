package com.github.samcarlberg.fxbehaviors.examples.button;

import com.github.samcarlberg.fxbehaviors.BehaviorBase;
import com.github.samcarlberg.fxbehaviors.InputBindings;
import com.github.samcarlberg.fxbehaviors.KeyBinding;
import com.github.samcarlberg.fxbehaviors.MouseBinding;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * A behavior for a JavaFX button that will fire the button when a user presses the spacebar.
 */
public class ButtonBehavior extends BehaviorBase<Button, ButtonBehavior> {

  private static final KeyBinding<ButtonBehavior> armOnSpacePressed = KeyBinding.<ButtonBehavior>builder()
      .withKey(KeyCode.SPACE)
      //.onEvent(KeyEvent.KEY_PRESSED) // Optional, since this is the default value
      .withAction(ButtonBehavior::armButton)
      .build();

  private static final KeyBinding<ButtonBehavior> fireOnSpaceReleased = KeyBinding.<ButtonBehavior>builder()
      .withKey(KeyCode.SPACE)
      .onEvent(KeyEvent.KEY_RELEASED)
      .withAction(ButtonBehavior::fireIfArmed)
      .build();

  private static final MouseBinding<ButtonBehavior> armOnMousePressed = MouseBinding.<ButtonBehavior>builder()
      .onEvent(MouseEvent.MOUSE_PRESSED)
      //.withMouseButton(MouseButton.PRIMARY) // Optional, since this is the default value
      .withAction(ButtonBehavior::armButton)
      .build();

  private static final MouseBinding<ButtonBehavior> fireOnMouseReleased = MouseBinding.<ButtonBehavior>builder()
      .onEvent(MouseEvent.MOUSE_RELEASED)
      .withAction(ButtonBehavior::fireIfArmed)
      .build();

  private static final InputBindings<ButtonBehavior> bindings = InputBindings.of(
      armOnSpacePressed,
      fireOnSpaceReleased,
      armOnMousePressed,
      fireOnMouseReleased
  );

  public ButtonBehavior(Button control) {
    super(control, bindings);
  }

  public void armButton() {
    getControl().arm();
  }

  public void fireIfArmed() {
    Button control = getControl();
    if (control.isArmed()) {
      control.fire();
      control.disarm();
    }
  }

}
