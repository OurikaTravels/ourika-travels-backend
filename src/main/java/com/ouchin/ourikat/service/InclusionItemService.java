package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.InclusionItemRequest;
import com.ouchin.ourikat.dto.response.InclusionItemResponse;

import java.util.List;

public interface InclusionItemService {
    InclusionItemResponse createInclusionItem(InclusionItemRequest inclusionItemRequest);
    InclusionItemResponse getInclusionItemById(Long id);
    List<InclusionItemResponse> getAllInclusionItems();
    InclusionItemResponse updateInclusionItem(Long id, InclusionItemRequest inclusionItemRequest);
    void deleteInclusionItem(Long id);
}