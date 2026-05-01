package com.university.shopping.service.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProductImageService {
    private final Path imagesDir;
    private final String[] allowedExtensions = new String[]{"png","jpg","jpeg","webp"};
    private final long maxBytes = 5L * 1024L * 1024L; // 5 MB

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
        // basic size check
        if (sourceFile.length() > maxBytes) return null;

        // extension check (case-insensitive)
        String name = sourceFile.getName();
        int dot = name.lastIndexOf('.');
        if (dot <= 0 || dot == name.length() - 1) return null;
        String ext = name.substring(dot + 1).toLowerCase();
        boolean allowed = false;
        for (int i = 0; i < allowedExtensions.length; i++) {
            if (allowedExtensions[i].equals(ext)) { allowed = true; break; }
        }
        if (!allowed) return null;
        // sanitize desired name hint or fallback to original filename
        String baseName = (desiredNameHint != null && !desiredNameHint.isEmpty()) ? desiredNameHint : sourceFile.getName();
        baseName = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (!baseName.toLowerCase().endsWith("." + ext)) {
            baseName = baseName + "." + ext;
        }
        String safeName = System.currentTimeMillis() + "_" + baseName;

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
