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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrekServiceImpl implements TrekService {

    private final TrekRepository trekRepository;
    private final TrekMapper trekMapper;
    private final HighlightRepository highlightRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public TrekServiceImpl(TrekRepository trekRepository, TrekMapper trekMapper, HighlightRepository highlightRepository, CategoryRepository categoryRepository, ServiceEntityRepository serviceEntityRepository) {
        this.trekRepository = trekRepository;
        this.trekMapper = trekMapper;
        this.highlightRepository = highlightRepository;
        this.categoryRepository = categoryRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    @Override
    public TrekResponse createTrek(TrekRequest trekRequest) {
        if (trekRequest.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required.");
        }

        Category category = categoryRepository.findById(trekRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + trekRequest.getCategoryId()));

        Trek trek = trekMapper.toEntity(trekRequest);
        trek.setCategory(category);

        trek = trekRepository.save(trek);

        return trekMapper.toResponse(trek);
    }

    @Override
    public TrekResponse getTrekById(Long id) {
        Trek trek = trekRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trek not found"));
        return trekMapper.toResponse(trek);
    }

    @Override
    public List<TrekResponse> getAllTreks() {
        return trekRepository.findAll().stream()
                .map(trekMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TrekResponse updateTrek(Long id, TrekRequest trekRequest) {

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


        trek = trekRepository.save(trek);


        return trekMapper.toResponse(trek);
    }

    @Override
    public void deleteTrek(Long id) {
        trekRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TrekResponse addHighlightToTrek(Long trekId, Long highlightId) {
        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + highlightId));

        trek.addHighlight(highlight);
        trekRepository.save(trek);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse removeHighlightFromTrek(Long trekId, Long highlightId) {
        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + highlightId));

        trek.removeHighlight(highlight);
        trekRepository.save(trek);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse addServiceToTrek(Long trekId, Long serviceId) {
        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        ServiceEntity service = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        trek.addService(service);
        trekRepository.save(trek);

        return trekMapper.toResponse(trek);
    }

    @Override
    @Transactional
    public TrekResponse removeServiceFromTrek(Long trekId, Long serviceId) {
        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        ServiceEntity service = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        trek.removeService(service);
        trekRepository.save(trek);

        return trekMapper.toResponse(trek);
    }

    @Override
    public List<TrekResponse> getByCategoryId(Long categoryId) {
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