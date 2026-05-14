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
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public final class ImageHelper {
    private ImageHelper() {}

    public static Node createProductImageView(Product p, double width, double height, boolean loadThumbnail) {
        String path = p.getImagePath();
        if (path != null && !path.isEmpty()) {
            try {
                File orig = new File(path);
                File toLoad = loadThumbnail ? findThumbnail(orig) : orig;
                String uri = toLoad.exists() ? toLoad.toURI().toString() : "file:" + path;
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
    
    public static Node createProductImageView(Product p, double width, double height) {
        return createProductImageView(p, width, height, true);
    }

    private static File findThumbnail(File original) {
        if (original == null) return null;
        File parent = original.getParentFile();
        if (parent == null || !parent.exists()) return original;

        String originalName = original.getName();
        try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(parent.toPath())) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                if (fileName.startsWith("thumb_") && fileName.endsWith(originalName)) {
                    return entry.toFile();
                }
            }
        } catch (Exception ignored) {
            // fallback to original
        }
        return original;
    }
}
