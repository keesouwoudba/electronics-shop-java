package com.university.shopping.view.pages;

import com.university.shopping.model.User;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Admin page for managing users.
 * Currently displays a placeholder until the UserRepository exposes getAllUsers().
 */
public class AdminUsersPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = StyleHelper.createPageContainer();
        
        // Page title
        root.getChildren().add(StyleHelper.createPageTitle("User Management"));
        
        // Empty state placeholder (until API is connected)
        VBox emptyState = StyleHelper.createEmptyStatePanel(
            "User List Coming Soon",
            "User management interface will be available once the UserRepository exposes getAllUsers() via AdminService."
        );
        root.getChildren().add(emptyState);
        
        // TODO: Connect to AdminService.getAllUsers() when API is ready
        // User[] users = context.getAdminService().getAllUsers();
        // if (users != null && users.length > 0) {
        //     // Render user list table/cards here
        // }

        return root;
    }
}
