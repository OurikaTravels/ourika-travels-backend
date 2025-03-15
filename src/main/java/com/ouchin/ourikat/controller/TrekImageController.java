package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.TrekImageResponse;
import com.ouchin.ourikat.dto.request.UpdatePrimaryRequest;
import com.ouchin.ourikat.service.FileService;
import com.ouchin.ourikat.service.TrekImageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/treks/{trekId}/images")
@RequiredArgsConstructor
public class TrekImageController {

    private final TrekImageService trekImageService;
    private final FileService fileService;
    private static final Logger logger = LoggerFactory.getLogger(TrekImageController.class);
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrekImageResponse> addImage(
            @PathVariable Long trekId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TrekImageResponse response = trekImageService.addImage(trekId, file, isPrimary);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TrekImageResponse>> getImagesByTrekId(@PathVariable Long trekId) {
        List<TrekImageResponse> images = trekImageService.getImagesByTrekId(trekId);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long trekId,
            @PathVariable Long imageId) throws BadRequestException {
        trekImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{imageId}/primary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrekImageResponse> togglePrimaryStatus(
            @PathVariable Long trekId,
            @PathVariable Long imageId) {
        TrekImageResponse response = trekImageService.togglePrimaryStatus(imageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        Resource resource = fileService.loadFileAsResource(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }


    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrekImageResponse>> addMultipleImages(
            @PathVariable Long trekId,
            @RequestParam("file") MultipartFile[] files,
            @RequestParam(value = "primaryIndex", defaultValue = "0") Integer primaryIndex) {

        logger.debug("Received request to add multiple images for trekId: {}", trekId);
        logger.debug("Number of files received: {}", files.length);
        logger.debug("Primary index: {}", primaryIndex);

        for (int i = 0; i < files.length; i++) {
            logger.debug("File {}: name={}, size={}", i, files[i].getOriginalFilename(), files[i].getSize());
        }

        if (files.length < 4) {
            logger.error("At least 4 files are required. Received: {}", files.length);
            return ResponseEntity.badRequest().body(null);
        }

        List<TrekImageResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            boolean isPrimary = (i == primaryIndex);
            logger.debug("Processing file {}: isPrimary={}", i, isPrimary);

            if (files[i].isEmpty()) {
                logger.error("File {} is empty", i);
                return ResponseEntity.badRequest().body(null);
            }

            TrekImageResponse response = trekImageService.addImage(trekId, files[i], isPrimary);
            responses.add(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}