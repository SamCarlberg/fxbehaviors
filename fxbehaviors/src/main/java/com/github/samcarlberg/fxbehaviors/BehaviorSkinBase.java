package com.github.samcarlberg.fxbehaviors;

import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;

/**
 * A type of skin that interacts with a behavior to interact with the control. Skins should use the behavior to
 * modify the control properties instead of directly accessing the control.
 *
 * @param <C>  the type of the skinned control
 * @param <B> the type of the behavior for the control
 */
public class BehaviorSkinBase<C extends Control, B extends BehaviorBase<C, B>> extends SkinBase<C> {

  private B behavior;

  public BehaviorSkinBase(C skinnable, B behavior) {
    super(skinnable);
    this.behavior = behavior;
  }

  public final B getBehavior() {
    return behavior;
  }

  @Override
  public void dispose() {
    if (behavior != null) {
      behavior.dispose();
    }
  }
}
