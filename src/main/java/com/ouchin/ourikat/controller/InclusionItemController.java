package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.InclusionItemRequest;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.InclusionItemResponse;
import com.ouchin.ourikat.service.InclusionItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inclusion-items")
public class InclusionItemController {

    private final InclusionItemService inclusionItemService;

    public InclusionItemController(InclusionItemService inclusionItemService) {
        this.inclusionItemService = inclusionItemService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InclusionItemResponse>> createInclusionItem(@Valid @RequestBody InclusionItemRequest inclusionItemRequest) {
        InclusionItemResponse responseDto = inclusionItemService.createInclusionItem(inclusionItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "InclusionItem created successfully", responseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InclusionItemResponse>> getInclusionItemById(@PathVariable Long id) {
        InclusionItemResponse inclusionItemResponse = inclusionItemService.getInclusionItemById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "InclusionItem retrieved successfully", inclusionItemResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InclusionItemResponse>>> getAllInclusionItems() {
        List<InclusionItemResponse> inclusionItemResponses = inclusionItemService.getAllInclusionItems();
        return ResponseEntity.ok(new ApiResponse<>(true, "InclusionItems retrieved successfully", inclusionItemResponses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InclusionItemResponse>> updateInclusionItem(
            @PathVariable Long id,
            @Valid @RequestBody InclusionItemRequest inclusionItemRequest) {
        InclusionItemResponse inclusionItemResponse = inclusionItemService.updateInclusionItem(id, inclusionItemRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "InclusionItem updated successfully", inclusionItemResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInclusionItem(@PathVariable Long id) {
        inclusionItemService.deleteInclusionItem(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "InclusionItem deleted successfully", null));
    }
}