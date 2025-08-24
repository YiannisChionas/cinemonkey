package com.hua.demo.movie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UploadPostersController {

    private final UploadPostersService uploadPostersService;

    @PostMapping("/api/movies/uploadPoster")
    public ResponseEntity<String> uploadPoster(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename) { // Accept filename from the user

        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            // Use the service to handle the upload with the custom filename
            String posterUrl = uploadPostersService.uploadPoster(file, filename);
            return new ResponseEntity<>(posterUrl, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            // Return a bad request status with the validation error message
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            // Handle file system errors and log for debugging
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            // Catch any other unexpected exceptions
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}