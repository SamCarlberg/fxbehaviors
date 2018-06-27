package com.github.samcarlberg.fxbehaviors.examples.slider;

import com.github.samcarlberg.fxbehaviors.BehaviorBase;
import com.github.samcarlberg.fxbehaviors.InputBindings;
import com.github.samcarlberg.fxbehaviors.KeyBinding;

import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

/**
 * A behavior for JavaFX sliders that permits control over the position of the thumb with keyboard inputs. Left
 * and right arrow keys (and their keypad equivalents) will decrement and increment the position of the thumb, and
 * the home and end buttons will move it to the start or end of the track.
 */
public class SliderBehavior extends BehaviorBase<Slider, SliderBehavior> {

  // Decrement the slider on LEFT or KP_LEFT
  private static final KeyBinding<SliderBehavior> decrement = KeyBinding.<SliderBehavior>builder()
      .withKey(KeyCode.LEFT)
      .withKey(KeyCode.KP_LEFT)
      .withAction(SliderBehavior::decrement)
      .build();

  // Increment the slider on RIGHT or KP_RIGHT
  private static final KeyBinding<SliderBehavior> increment = KeyBinding.<SliderBehavior>builder()
      .withKey(KeyCode.RIGHT)
      .withKey(KeyCode.KP_RIGHT)
      .withAction(SliderBehavior::increment)
      .build();

  // Move the slider to the start on HOME or Ctrl+LEFT
  private static final KeyBinding<SliderBehavior> toStart = KeyBinding.<SliderBehavior>builder()
      .withKey(KeyCode.HOME)
      .withKey(KeyCode.LEFT, KeyCombination.CONTROL_DOWN)
      .withAction(SliderBehavior::goToStart)
      .build();

  // Move the slider to the end on END or Ctrl+RIGHT
  private static final KeyBinding<SliderBehavior> toEnd = KeyBinding.<SliderBehavior>builder()
      .withKey(KeyCode.END)
      .withKey(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN)
      .withAction(SliderBehavior::goToEnd)
      .build();

  private static final InputBindings<SliderBehavior> keyBindings = InputBindings.of(
      decrement,
      increment,
      toStart,
      toEnd
  );

  public SliderBehavior(Slider control) {
    super(control, keyBindings);
  }

  public void decrement() {
    getControl().decrement();
  }

  public void increment() {
    getControl().increment();
  }

  public void goToStart() {
    Slider control = getControl();
    control.setValue(control.getMin());
  }

  public void goToEnd() {
    Slider control = getControl();
    control.setValue(control.getMax());
  }

}
