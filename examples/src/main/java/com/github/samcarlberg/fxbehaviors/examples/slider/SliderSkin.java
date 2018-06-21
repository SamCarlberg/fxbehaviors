package com.github.samcarlberg.fxbehaviors.examples.slider;

import com.github.samcarlberg.fxbehaviors.BehaviorSkinBase;

import javafx.scene.control.Slider;

public class SliderSkin extends BehaviorSkinBase<Slider, SliderBehavior> {

  public SliderSkin(Slider skinnable) {
    super(skinnable, new SliderBehavior(skinnable));
  }

}
