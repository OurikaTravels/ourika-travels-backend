package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.InclusionItemRequest;
import com.ouchin.ourikat.dto.response.InclusionItemResponse;
import com.ouchin.ourikat.entity.InclusionItem;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.InclusionItemMapper;
import com.ouchin.ourikat.repository.InclusionItemRepository;
import com.ouchin.ourikat.service.InclusionItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InclusionItemServiceImpl implements InclusionItemService {

    private final InclusionItemRepository inclusionItemRepository;
    private final InclusionItemMapper inclusionItemMapper;

    public InclusionItemServiceImpl(InclusionItemRepository inclusionItemRepository, InclusionItemMapper inclusionItemMapper) {
        this.inclusionItemRepository = inclusionItemRepository;
        this.inclusionItemMapper = inclusionItemMapper;
    }

    @Override
    public InclusionItemResponse createInclusionItem(InclusionItemRequest inclusionItemRequest) {
        InclusionItem inclusionItem = inclusionItemMapper.toEntity(inclusionItemRequest);
        inclusionItem = inclusionItemRepository.save(inclusionItem);
        return inclusionItemMapper.toResponse(inclusionItem);
    }

    @Override
    public InclusionItemResponse getInclusionItemById(Long id) {
        InclusionItem inclusionItem = inclusionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InclusionItem not found with id: " + id));
        return inclusionItemMapper.toResponse(inclusionItem);
    }

    @Override
    public List<InclusionItemResponse> getAllInclusionItems() {
        return inclusionItemRepository.findAll().stream()
                .map(inclusionItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InclusionItemResponse updateInclusionItem(Long id, InclusionItemRequest inclusionItemRequest) {
        InclusionItem inclusionItem = inclusionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InclusionItem not found with id: " + id));
        inclusionItem.setContent(inclusionItemRequest.getContent());
        inclusionItem = inclusionItemRepository.save(inclusionItem);
        return inclusionItemMapper.toResponse(inclusionItem);
    }

    @Override
    public void deleteInclusionItem(Long id) {
        if (!inclusionItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("InclusionItem not found with id: " + id);
        }
        inclusionItemRepository.deleteById(id);
    }
}