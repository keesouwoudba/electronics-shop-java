package com.university.shopping.view.layout;

import com.university.shopping.view.components.FooterComponent;
import com.university.shopping.view.components.HeaderNavComponent;
import com.university.shopping.view.components.NotificationBarComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.layout.Pane;

public class AppShell {
    private final BorderPane root;
    private final HeaderNavComponent header;
    private final NotificationBarComponent notificationBar;
    private final ScrollPane scrollRoot;
    private final VBox pageFlow;
    private final FooterComponent footer;
    private final VBox topContainer;

    public AppShell() {
        this.root = new BorderPane();
        this.header = new HeaderNavComponent();
        this.notificationBar = new NotificationBarComponent();
        this.scrollRoot = new ScrollPane();
        this.pageFlow = new VBox();
        this.footer = new FooterComponent();
        this.topContainer = new VBox();

        scrollRoot.setContent(pageFlow);
        scrollRoot.setFitToWidth(true);
        root.setCenter(scrollRoot);
    }

    public Parent buildRoot(ScreenContext context) {
        topContainer.getChildren().clear();
        topContainer.getChildren().addAll(header.render(context), notificationBar.getNode());
        root.setTop(topContainer);
        return root;
    }

    public void updateHeader(ScreenContext context) {
        // refresh header state and re-render header node
        header.refresh(context);
        if (!topContainer.getChildren().isEmpty()) {
            topContainer.getChildren().set(0, header.render(context));
        }
    }

    public void setContent(Node pageNode) {
        setPageContent(pageNode);
    }

    public void showNotification(String message, boolean isError) {
        if (isError) notificationBar.showError(message);
        else notificationBar.showSuccess(message);
    }

    public void hideNotification() {
        notificationBar.clear();
    }

    public void setPageContent(Node pageNode) {
        pageFlow.getChildren().clear(); //always remove previous content
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
