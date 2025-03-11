package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/guides/{guideId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> createPost(
            @PathVariable Long guideId,
            @RequestPart("post") @Valid PostRequestDto requestDto,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        PostResponseDto responseDto = postService.createPost(guideId, requestDto, images);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByGuideId(@PathVariable Long guideId) {
        List<PostResponseDto> posts = postService.getPostsByGuideId(guideId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateRequestDto updateDto) {

        PostResponseDto responseDto = postService.updatePost(postId, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping(value = "/{postId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> updatePostImages(
            @PathVariable Long postId,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        PostResponseDto responseDto = postService.updatePostImages(postId, images);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) throws IOException {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}