package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.HighlightRequest;
import com.ouchin.ourikat.dto.response.HighlightResponse;
import com.ouchin.ourikat.entity.Highlight;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.HighlightMapper;
import com.ouchin.ourikat.repository.HighlightRepository;
import com.ouchin.ourikat.service.HighlightService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HighlightServiceImpl implements HighlightService {

    private final HighlightRepository highlightRepository;
    private final HighlightMapper highlightMapper;

    public HighlightServiceImpl(HighlightRepository highlightRepository, HighlightMapper highlightMapper) {
        this.highlightRepository = highlightRepository;
        this.highlightMapper = highlightMapper;
    }

    @Override
    public HighlightResponse createHighlight(HighlightRequest highlightRequest) {
        Highlight highlight = highlightMapper.toEntity(highlightRequest);
        highlight = highlightRepository.save(highlight);
        return highlightMapper.toResponse(highlight);
    }

    @Override
    public HighlightResponse getHighlightById(Long id) {
        Highlight highlight = highlightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + id));
        return highlightMapper.toResponse(highlight);
    }

    @Override
    public List<HighlightResponse> getAllHighlights() {
        return highlightRepository.findAll().stream()
                .map(highlightMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HighlightResponse updateHighlight(Long id, HighlightRequest highlightRequest) {
        Highlight highlight = highlightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + id));

        highlight.setContent(highlightRequest.getContent());
        highlight = highlightRepository.save(highlight);
        return highlightMapper.toResponse(highlight);
    }

    @Override
    public void deleteHighlight(Long id) {
        Highlight highlight = highlightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Highlight not found with id: " + id));

        if (!highlight.getTreks().isEmpty()) {
            throw new IllegalStateException("Cannot delete highlight: It is associated with one or more treks.");
        }

        highlightRepository.deleteById(id);
    }
}