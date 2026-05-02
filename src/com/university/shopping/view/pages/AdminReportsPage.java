package com.university.shopping.view.pages;

import com.university.shopping.app.Route;
import com.university.shopping.view.components.AdminNavComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Admin page for generating and viewing system reports.
 * Provides access to sales, inventory, and user activity reports via AdminService.
 */
public class AdminReportsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        
        // Admin navigation tabs
        root.getChildren().add(AdminNavComponent.render(context, Route.ADMIN_REPORTS));
        
        VBox content = StyleHelper.createPageContainer();
        
        // Page title
        content.getChildren().add(StyleHelper.createPageTitle("System Reports"));
        
        // Report options card
        VBox reportCard = StyleHelper.createCard();

        Label formatLabel = new Label("Export format");
        ComboBox<String> formatChoice = new ComboBox<>();
        formatChoice.getItems().addAll("console", "csv");
        formatChoice.setValue("csv");

        Button exportBtn = StyleHelper.createPrimaryButton("Export Report");
        exportBtn.setOnAction(e -> {
            String format = formatChoice.getValue();
            String result = context.getAdminService().exportSystemReport(format);
            context.getRenderer().setNotification("Report export: " + result, "SUCCESS".equals(result) ? false : true);
        });

        HBox exportRow = new HBox(10, formatLabel, formatChoice, exportBtn);
        exportRow.setAlignment(Pos.CENTER_LEFT);
        reportCard.getChildren().add(exportRow);
        
        content.getChildren().add(reportCard);
        root.getChildren().add(content);
        return root;
    }
}
