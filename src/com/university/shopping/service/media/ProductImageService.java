package com.university.shopping.service.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProductImageService {
    private final Path imagesDir;

    public ProductImageService() {
        this.imagesDir = Paths.get("data", "images");
        try {
            Files.createDirectories(imagesDir);
        } catch (IOException ignored) {
            // best-effort
        }
    }

    /**
     * Save an image file into the data/images directory. Returns the saved file path (string) or null on failure.
     */
    public String saveImage(File sourceFile, String desiredNameHint) {
        if (sourceFile == null || !sourceFile.exists()) return null;
        String safeName = System.currentTimeMillis() + "_" + sourceFile.getName();
        if (desiredNameHint != null && !desiredNameHint.isEmpty()) {
            safeName = System.currentTimeMillis() + "_" + desiredNameHint;
        }

        Path target = imagesDir.resolve(safeName);
        try {
            Files.copy(sourceFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean deleteImage(String pathStr) {
        if (pathStr == null || pathStr.isEmpty()) return false;
        try {
            Path p = Paths.get(pathStr);
            return Files.deleteIfExists(p);
        } catch (Exception e) {
            return false;
        }
    }
}
