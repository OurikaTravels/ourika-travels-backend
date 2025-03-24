package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostResponseDto createPost(Long guideId, PostRequestDto requestDto, List<MultipartFile> images) throws IOException;

    List<PostResponseDto> getPostsByGuideId(Long guideId);

    PostResponseDto getPostById(Long postId);

    PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateDto);

    PostResponseDto updatePostImages(Long postId, List<MultipartFile> images) throws IOException;

    void deletePost(Long postId) throws IOException;

    List<PostResponseDto> getAll();

    List<Long> getPostIdsLikedByUser(Long userId);
}