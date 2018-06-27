package com.github.samcarlberg.fxbehaviors;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class InputBindingsTest {

  @Test
  public void testWithNonPassingFilter() {
    final AtomicBoolean fired = new AtomicBoolean(false);
    final EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    final KeyCode keyCode = KeyCode.A;
    final InputBindings<?> bindings = InputBindings.of((e, b) -> false, List.of(
        KeyBinding.builder()
            .onEvent(eventType)
            .withKey(keyCode)
            .withAction(__ -> fired.set(true))
            .build()
    ));

    bindings.fire(KeyBindingTest.createKeyEvent(eventType, keyCode), null);
    assertFalse(fired.get(), "Bindings should not fire when the filter cannot be passed");
  }

}
