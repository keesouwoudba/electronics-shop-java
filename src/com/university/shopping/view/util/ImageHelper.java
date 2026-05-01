package com.university.shopping.view.util;

import com.university.shopping.model.Product;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;

public final class ImageHelper {
    private ImageHelper() {}

    public static Node createProductImageView(Product p, double width, double height) {
        String path = p.getImagePath();
        if (path != null && !path.isEmpty()) {
            try {
                File f = new File(path);
                String uri = f.exists() ? f.toURI().toString() : "file:" + path;
                Image img = new Image(uri, width, height, true, true, false);
                if (!img.isError()) {
                    ImageView iv = new ImageView(img);
                    iv.setFitWidth(width);
                    iv.setFitHeight(height);
                    iv.setPreserveRatio(true);
                    return iv;
                }
            } catch (Exception ignored) {
                // fall through to placeholder
            }
        }

        // Placeholder: simple rectangle with "No Image" label
        Rectangle rect = new Rectangle(width, height, Color.web("#f2f3fd"));
        rect.setStroke(Color.web("#e0e2ec"));
        rect.setArcWidth(12);
        rect.setArcHeight(12);

        Label lbl = new Label("No Image");
        lbl.setStyle("-fx-text-fill: #9aa1b3; -fx-font-size: 14px;");

        StackPane sp = new StackPane(rect, lbl);
        sp.setAlignment(Pos.CENTER);
        return sp;
    }
}
