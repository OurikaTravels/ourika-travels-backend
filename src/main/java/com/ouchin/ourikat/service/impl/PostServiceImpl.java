package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.CommentMapper;
import com.ouchin.ourikat.mapper.PostMapper;
import com.ouchin.ourikat.repository.GuideRepository;
import com.ouchin.ourikat.repository.PostRepository;
import com.ouchin.ourikat.service.CommentService;
import com.ouchin.ourikat.service.FileService;
import com.ouchin.ourikat.service.LikeService;
import com.ouchin.ourikat.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GuideRepository guideRepository;
    private final PostMapper postMapper;
    private final FileService fileService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public PostResponseDto createPost(Long guideId, PostRequestDto requestDto, List<MultipartFile> images) throws IOException {
        log.debug("Creating post for guide with ID: {}", guideId);

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + guideId));

        validateImages(images);

        Post post = postMapper.toEntity(requestDto, guide);
        List<String> imagePaths = saveImages(images);
        post.setImages(imagePaths);

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with ID: {}", savedPost.getId());

        return postMapper.toResponseDto(savedPost);
    }

    private void validateImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least 1 image is required");
        }

        if (images.size() > 3) {
            throw new IllegalArgumentException("Maximum 3 images are allowed");
        }
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        return images.stream()
                .map(image -> {
                    try {
                        return fileService.saveFile(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getPostsByGuideId(Long guideId) {
        log.debug("Fetching posts for guide with ID: {}", guideId);

        guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + guideId));

        List<Post> posts = postRepository.findByGuideIdOrderByCreatedAtDesc(guideId);
        return postMapper.toResponseDtoList(posts, likeService, commentService);
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        log.debug("Fetching post with ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        int likeCount = likeService.getPostLikeCount(postId);
        int commentCount = commentService.getPostCommentCount(postId);
        List<CommentResponseDto> comments = commentService.getPostComments(postId);

        return postMapper.toResponseDto(post, likeCount, commentCount, comments);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateDto) {
        log.debug("Updating post with ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        postMapper.updateEntityFromDto(updateDto, post);
        Post updatedPost = postRepository.save(post);

        log.info("Post updated successfully with ID: {}", updatedPost.getId());
        return postMapper.toResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public PostResponseDto updatePostImages(Long postId, List<MultipartFile> images) throws IOException {
        log.debug("Updating images for post with ID: {}", postId);

        validateImages(images);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        for (String imagePath : post.getImages()) {
            fileService.deleteFile(imagePath);
        }

        List<String> imagePaths = saveImages(images);
        post.setImages(imagePaths);

        Post updatedPost = postRepository.save(post);
        log.info("Post images updated successfully with ID: {}", updatedPost.getId());
        return postMapper.toResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) throws IOException {
        log.debug("Deleting post with ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        for (String imagePath : post.getImages()) {
            fileService.deleteFile(imagePath);
        }

        postRepository.delete(post);
        log.info("Post deleted successfully with ID: {}", postId);
    }

    @Override
    public List<PostResponseDto> getAll() {
        log.debug("Fetching all posts");

        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            int likeCount = likeService.getPostLikeCount(post.getId());
            int commentCount = commentService.getPostCommentCount(post.getId());
            List<CommentResponseDto> comments = commentService.getPostComments(post.getId());

            return postMapper.toResponseDto(post, likeCount, commentCount, comments);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getPostIdsLikedByUser(Long userId) {
        log.debug("Fetching post IDs liked by user with ID: {}", userId);

        return postRepository.findPostIdsByUserId(userId);
    }
}