package com.university.shopping.view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NotificationBarComponent {
    private final HBox root;
    private final Label messageLabel;

    public NotificationBarComponent() {
        this.root = new HBox();
        this.messageLabel = new Label();
        this.root.getChildren().add(messageLabel);
        this.root.setVisible(false);
        this.root.setManaged(false);
    }

    public Node getNode() {
        return root;
    }

    public void showSuccess(String message) {
        messageLabel.setText(message);
        root.setStyle("-fx-background-color: lightgreen; -fx-padding: 10;");
        root.setVisible(true);
        root.setManaged(true);
    }

    public void showError(String message) {
        messageLabel.setText(message);
        root.setStyle("-fx-background-color: salmon; -fx-padding: 10;");
        root.setVisible(true);
        root.setManaged(true);
    }

    public void clear() {
        messageLabel.setText("");
        root.setVisible(false);
        root.setManaged(false);
    }
}
