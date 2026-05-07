package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.User;
import com.university.shopping.view.components.AdminNavComponent;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import com.university.shopping.view.util.StyleHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Admin page for managing users.
 */
public class AdminUsersPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox();
        
        // Admin navigation tabs
        root.getChildren().add(AdminNavComponent.render(context, Route.ADMIN_USERS));
        
        VBox content = StyleHelper.createPageContainer();
        
        // Page title
        content.getChildren().add(StyleHelper.createPageTitle("User Management"));

        User selectedUser = null;
        Integer selectedUserId = context.getAppState().getSelectedUserId();
        //expose get user by id to the adminService so that it would call existing funcionality as facade api
        if (selectedUserId != null) {
            User[] users = context.getAdminService().getAllUsers();
            if (users != null) {
                for (User user : users) {
                    if (user != null && user.getUserId() == selectedUserId) {
                        selectedUser = user;
                        break;
                    }
                }
            }
        }

        VBox formCard = StyleHelper.createCard();
        formCard.setPadding(new Insets(18));
        Label formTitle = new Label(selectedUser == null ? "Add User" : "Edit User");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameFld = new TextField();
        usernameFld.setPromptText("Username");
        TextField passwordFld = new TextField();
        passwordFld.setPromptText("Password");
        CheckBox adminChk = new CheckBox("Admin account");

        if (selectedUser != null) {
            usernameFld.setText(selectedUser.getUsername());
            adminChk.setSelected(selectedUser.isAdmin());
        }

        final User activeSelectedUser = selectedUser;
        final Integer activeSelectedUserId = selectedUserId;

        Button saveBtn = StyleHelper.createPrimaryButton(activeSelectedUser == null ? "Create User" : "Save Changes");
        saveBtn.setOnAction(e -> {
            String username = usernameFld.getText();
            String password = passwordFld.getText();
            boolean isAdmin = adminChk.isSelected();

            String result;
            if (activeSelectedUser == null) {
                result = context.getAdminService().addUser(username, password, isAdmin);
            } else {
                result = context.getAdminService().updateUser(activeSelectedUser.getUserId(), username, password, isAdmin);
            }

            context.getRenderer().setNotification((activeSelectedUser == null ? "Create user: " : "Update user: ") + result, "SUCCESS".equals(result) ? false : true);
            if ("SUCCESS".equals(result)) {
                context.getAppState().setSelectedUserId(null);
                context.getRenderer().navigate(NavIntent.open(Route.ADMIN_USERS));
            }
        });

        Button cancelBtn = StyleHelper.createSecondaryButton("Clear");
        cancelBtn.setOnAction(e -> {
            context.getAppState().setSelectedUserId(null);
            context.getRenderer().navigate(NavIntent.open(Route.ADMIN_USERS));
        });

        HBox actionRow = new HBox(10, saveBtn, cancelBtn);
        formCard.getChildren().addAll(formTitle, usernameFld, passwordFld, adminChk, actionRow);
        content.getChildren().add(formCard);

        User[] users = context.getAdminService().getAllUsers();
        if (users == null || users.length == 0) {
            content.getChildren().add(StyleHelper.createEmptyStatePanel(
                "No Users Found",
                "The user repository is currently empty."
            ));
        } else {
            VBox userList = StyleHelper.createCard();
            for (User user : users) {
                if (user == null) {
                    continue;
                }

                HBox row = new HBox(16);
                row.setAlignment(Pos.CENTER_LEFT);

                Label username = new Label(user.getUsername());
                username.setStyle("-fx-font-weight: bold;");

                Label role = new Label(user.isAdmin() ? "Admin" : "Customer");
                role.setStyle("-fx-text-fill: #005bbf;");

                Label date = new Label(user.getCreatedDate());
                date.setStyle("-fx-text-fill: grey;");

                Button editBtn = new Button("Edit");
                editBtn.setOnAction(e -> {
                    context.getAppState().setSelectedUserId(user.getUserId());
                    context.getRenderer().navigate(NavIntent.open(Route.ADMIN_USERS));
                });

                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    String result = context.getAdminService().deleteUser(user.getUserId());
                    context.getRenderer().setNotification("Delete user: " + result, "SUCCESS".equals(result) ? false : true);
                    if ("SUCCESS".equals(result)) {
                        if (activeSelectedUserId != null && activeSelectedUserId.equals(user.getUserId())) {
                            context.getAppState().setSelectedUserId(null);
                        }
                        context.getRenderer().navigate(NavIntent.open(Route.ADMIN_USERS));
                    }
                });

                row.getChildren().addAll(username, role, date, editBtn, deleteBtn);
                userList.getChildren().add(row);
            }

            content.getChildren().add(userList);
        }

        root.getChildren().add(content);
        return root;
    }
}
