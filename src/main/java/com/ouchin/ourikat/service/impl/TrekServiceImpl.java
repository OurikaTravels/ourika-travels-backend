package com.ouchin.ourikat.service.impl;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.exception.DuplicateTitleException;
import com.ouchin.ourikat.mapper.TrekMapper;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.service.TrekService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrekServiceImpl implements TrekService {

    private final TrekRepository trekRepository;
    private final TrekMapper trekMapper;

    public TrekServiceImpl(TrekRepository trekRepository, TrekMapper trekMapper) {
        this.trekRepository = trekRepository;
        this.trekMapper = trekMapper;
    }

    @Override
    public TrekResponse createTrek(TrekRequest trekRequest) {

        if (trekRepository.findByTitle(trekRequest.getTitle()).isPresent()) {
            throw new DuplicateTitleException("A trek with title '" + trekRequest.getTitle() + "' already exists.");
        }

        Trek trek = trekMapper.toEntity(trekRequest);

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
                .orElseThrow(() -> new RuntimeException("Trek not found"));


        if (trekRepository.existsByTitleAndIdNot(trekRequest.getTitle(), id)) {
            throw new DuplicateTitleException("A trek with title '" + trekRequest.getTitle() + "' already exists.");
        }
        trek.setTitle(trekRequest.getTitle());
        trek.setDescription(trekRequest.getDescription());
        trek.setDuration(trekRequest.getDuration());
        trek.setStartLocation(trekRequest.getStartLocation());
        trek.setEndLocation(trekRequest.getEndLocation());
        trek.setFullDescription(trekRequest.getFullDescription());
        trek.setPrice(trekRequest.getPrice());
        trek = trekRepository.save(trek);
        return trekMapper.toResponse(trek);
    }

    @Override
    public void deleteTrek(Long id) {
        trekRepository.deleteById(id);
    }
}