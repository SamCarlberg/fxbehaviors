package com.github.samcarlberg.fxbehaviors.examples.button;

import com.github.samcarlberg.fxbehaviors.BehaviorSkinBase;

import javafx.scene.control.Button;

/**
 * A custom skin for a JavaFX button that will use a custom behavior.
 */
public class ButtonSkin extends BehaviorSkinBase<Button, ButtonBehavior> {

  public ButtonSkin(Button skinnable) {
    super(skinnable, new ButtonBehavior(skinnable));
  }

}
