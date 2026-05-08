package com.university.shopping.view.pages;

import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PrivacyPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        StyleHelper.applyPageBackground(root);

        VBox content = StyleHelper.createPageContainer();
        content.setPadding(new Insets(40));
        content.setSpacing(20);

        Label title = new Label("Privacy Policy");
        title.getStyleClass().add("page-title");

        Label text = new Label(
            "Effective Date: January 1, 2026\n\n" +
            "Your privacy is important to TechVolt Electronics. It is our policy to respect " +
            "your privacy regarding any information we may collect from you across our application " +
            "and website.\n\n" +
            "Data Collection\n" +
            "We only ask for personal information when we truly need it to provide a service " +
            "to you. We collect it by fair and lawful means, with your knowledge and consent.\n" +
            "Information like your name, email address, and order history is used exclusively " +
            "to fulfill purchases and enhance your browsing experience.\n\n" +
            "Data Sharing\n" +
            "We don't share any personally identifying information publicly or with third parties, " +
            "except when required to by law or when absolutely necessary to process payments " +
            "through our secure partners.\n\n" +
            "Security\n" +
            "The data we store is protected within commercially acceptable means to prevent loss " +
            "and theft, as well as unauthorized access or modification.\n"
        );
        text.setWrapText(true);
        text.getStyleClass().add("page-text");

        content.getChildren().addAll(title, text);
        root.getChildren().add(content);
        return root;
    }
}
