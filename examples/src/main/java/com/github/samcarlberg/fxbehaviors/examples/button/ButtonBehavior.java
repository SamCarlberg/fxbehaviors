package com.github.samcarlberg.fxbehaviors.examples.button;

import com.github.samcarlberg.fxbehaviors.BehaviorBase;
import com.github.samcarlberg.fxbehaviors.KeyBinding;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A behavior for a JavaFX button that will fire the button when a user presses the spacebar.
 */
public class ButtonBehavior extends BehaviorBase<Button, ButtonBehavior> {

  private static final KeyBinding<ButtonBehavior> armOnSpacePressed = KeyBinding.<ButtonBehavior>builder()
      .withKey(KeyCode.SPACE)
      //.onEvent(KeyEvent.KEY_PRESSED) // Optional, since this is the default value
      .withAction(ButtonBehavior::prime)
      .build();

  private static final KeyBinding<ButtonBehavior> fireOnSpaceReleased = KeyBinding.<ButtonBehavior>builder()
      .withKey(KeyCode.SPACE)
      .onEvent(KeyEvent.KEY_RELEASED)
      .withAction(ButtonBehavior::fireIfArmed)
      .build();

  private static final List<KeyBinding<ButtonBehavior>> keyBindings = List.of(armOnSpacePressed, fireOnSpaceReleased);

  public ButtonBehavior(Button control) {
    super(control, keyBindings);
  }

  public void prime() {
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
