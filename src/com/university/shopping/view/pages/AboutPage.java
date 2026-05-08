package com.university.shopping.view.pages;

import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AboutPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        StyleHelper.applyPageBackground(root);

        VBox content = StyleHelper.createPageContainer();
        content.setPadding(new Insets(40));
        content.setSpacing(20);

        Label title = new Label("About Us");
        title.getStyleClass().add("page-title");

        Label text = new Label(
            "Welcome to TechVolt Electronics!\n\n" +
            "Since our founding, we have been dedicated to providing our customers with " +
            "the highest quality electronics and components. We specialize in bringing " +
            "cutting-edge technology straight to your home or office workspace.\n\n" +
            "Whether you're looking for premium laptops, top-tier audio equipment, " +
            "or the latest mobile devices, our catalog is meticulously curated to " +
            "meet the standards of tech enthusiasts and professionals alike.\n\n" +
            "Thank you for choosing TechVolt Electronics."
        );
        text.setWrapText(true);
        text.getStyleClass().add("page-text");

        content.getChildren().addAll(title, text);
        root.getChildren().add(content);
        return root;
    }
}
