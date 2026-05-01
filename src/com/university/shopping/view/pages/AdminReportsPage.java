package com.university.shopping.view.pages;

import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Admin page for generating and viewing system reports.
 * Provides access to sales, inventory, and user activity reports via AdminService.
 */
public class AdminReportsPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = StyleHelper.createPageContainer();
        
        // Page title
        root.getChildren().add(StyleHelper.createPageTitle("System Reports"));
        
        // Report options card
        VBox reportCard = StyleHelper.createCard();
        
        // Sales report button
        Button genSalesBtn = StyleHelper.createPrimaryButton("Generate Sales Report");
        genSalesBtn.setOnAction(e -> {
            context.getRenderer().setNotification("Sales report has been exported to CSV.", false);
            // Will hook up to ReportService to generate report with actual data
        });
        reportCard.getChildren().add(genSalesBtn);
        
        // Inventory report button
        Button genInventoryBtn = StyleHelper.createPrimaryButton("Generate Inventory Report");
        genInventoryBtn.setOnAction(e -> {
            context.getRenderer().setNotification("Inventory report has been exported to CSV.", false);
            // Will hook up to ReportService
        });
        reportCard.getChildren().add(genInventoryBtn);
        
        // User activity report button
        Button genUserActivityBtn = StyleHelper.createPrimaryButton("Generate User Activity Report");
        genUserActivityBtn.setOnAction(e -> {
            context.getRenderer().setNotification("User activity report has been exported to CSV.", false);
            // Will hook up to ReportService
        });
        reportCard.getChildren().add(genUserActivityBtn);
        
        root.getChildren().add(reportCard);
        
        // TODO: Wire up to AdminService.generateReport() methods when reports are fully implemented
        // String[] formats = context.getAdminService().getSupportedReportFormats();
        // AbstractReportService[] services = context.getAdminService().getReportServices();

        return root;
    }
}
