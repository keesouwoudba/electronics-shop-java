package com.university.shopping.view.pages;

import com.university.shopping.app.NavIntent;
import com.university.shopping.app.Route;
import com.university.shopping.model.Product;
import com.university.shopping.view.contracts.ScreenComponent;
import com.university.shopping.view.contracts.ScreenContext;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import com.university.shopping.view.util.ImageHelper;

public class AdminProductEditorPage implements ScreenComponent {
    @Override
    public Node render(ScreenContext context) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));

        Integer pid = context.getAppState().getSelectedProductId();
        boolean isEdit = pid != null;

        Label title = new Label(isEdit ? "Edit Product" : "Add New Product");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #005bbf; -fx-cursor: hand;");
        backBtn.setOnAction(e -> context.getRouter().dispatch(NavIntent.open(Route.ADMIN_PRODUCTS)));

        VBox form = new VBox(15);
        form.setMaxWidth(500);

        TextField nameFld = new TextField();
        nameFld.setPromptText("Product Name");
        
        TextArea descFld = new TextArea();
        descFld.setPromptText("Description");
        descFld.setPrefRowCount(4);

        TextField priceFld = new TextField();
        priceFld.setPromptText("Price");

        TextField stockFld = new TextField();
        stockFld.setPromptText("Stock Quantity");

        // Image controls
        HBox imageRow = new HBox(10);
        imageRow.setPadding(new Insets(6,0,6,0));
        Node imagePreview = new Label("No Image");
        Button uploadBtn = new Button("Upload Image");
        Button removeBtn = new Button("Remove Image");

        final String[] stagedImageName = new String[]{""};
        final String[] stagedImagePath = new String[]{""};

        uploadBtn.setOnAction(ev -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Product Image");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
            );
            File selected = chooser.showOpenDialog(null);
            if (selected != null) {
                // Use product image service if available
                if (context.getProductImageService() != null) {
                    String savedPath = context.getProductImageService().saveImage(selected, selected.getName());
                    if (savedPath != null) {
                        stagedImageName[0] = selected.getName();
                        stagedImagePath[0] = savedPath;
                        imagePreview = ImageHelper.createProductImageView(new com.university.shopping.model.Product(0, "", 0.0, "", "", 0, false, 0.0, stagedImageName[0], stagedImagePath[0]), 120, 80);
                    }
                } else {
                    // Fallback: don't copy, just preview
                    stagedImageName[0] = selected.getName();
                    stagedImagePath[0] = selected.getAbsolutePath();
                    imagePreview = ImageHelper.createProductImageView(new com.university.shopping.model.Product(0, "", 0.0, "", "", 0, false, 0.0, stagedImageName[0], stagedImagePath[0]), 120, 80);
                }
                imageRow.getChildren().clear();
                imageRow.getChildren().addAll(imagePreview, uploadBtn, removeBtn);
            }
        });

        removeBtn.setOnAction(ev -> {
            // remove staged image and attempt delete via service
            if (stagedImagePath[0] != null && !stagedImagePath[0].isEmpty() && context.getProductImageService() != null) {
                context.getProductImageService().deleteImage(stagedImagePath[0]);
            }
            stagedImageName[0] = "";
            stagedImagePath[0] = "";
            imagePreview = new Label("No Image");
            imageRow.getChildren().clear();
            imageRow.getChildren().addAll(imagePreview, uploadBtn, removeBtn);
        });

        imageRow.getChildren().addAll(imagePreview, uploadBtn, removeBtn);

        if (isEdit) {
            Product p = context.getShopService().getProductById(pid);
            if (p != null) {
                nameFld.setText(p.getName());
                descFld.setText(p.getDescription());
                priceFld.setText(String.valueOf(p.getPrice()));
                stockFld.setText(String.valueOf(p.getStockQuantity()));
                if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                    stagedImageName[0] = p.getImageName();
                    stagedImagePath[0] = p.getImagePath();
                    imagePreview = ImageHelper.createProductImageView(p, 120, 80);
                    imageRow.getChildren().clear();
                    imageRow.getChildren().addAll(imagePreview, uploadBtn, removeBtn);
                }
            }
        }

        Button saveBtn = new Button("Save Product");
        saveBtn.setStyle("-fx-background-color: #005bbf; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameFld.getText();
                String desc = descFld.getText();
                double price = Double.parseDouble(priceFld.getText());
                int stock = Integer.parseInt(stockFld.getText());
                
                if (isEdit) {
                    Product p = context.getShopService().getProductById(pid);
                    if (p != null) {
                        p.setName(name);
                        p.setDescription(desc);
                        p.setPrice(price);
                        p.setStockQuantity(stock);
                        if (stagedImageName[0] != null && !stagedImageName[0].isEmpty()) {
                            p.setImageName(stagedImageName[0]);
                            p.setImagePath(stagedImagePath[0]);
                        }
                        String res = context.getAdminService().updateProduct(p);
                        context.getRenderer().setNotification("Update: " + res, "SUCCESS".equals(res) ? false : true);
                    }
                } else {
                    // create new product
                    Product newProd = new Product(name, price, "general", desc, stock, false, 0.0, stagedImageName[0], stagedImagePath[0]);
                    String res = context.getAdminService().addNewProduct(newProd);
                    context.getRenderer().setNotification("Create: " + res, "SUCCESS".equals(res) ? false : true);
                }
                context.getRouter().dispatch(NavIntent.open(Route.ADMIN_PRODUCTS));
            } catch (Exception ex) {
                context.getRenderer().setNotification("Invalid input fields! Check numbers.", true);
            }
        });

        form.getChildren().addAll(
            new Label("Name:"), nameFld,
            new Label("Description:"), descFld,
            new Label("Price:"), priceFld,
            new Label("Stock:"), stockFld,
            saveBtn
        );

        root.getChildren().addAll(backBtn, title, form);
        return root;
    }
}
