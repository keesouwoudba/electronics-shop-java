package com.university.shopping.view.components;

import com.university.shopping.view.contracts.ScreenContext;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class FooterComponent {
    private final VBox root;

    public FooterComponent() {
        this.root = new VBox();
    }

    public Node render(ScreenContext context) {
        // Basic skeleton, will complete in component phase
        return root;
    }
}
