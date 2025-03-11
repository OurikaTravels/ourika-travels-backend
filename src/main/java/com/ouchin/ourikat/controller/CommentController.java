package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.CommentRequestDto;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<?> addComment(@PathVariable Long postId,
                                        @RequestBody CommentRequestDto requestDto,
                                        Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        CommentResponseDto response = commentService.addComment(postId, user.getId(), requestDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getPostComments(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCommentCount(@PathVariable Long postId) {
        int count = commentService.getPostCommentCount(postId);
        return ResponseEntity.ok(Map.of("commentCount", count));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        CommentResponseDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequestDto requestDto,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        CommentResponseDto responseDto = commentService.updateComment(commentId, currentUser.getId(), requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.ok(new ApiResponse<>("Comment deleted successfully", true));
    }
}