package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.service.TrekService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treks/{trekId}/highlights")
public class TrekHighlightController {

    private final TrekService trekService;

    public TrekHighlightController(TrekService trekService) {
        this.trekService = trekService;
    }

    @PostMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<TrekResponse>> addHighlightToTrek(
            @PathVariable Long trekId,
            @PathVariable Long highlightId) {
        TrekResponse trekResponse = trekService.addHighlightToTrek(trekId, highlightId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlight added to trek successfully", trekResponse));
    }

    @DeleteMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<TrekResponse>> removeHighlightFromTrek(
            @PathVariable Long trekId,
            @PathVariable Long highlightId) {
        TrekResponse trekResponse = trekService.removeHighlightFromTrek(trekId, highlightId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Highlight removed from trek successfully", trekResponse));
    }
}









