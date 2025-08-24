package com.hua.demo.movie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadPostersService {

    @Value("${upload.postersDir}")
    private String postersDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final String[] ALLOWED_TYPES = {"image/png", "image/jpeg", "image/webp"};

    public String uploadPoster(MultipartFile file, String customFilename) throws IOException {
        validateFile(file);

        // Sanitize the custom filename
        String sanitizedFilename = sanitizeFilename(customFilename);

        // Get the correct file extension based on the MIME type
        String fileExtension = getFileExtension(file.getContentType());
        String fullFilename = sanitizedFilename + fileExtension;

        // Resolve the path to the static resource directory
        Path directoryPath = Paths.get(postersDir).toAbsolutePath();
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath); // Ensure the directory exists
        }

        // Save the file in the specified directory
        Path filePath = directoryPath.resolve(fullFilename);
        file.transferTo(filePath.toFile());

        // Return the relative path that can be used by the front end
        return "/posters/" + fullFilename;
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size of 5MB.");
        }

        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String type : ALLOWED_TYPES) {
            if (type.equalsIgnoreCase(contentType)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new IllegalArgumentException("Invalid file type. Only PNG, JPEG, and WebP are allowed.");
        }
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    private String getFileExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Unsupported file type: " + contentType);
        };
    }
}
