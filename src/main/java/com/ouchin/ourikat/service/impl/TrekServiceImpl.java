package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.dto.response.TrekSearchResponse;
import com.ouchin.ourikat.entity.*;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.TrekMapper;
import com.ouchin.ourikat.repository.CategoryRepository;
import com.ouchin.ourikat.repository.HighlightRepository;
import com.ouchin.ourikat.repository.ServiceEntityRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.service.TrekService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrekServiceImpl implements TrekService {

    private final TrekRepository trekRepository;
    private final TrekMapper trekMapper;
    private final HighlightRepository highlightRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    @Override
    @Transactional
    public TrekResponse createTrek(TrekRequest trekRequest) {
        log.debug("Creating trek with title: {}", trekRequest.getTitle());

        if (trekRequest.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }

        Category category = categoryRepository.findById(trekRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + trekRequest.getCategoryId()));

        Trek trek = trekMapper.toEntity(trekRequest);
        trek.setCategory(category);

        Trek savedTrek = trekRepository.save(trek);
        log.info("Trek created successfully with ID: {}", savedTrek.getId());

        return trekMapper.toResponse(savedTrek);
    }

    @Override
    public TrekResponse getTrekById(Long id) {
        log.debug("Fetching trek with ID: {}", id);

        Trek trek = trekRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + id));

        return trekMapper.toResponse(trek);
    }

    @Override
    public List<TrekResponse> getAllTreks() {
        log.debug("Fetching all treks");

        return trekRepository.findAll().stream()
                .map(trekMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrekResponse updateTrek(Long id, TrekRequest trekRequest) {
        log.debug("Updating trek with ID: {}", id);

        Trek trek = trekRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + id));

        if (trekRequest.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }

        Category category = categoryRepository.findById(trekRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + trekRequest.getCategoryId()));

        trek.setTitle(trekRequest.getTitle());
        trek.setDescription(trekRequest.getDescription());
        trek.setDuration(trekRequest.getDuration());
        trek.setStartLocation(trekRequest.getStartLocation());
        trek.setEndLocation(trekRequest.getEndLocation());
        trek.setFullDescription(trekRequest.getFullDescription());
        trek.setPrice(trekRequest.getPrice());
        trek.setCategory(category);

        Trek updatedTrek = trekRepository.save(trek);
        log.info("Trek updated successfully with ID: {}", updatedTrek.getId());

        return trekMapper.toResponse(updatedTrek);
    }

    @Override
    @Transactional
    public void deleteTrek(Long id) {
        log.debug("Deleting trek with ID: {}", id);

        if (!trekRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trek not found with id: " + id);
        }

        trekRepository.deleteById(id);
        log.info("Trek deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public TrekResponse addHighlightToTrek(Long trekId, Long highlightId) {
        log.debug("Adding highlight with ID: {} to trek with ID: {}", highlightId, trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + highlightId));

        trek.addHighlight(highlight);
        trekRepository.save(trek);
        log.info("Highlight added successfully to trek with ID: {}", trekId);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse removeHighlightFromTrek(Long trekId, Long highlightId) {
        log.debug("Removing highlight with ID: {} from trek with ID: {}", highlightId, trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + highlightId));

        trek.removeHighlight(highlight);
        trekRepository.save(trek);
        log.info("Highlight removed successfully from trek with ID: {}", trekId);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse addServiceToTrek(Long trekId, Long serviceId) {
        log.debug("Adding service with ID: {} to trek with ID: {}", serviceId, trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        ServiceEntity service = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        trek.addService(service);
        trekRepository.save(trek);
        log.info("Service added successfully to trek with ID: {}", trekId);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse removeServiceFromTrek(Long trekId, Long serviceId) {
        log.debug("Removing service with ID: {} from trek with ID: {}", serviceId, trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        ServiceEntity service = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        trek.removeService(service);
        trekRepository.save(trek);
        log.info("Service removed successfully from trek with ID: {}", trekId);

        return trekMapper.toResponse(trek);
    }

    @Override
    public List<TrekResponse> getByCategoryId(Long categoryId) {
        log.debug("Fetching treks for category with ID: {}", categoryId);

        List<Trek> treks = trekRepository.findByCategoryId(categoryId);

        if (treks.isEmpty()) {
            throw new ResourceNotFoundException("No treks found for category ID: " + categoryId);
        }

        return treks.stream()
                .map(trekMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrekSearchResponse> searchTreksByTitle(String title) {
        log.debug("Searching treks by title: {}", title);

        List<Trek> treks = trekRepository.findByTitleContainingIgnoreCase(title);

        if (treks.isEmpty()) {
            throw new ResourceNotFoundException("No treks found with title containing: " + title);
        }

        return treks.stream()
                .map(trek -> {
                    String primaryImageUrl = trek.getImages().stream()
                            .filter(TrekImage::isPrimary)
                            .findFirst()
                            .map(image -> "http://localhost:8080/api/images/" + image.getPath())
                            .orElse(null);

                    return new TrekSearchResponse(
                            trek.getId(),
                            trek.getTitle(),
                            trek.getDescription(),
                            primaryImageUrl
                    );
                })
                .collect(Collectors.toList());
    }
}