package com.university.shopping.view.pages;

import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ContactPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        StyleHelper.applyPageBackground(root);

        VBox content = StyleHelper.createPageContainer();
        content.setPadding(new Insets(40));
        content.setSpacing(20);

        Label title = new Label("Contact Us");
        title.getStyleClass().add("page-title");

        Label text = new Label(
            "We'd love to hear from you! If you have any inquiries regarding your orders, " +
            "products, or our services, please reach out to us using the information below.\n\n" +
            "Customer Support:\n" +
            "Email: support@techvolt.com\n" +
            "Phone: 1-800-TECH-VOLT\n" +
            "Hours: Monday - Friday, 9:00 AM - 6:00 PM (EST)\n\n" +
            "Headquarters:\n" +
            "123 Innovation Drive\n" +
            "Silicon Valley, CA 90210\n" +
            "United States"
        );
        text.setWrapText(true);
        text.getStyleClass().add("page-text");

        content.getChildren().addAll(title, text);
        root.getChildren().add(content);
        return root;
    }
}
