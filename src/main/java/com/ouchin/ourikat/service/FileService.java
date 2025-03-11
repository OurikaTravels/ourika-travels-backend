package com.ouchin.ourikat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);

        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }
}