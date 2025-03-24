package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.response.TrekImageResponse;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.entity.TrekImage;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.TrekImageMapper;
import com.ouchin.ourikat.repository.TrekImageRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.service.FileService;
import com.ouchin.ourikat.service.TrekImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrekImageServiceImpl implements TrekImageService {

    private final TrekRepository trekRepository;
    private final TrekImageRepository trekImageRepository;
    private final TrekImageMapper trekImageMapper;
    private final FileService fileService;

    @Override
    @Transactional
    public TrekImageResponse addImage(Long trekId, MultipartFile file, Boolean isPrimary) {
        log.debug("Adding image to trek with ID: {}", trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        try {
            String fileName = fileService.saveFile(file);

            if (isPrimary) {
                trekImageRepository.unsetAllPrimaryImagesForTrek(trekId);
            }

            boolean shouldBePrimary = isPrimary || trekImageRepository.countByTrekId(trekId) == 0;

            TrekImage trekImage = new TrekImage();
            trekImage.setPath(fileName);
            trekImage.setIsPrimary(shouldBePrimary);
            trekImage.setTrek(trek);

            trekImage = trekImageRepository.save(trekImage);
            log.info("Image added successfully with ID: {}", trekImage.getId());

            return trekImageMapper.toResponse(trekImage);
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public List<TrekImageResponse> getImagesByTrekId(Long trekId) {
        log.debug("Fetching images for trek with ID: {}", trekId);

        List<TrekImage> images = trekImageRepository.findByTrekId(trekId);
        return trekImageMapper.toResponseList(images);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) throws BadRequestException {
        log.debug("Deleting image with ID: {}", imageId);

        TrekImage image = trekImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        Long trekId = image.getTrek().getId();

        long imageCount = trekImageRepository.countByTrekId(trekId);
        if (imageCount <= 4) {
            throw new BadRequestException("Trek must have at least 4 images. Cannot delete.");
        }

        if (image.getIsPrimary()) {
            trekImageRepository.findFirstByTrekIdAndIdNot(trekId, imageId)
                    .ifPresent(newPrimary -> {
                        newPrimary.setIsPrimary(true);
                        trekImageRepository.save(newPrimary);
                    });
        }

        try {
            fileService.deleteFile(image.getPath());
        } catch (IOException e) {
            log.error("Failed to delete file: {}", image.getPath(), e);
        }

        trekImageRepository.delete(image);
        log.info("Image deleted successfully with ID: {}", imageId);
    }

    @Override
    @Transactional
    public TrekImageResponse togglePrimaryStatus(Long imageId) {
        log.debug("Toggling primary status for image with ID: {}", imageId);

        TrekImage image = trekImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        Long trekId = image.getTrek().getId();

        boolean newPrimaryStatus = !image.getIsPrimary();

        if (newPrimaryStatus) {
            trekImageRepository.unsetAllPrimaryImagesForTrek(trekId);
        }

        image.setIsPrimary(newPrimaryStatus);
        image = trekImageRepository.save(image);

        log.info("Primary status toggled successfully for image with ID: {}", imageId);
        return trekImageMapper.toResponse(image);
    }

}