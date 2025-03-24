package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.HighlightRequest;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.HighlightResponse;
import com.ouchin.ourikat.service.HighlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/highlights")
public class HighlightController {

    private final HighlightService highlightService;

    public HighlightController(HighlightService highlightService) {
        this.highlightService = highlightService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HighlightResponse>> createHighlight(@Valid @RequestBody HighlightRequest highlightRequest) {
        HighlightResponse responseDto = highlightService.createHighlight(highlightRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Highlight created successfully", responseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HighlightResponse>> getHighlightById(@PathVariable Long id) {
        HighlightResponse highlightResponse = highlightService.getHighlightById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlight retrieved successfully", highlightResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HighlightResponse>>> getAllHighlights() {
        List<HighlightResponse> highlightResponses = highlightService.getAllHighlights();
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlights retrieved successfully", highlightResponses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HighlightResponse>> updateHighlight(
            @PathVariable Long id,
            @Valid @RequestBody HighlightRequest highlightRequest) {
        HighlightResponse highlightResponse = highlightService.updateHighlight(id, highlightRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlight updated successfully", highlightResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteHighlight(@PathVariable Long id) {
        highlightService.deleteHighlight(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlight deleted successfully", null));
    }
}