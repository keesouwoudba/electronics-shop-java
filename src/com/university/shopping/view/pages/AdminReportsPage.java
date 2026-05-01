package com.university.shopping.view.pages;

import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminReportsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));

        Label title = new Label("System Reports");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox cards = new VBox(15);
        cards.setStyle("-fx-border-color: #e0e2ec; -fx-border-radius: 8px; -fx-padding: 20px; -fx-background-color: white;");

        Button genSalesBtn = new Button("Generate Sales Report");
        genSalesBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold;");
        genSalesBtn.setOnAction(e -> {
            context.getRenderer().setNotification("Sales report has been exported to CSV.", false);
            // Will hook up to ReportService
        });

        cards.getChildren().addAll(new Label("Click a report type to export it currently as CSV."), genSalesBtn);

        root.getChildren().addAll(title, cards);
        return root;
    }
}
