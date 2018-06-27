# FX Behaviors

[ ![Download](https://api.bintray.com/packages/samcarlberg/maven-artifacts/fxbehaviors/images/download.svg) ](https://bintray.com/samcarlberg/maven-artifacts/fxbehaviors/_latestVersion)

An alternative to JavaFX behavior classes that were placed in an internal module
in Java 9 with no publicly accessible replacement. This is not a one-to-one
replacement; the bindings API is more robust and functionally-focused;
actions are specified by lambda expressions or method references instead of
magic strings which API consumers must provide and `switch` on.

## Defining Custom Behaviors

### Old
```java
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

public class OldBehavior extends BehaviorBase<Control> {
  private static final String ACTION_NAME = "AnAction";
  
  public OldBehavior(Control control) {
    super(control, List.of(new KeyBinding(KeyCode.SPACE, ACTION_NAME)));
  }
  
  @Override
  protected void callAction(String action) {
    switch (action) {
      case ACTION_NAME:
        doAction();
        break;
      default:
        super.callAction(action);
        break;
    }
  }
  
  public void doAction() {
    // ...
  }
  
}
```

### New
```java
import com.github.samcarlberg.fxbehaviors.BehaviorBase;
import com.github.samcarlberg.fxbehaviors.InputBindings;
import com.github.samcarlberg.fxbehaviors.KeyBinding;

public class NewBehavior extends BehaviorBase<Control, NewBehavior> {
  
  private static final KeyBinding<NewBehavior> doActionOnSpace = KeyBinding.<NewBehavior>builder()
    .withKey(KeyCode.SPACE)
    .withAction(NewBehavior::doAction)
    .build();
  
  public NewBehavior(Control control) {
    super(control, InputBindings.of(doActionOnSpace));
  }
  
  public void doAction() {
    // ...
  }
}
``` 
