package com.university.shopping.view.layout;

import com.university.shopping.view.components.FooterComponent;
import com.university.shopping.view.components.HeaderNavComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AppShell {
    private final BorderPane root;
    private final HeaderNavComponent header;
    private final ScrollPane scrollRoot;
    private final VBox pageFlow;
    private final FooterComponent footer;

    public AppShell() {
        this.root = new BorderPane();
        this.header = new HeaderNavComponent();
        this.scrollRoot = new ScrollPane();
        this.pageFlow = new VBox();
        this.footer = new FooterComponent();

        scrollRoot.setContent(pageFlow);
        scrollRoot.setFitToWidth(true);
        root.setCenter(scrollRoot);
    }

    public Parent buildRoot(ScreenContext context) {
        root.setTop(header.render(context));
        return root;
    }

    public void setPageContent(Node pageNode) {
        pageFlow.getChildren().clear();
        if (pageNode != null) {
            pageFlow.getChildren().add(pageNode);
        }
        // Footer is always rendered at bottom of page flow
        pageFlow.getChildren().add(footer.render(null)); // We'll pass context later or cache it
    }

    public BorderPane getRoot() {
        return root;
    }
    
    public HeaderNavComponent getHeader() {
        return header;
    }
}
