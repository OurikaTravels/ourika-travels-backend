package com.ouchin.ourikat.service;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.dto.response.TrekSearchResponse;

import java.util.List;

public interface TrekService {
    TrekResponse createTrek(TrekRequest trekRequest);
    TrekResponse getTrekById(Long id);
    List<TrekResponse> getAllTreks();
    TrekResponse updateTrek(Long id, TrekRequest trekRequest);
    void deleteTrek(Long id);

    TrekResponse addHighlightToTrek(Long trekId, Long highlightId);
    TrekResponse removeHighlightFromTrek(Long trekId, Long highlightId);


    TrekResponse addServiceToTrek(Long trekId, Long serviceId);
    TrekResponse removeServiceFromTrek(Long trekId, Long serviceId);


    List<TrekResponse> getByCategoryId(Long categoryId);
    List<TrekSearchResponse> searchTreksByTitle(String title);
}