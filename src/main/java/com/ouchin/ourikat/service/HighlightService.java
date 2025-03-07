package com.ouchin.ourikat.service;
import com.ouchin.ourikat.dto.request.HighlightRequest;
import com.ouchin.ourikat.dto.response.HighlightResponse;
import com.ouchin.ourikat.dto.response.TrekResponse;

import java.util.List;

public interface HighlightService {
    HighlightResponse createHighlight(HighlightRequest highlightRequest);
    HighlightResponse getHighlightById(Long id);
    List<HighlightResponse> getAllHighlights();
    HighlightResponse updateHighlight(Long id, HighlightRequest highlightRequest);
    void deleteHighlight(Long id);

}