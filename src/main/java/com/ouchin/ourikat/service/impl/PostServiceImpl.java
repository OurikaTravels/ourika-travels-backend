package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.CommentMapper;
import com.ouchin.ourikat.mapper.PostMapper;
import com.ouchin.ourikat.repository.GuideRepository;
import com.ouchin.ourikat.repository.PostRepository;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.CommentService;
import com.ouchin.ourikat.service.FileService;
import com.ouchin.ourikat.service.LikeService;
import com.ouchin.ourikat.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostResponseDto createPost(Long guideId, PostRequestDto requestDto, List<MultipartFile> images) throws IOException {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + guideId));

        if (images == null || images.isEmpty() || images.size() < 1) {
            throw new IllegalArgumentException("At least 1 image is required");
        }

        if (images.size() > 3) {
            throw new IllegalArgumentException("Maximum 3 images are allowed");
        }

        Post post = postMapper.toEntity(requestDto, guide);

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            String imagePath = fileService.saveFile(image);
            imagePaths.add(imagePath);
        }
        post.setImages(imagePaths);

        Post savedPost = postRepository.save(post);

        return postMapper.toResponseDto(savedPost);
    }

    @Override
    public List<PostResponseDto> getPostsByGuideId(Long guideId) {
        guideRepository.findById(guideId)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + guideId));

        List<Post> posts = postRepository.findByGuideIdOrderByCreatedAtDesc(guideId);
        return postMapper.toResponseDtoList(posts);
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        int likeCount = likeService.getPostLikeCount(postId);
        int commentCount = commentService.getPostCommentCount(postId);
        List<CommentResponseDto> comments = commentService.getPostComments(postId);

        // Check if current user has liked the post
        boolean isLiked = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            User currentUser = (User) authentication.getPrincipal();
            isLiked = likeService.isPostLikedByUser(postId, currentUser.getId());
        }

        return postMapper.toResponseDto(post, likeCount, commentCount, comments, isLiked);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        postMapper.updateEntityFromDto(updateDto, post);
        Post updatedPost = postRepository.save(post);

        return postMapper.toResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public PostResponseDto updatePostImages(Long postId, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty() ) {
            throw new IllegalArgumentException("At least 1 image is required");
        }

        if (images.size() > 3) {
            throw new IllegalArgumentException("Maximum 3 images are allowed");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        for (String imagePath : post.getImages()) {
            fileService.deleteFile(imagePath);
        }

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            String imagePath = fileService.saveFile(image);
            imagePaths.add(imagePath);
        }
        post.setImages(imagePaths);

        Post updatedPost = postRepository.save(post);
        return postMapper.toResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        for (String imagePath : post.getImages()) {
            fileService.deleteFile(imagePath);
        }

        postRepository.delete(post);
    }
}