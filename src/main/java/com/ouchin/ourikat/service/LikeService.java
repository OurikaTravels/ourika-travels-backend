package com.ouchin.ourikat.service;

public interface LikeService {
    boolean toggleLike(Long postId, Long userId);
    int getPostLikeCount(Long postId);
    boolean isPostLikedByUser(Long postId, Long userId);
}