package com.bolashak.instagramclonebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            // Create upload directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();
            String cleanedFileName = originalFileName.replaceAll("\\s+", "_");
            String uniqueFilename = UUID.randomUUID().toString() + "_" + cleanedFileName;

            //logger.info("A url: {}", uniqueFilename);

            Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);
            Files.write(filePath, file.getBytes());

            String fileUrl = "/uploads/" + uniqueFilename;
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
