package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.CommentRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long postId, Long userId, CommentRequestDto requestDto);
    List<CommentResponseDto> getPostComments(Long postId);
    CommentResponseDto getCommentById(Long commentId);
    CommentResponseDto updateComment(Long commentId, Long userId, CommentRequestDto requestDto);
    void deleteComment(Long commentId, Long userId);
    int getPostCommentCount(Long postId);
}
