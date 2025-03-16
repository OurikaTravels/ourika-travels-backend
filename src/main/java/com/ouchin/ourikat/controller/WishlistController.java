package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.WishlistRequestDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.WishlistResponseDto;
import com.ouchin.ourikat.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/tourists/{touristId}/add")
    public ResponseEntity<ApiResponse<WishlistResponseDto>> addToWishlist(
            @PathVariable Long touristId,
            @RequestBody WishlistRequestDto request) {
        WishlistResponseDto wishlist = wishlistService.addToWishlist(touristId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Trek added to wishlist successfully", wishlist));
    }

    @DeleteMapping("/tourists/{touristId}/remove/{trekId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @PathVariable Long touristId,
            @PathVariable Long trekId) {
        wishlistService.removeFromWishlist(touristId, trekId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Trek removed from wishlist successfully", null));
    }

    @GetMapping("/tourists/{touristId}")
    public ResponseEntity<ApiResponse<List<WishlistResponseDto>>> getWishlistByTouristId(
            @PathVariable Long touristId) {
        List<WishlistResponseDto> wishlist = wishlistService.getWishlistByTouristId(touristId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Wishlist retrieved successfully", wishlist));
    }
}