package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                        Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        boolean liked = likeService.toggleLike(postId, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(liked ? "Post liked" : "Post unliked", liked));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getLikeCount(@PathVariable Long postId) {
        int count = likeService.getPostLikeCount(postId);
        return ResponseEntity.ok(Map.of("likeCount", count));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getLikeStatus(@PathVariable Long postId,
                                           Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        boolean liked = likeService.isPostLikedByUser(postId, user.getId());

        return ResponseEntity.ok(new ApiResponse<>("Like status retrieved", liked));
    }
}