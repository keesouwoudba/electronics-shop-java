package com.university.shopping.view.contracts;

import javafx.scene.Node;

public interface ScreenComponent {
    Node render(ScreenContext context);

    default void onAfterEnter(ScreenContext context) {}
    default void onBeforeLeave(ScreenContext context) {}
}
