package com.github.samcarlberg.fxbehaviors;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import static com.github.samcarlberg.fxbehaviors.Stubs.setOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyBindingTest {

  @Test
  public void sanityCheck() {
    Consumer<? extends BehaviorBase> nop = x -> {};
    EventType<KeyEvent> type = KeyEvent.KEY_PRESSED;
    List<KeyCombination> keyCombinations = Collections.singletonList(new KeyCodeCombination(KeyCode.A));
    assertAll("Constructor sanity check",
        () -> assertThrows(NullPointerException.class,
            () -> new KeyBinding<>(null, type, nop), "Null key list"),
        () -> assertThrows(NullPointerException.class,
            () -> new KeyBinding<>(Collections.singletonList(null), type, nop), "Key list with null entries"),
        () -> assertThrows(NullPointerException.class,
            () -> new KeyBinding<>(keyCombinations, null, nop), "Null event type"),
        () -> assertThrows(NullPointerException.class,
            () -> new KeyBinding<>(keyCombinations, type, null), "Null action"),
        () -> assertThrows(IllegalArgumentException.class,
            () -> new KeyBinding<>(keyCombinations, KeyEvent.KEY_TYPED, nop), "KEY_TYPED event")
    );
  }

  @Test
  public void testSingleKeyCodeNoMods() {
    AtomicBoolean fired = new AtomicBoolean(false);
    KeyCode keyCode = KeyCode.A;
    EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    KeyBinding<?> binding = KeyBinding.builder()
        .withKey(keyCode)
        .onEvent(eventType)
        .withAction(x -> fired.set(true))
        .build();
    KeyEvent noMods = createKeyEvent(eventType, keyCode);

    binding.fireIfMatches(noMods, null);
    assertTrue(fired.get(), "Binding should have fired on event " + noMods);

    fired.set(false);
    KeyEvent withMods = createKeyEvent(eventType, keyCode, KeyCombination.CONTROL_DOWN);

    binding.fireIfMatches(withMods, null);
    assertFalse(fired.get(), "Binding should NOT have fired on event " + withMods);
  }

  @Test
  public void testSingleKeyCodeWithMods() {
    AtomicBoolean fired = new AtomicBoolean(false);
    KeyCode keyCode = KeyCode.SPACE;
    EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    KeyBinding<?> binding = KeyBinding.builder()
        .withKey(keyCode, KeyCombination.SHIFT_DOWN)
        .onEvent(eventType)
        .withAction(x -> fired.set(true))
        .build();
    KeyEvent withMods = createKeyEvent(eventType, keyCode, KeyCombination.SHIFT_DOWN);

    binding.fireIfMatches(withMods, null);
    assertTrue(fired.get(), "Binding should have fired on event " + withMods);

    fired.set(false);
    KeyEvent noMods = createKeyEvent(eventType, keyCode);

    binding.fireIfMatches(noMods, null);
    assertFalse(fired.get(), "Binding should NOT have fired on event " + noMods);
  }

  @Test
  public void testMultipleKeysAllMatching() {
    AtomicBoolean fired = new AtomicBoolean(false);
    KeyCode keyCode = KeyCode.B;
    EventType<KeyEvent> eventType = KeyEvent.KEY_RELEASED;
    KeyBinding<?> binding = KeyBinding.builder()
        .withKey(keyCode)
        .withKey(keyCode, KeyCombination.CONTROL_DOWN)
        .onEvent(eventType)
        .withAction(x -> fired.set(true))
        .build();
    KeyEvent noMods = createKeyEvent(eventType, keyCode);

    binding.fireIfMatches(noMods, null);
    assertTrue(fired.get(), "Binding should have fired for event " + noMods);

    fired.set(false);

    KeyEvent withMods = createKeyEvent(eventType, keyCode, KeyCombination.CONTROL_DOWN);
    binding.fireIfMatches(withMods, null);
    assertTrue(fired.get(), "Binding should have fired for event " + withMods);
  }

  @Test
  public void testMultipleKeysOneMatching() {
    AtomicBoolean fired = new AtomicBoolean(false);
    KeyCode keyCode = KeyCode.L;
    EventType<KeyEvent> eventType = KeyEvent.KEY_RELEASED;
    KeyBinding<?> binding = KeyBinding.builder()
        .withKey(keyCode)
        .withKey(keyCode, KeyCombination.CONTROL_DOWN)
        .onEvent(eventType)
        .withAction(x -> fired.set(true))
        .build();
    KeyEvent noMods = createKeyEvent(eventType, keyCode);

    binding.fireIfMatches(noMods, null);
    assertTrue(fired.get(), "Binding should have fired for event " + noMods);

    fired.set(false);

    KeyEvent withMods = createKeyEvent(eventType, keyCode, KeyCombination.SHIFT_DOWN);
    binding.fireIfMatches(withMods, null);
    assertFalse(fired.get(), "Binding should NOT have fired for event " + withMods);
  }

  @Test
  public void testMultipleKeysNoneMatching() {
    AtomicBoolean fired = new AtomicBoolean(false);
    KeyCode keyCode = KeyCode.ESCAPE;
    EventType<KeyEvent> eventType = KeyEvent.KEY_RELEASED;
    KeyBinding<?> binding = KeyBinding.builder()
        .withKey(keyCode)
        .withKey(keyCode, KeyCombination.CONTROL_DOWN)
        .onEvent(eventType)
        .withAction(x -> fired.set(true))
        .build();
    KeyEvent event = createKeyEvent(KeyEvent.KEY_PRESSED, keyCode);

    binding.fireIfMatches(event, null);
    assertFalse(fired.get(), "Binding should NOT have fired for event " + event);
  }

  public static KeyEvent createKeyEvent(EventType<KeyEvent> type, KeyCode keyCode, KeyCombination.Modifier... modifiers) {
    Collection<KeyCombination.Modifier> mods = setOf(modifiers);
    return new KeyEvent(
        KeyBindingTest.class,
        null,
        type,
        keyCode.impl_getChar(),
        keyCode.impl_getChar(),
        keyCode,
        mods.contains(KeyCombination.SHIFT_DOWN),
        mods.contains(KeyCombination.CONTROL_DOWN),
        mods.contains(KeyCombination.ALT_DOWN),
        mods.contains(KeyCombination.META_DOWN)
    );
  }

}