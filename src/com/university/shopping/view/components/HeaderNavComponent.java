package com.university.shopping.view.components;

import com.university.shopping.view.contracts.ScreenContext;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class HeaderNavComponent {
    private final HBox root;
    private final Label userBadge;

    public HeaderNavComponent() {
        this.root = new HBox();
        this.userBadge = new Label();
    }

    public Node render(ScreenContext context) {
        // Basic skeleton, will complete in component phase
        return root;
    }

    public void refresh(ScreenContext context) {
        
    }
}
