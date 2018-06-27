package com.github.samcarlberg.fxbehaviors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static com.github.samcarlberg.fxbehaviors.KeyBindingTest.createKeyEvent;
import static com.github.samcarlberg.fxbehaviors.MouseBindingTest.createMouseEvent;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BehaviorBaseTest {

  /**
   * Gets around Control's static initializer to call Platform code.
   */
  @BeforeAll
  public static void force() {
    try {
      Field field = Application.class.getDeclaredField("userAgentStylesheet");
      field.setAccessible(true);
      field.set(null, "");
    } catch (IllegalAccessException | NoSuchFieldException e) {
      fail("Could not work around static initializer for javafx.scene.control.Control", e);
    }
  }

  @Test
  public void testEvents() {
    AtomicBoolean keyFired = new AtomicBoolean(false);
    AtomicBoolean mouseFired = new AtomicBoolean(false);
    BehaviorBase<Button, ?> behavior = new BehaviorBase(new Button(),
        Set.of(
            KeyBinding.builder()
                .withAction(it -> keyFired.set(true))
                .build(),
            MouseBinding.builder()
                .withAction(it -> mouseFired.set(true))
                .build()
        ));

    behavior.getControl().fireEvent(createKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.A));
    assertAll("Only the key binding should have fired",
        () -> assertTrue(keyFired.get()),
        () -> assertFalse(mouseFired.get())
    );

    keyFired.set(false);
    behavior.getControl().fireEvent(createMouseEvent(MouseEvent.MOUSE_PRESSED, MouseButton.PRIMARY));
    assertAll("Only the mouse binding should have fired",
        () -> assertFalse(keyFired.get()),
        () -> assertTrue(mouseFired.get())
    );
  }

}