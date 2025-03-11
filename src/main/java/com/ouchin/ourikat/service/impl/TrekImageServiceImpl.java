package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.TrekImageResponse;
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
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        try {
            String fileName = fileService.saveFile(file);

            // If this is set as primary, unset any existing primary image
            if (isPrimary) {
                trek.getImages().stream()
                        .filter(TrekImage::getIsPrimary)
                        .forEach(img -> img.setIsPrimary(false));
            }

            // If no images exist yet or no primary is set, make this the primary
            boolean shouldBePrimary = isPrimary || trek.getImages().isEmpty() ||
                    trek.getImages().stream().noneMatch(TrekImage::getIsPrimary);

            TrekImage trekImage = new TrekImage();
            trekImage.setPath(fileName);
            trekImage.setIsPrimary(shouldBePrimary);
            trekImage.setTrek(trek);

            trekImage = trekImageRepository.save(trekImage);
            return trekImageMapper.toResponse(trekImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public List<TrekImageResponse> getImagesByTrekId(Long trekId) {
        List<TrekImage> images = trekImageRepository.findByTrekId(trekId);
        return trekImageMapper.toResponseList(images);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) throws BadRequestException {
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

            System.err.println("Failed to delete file: " + image.getPath());
        }


        trekImageRepository.delete(image);
    }

    @Override
    @Transactional
    public TrekImageResponse setImageAsPrimary(Long imageId) {
        TrekImage image = trekImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        Long trekId = image.getTrek().getId();


        trekImageRepository.findByTrekIdAndIsPrimaryTrue(trekId)
                .forEach(img -> {
                    img.setIsPrimary(false);
                    trekImageRepository.save(img);
                });


        image.setIsPrimary(true);
        image = trekImageRepository.save(image);

        return trekImageMapper.toResponse(image);
    }

    @Override
    public void validateTrekImages(Long trekId) throws BadRequestException {
        long imageCount = trekImageRepository.countByTrekId(trekId);
        if (imageCount < 4) {
            throw new BadRequestException("Trek must have at least 4 images");
        }

        boolean hasPrimary = trekImageRepository.existsByTrekIdAndIsPrimaryTrue(trekId);
        if (!hasPrimary) {
            throw new BadRequestException("Trek must have a primary image");
        }
    }
}
