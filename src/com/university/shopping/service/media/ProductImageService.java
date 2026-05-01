package com.university.shopping.service.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
            // generate thumbnail (best-effort)
            try {
                generateThumbnail(target.toFile(), 400, 400);
            } catch (Exception ignored) {}
            return target.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean deleteImage(String pathStr) {
        if (pathStr == null || pathStr.isEmpty()) return false;
        try {
            Path p = Paths.get(pathStr);
            boolean removed = Files.deleteIfExists(p);

            // Also remove any generated thumbnails that reference this file name.
            String baseName = p.getFileName().toString();
            try (java.nio.file.DirectoryStream<Path> ds = Files.newDirectoryStream(imagesDir)) {
                for (Path child : ds) {
                    String fn = child.getFileName().toString();
                    if (fn.startsWith("thumb_") && fn.endsWith(baseName)) {
                        try { Files.deleteIfExists(child); } catch (Exception ignored) {}
                    }
                }
            } catch (Exception ignored) {}

            return removed;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate a thumbnail for the given image file and return its path, or null on failure.
     */
    public String generateThumbnail(File sourceFile, int maxWidth, int maxHeight) {
        if (sourceFile == null || !sourceFile.exists()) return null;
        try {
            BufferedImage img = ImageIO.read(sourceFile);
            if (img == null) return null;

            int origW = img.getWidth();
            int origH = img.getHeight();
            double scale = Math.min((double) maxWidth / origW, (double) maxHeight / origH);
            if (scale > 1.0) scale = 1.0; // don't upscale

            int newW = Math.max(1, (int) Math.round(origW * scale));
            int newH = Math.max(1, (int) Math.round(origH * scale));

            BufferedImage thumb = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = thumb.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(img, 0, 0, newW, newH, null);
            g2.dispose();

            String name = sourceFile.getName();
            String thumbName = "thumb_" + System.currentTimeMillis() + "_" + name;
            Path thumbPath = imagesDir.resolve(thumbName);
            String ext = "png";
            int dot = name.lastIndexOf('.');
            if (dot > 0 && dot < name.length() - 1) {
                ext = name.substring(dot + 1).toLowerCase();
                if (!ImageIO.getImageWritersByFormatName(ext).hasNext()) ext = "png";
            }

            ImageIO.write(thumb, ext, thumbPath.toFile());
            return thumbPath.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
