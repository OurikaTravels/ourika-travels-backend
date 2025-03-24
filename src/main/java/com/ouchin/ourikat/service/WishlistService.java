package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.WishlistRequestDto;
import com.ouchin.ourikat.dto.response.WishlistResponseDto;

import java.util.List;

public interface WishlistService {
    WishlistResponseDto addToWishlist(Long touristId, WishlistRequestDto request);
    void removeFromWishlist(Long touristId, Long trekId);
    List<WishlistResponseDto> getWishlistByTouristId(Long touristId);

    int getWishlistCountByTouristId(Long touristId);
    List<Long> getTrekIdsInWishlistByTouristId(Long touristId);
}