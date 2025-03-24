package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.WishlistRequestDto;
import com.ouchin.ourikat.dto.response.WishlistResponseDto;
import com.ouchin.ourikat.entity.*;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.TrekMapper;
import com.ouchin.ourikat.mapper.WishlistMapper;
import com.ouchin.ourikat.repository.TouristRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.repository.WishlistRepository;
import com.ouchin.ourikat.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final TouristRepository touristRepository;
    private final TrekRepository trekRepository;
    private final WishlistMapper wishlistMapper;
    private final TrekMapper trekMapper;

    @Override
    @Transactional
    public WishlistResponseDto addToWishlist(Long touristId, WishlistRequestDto request) {
        Tourist tourist = touristRepository.findById(touristId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist not found"));

        Trek trek = trekRepository.findById(request.getTrekId())
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found"));


        if (wishlistRepository.existsByTouristIdAndTrekId(touristId, request.getTrekId())) {
            throw new IllegalArgumentException("Trek is already in the wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setTourist(tourist);
        wishlist.setTrek(trek);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toResponseDto(savedWishlist);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long touristId, Long trekId) {
        wishlistRepository.deleteByTouristIdAndTrekId(touristId, trekId);
    }

    @Override
    public List<WishlistResponseDto> getWishlistByTouristId(Long touristId) {
        List<Wishlist> wishlists = wishlistRepository.findByTouristId(touristId);
        return wishlists.stream()
                .map(wishlistMapper::toResponseDto)
                .toList();
    }

    @Override
    public int getWishlistCountByTouristId(Long touristId) {
        return wishlistRepository.countByTouristId(touristId);
    }

    @Override
    public List<Long> getTrekIdsInWishlistByTouristId(Long touristId) {
        return wishlistRepository.findTrekIdsByTouristId(touristId);
    }
}