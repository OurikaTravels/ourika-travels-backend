package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.CommentRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.entity.Comment;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.entity.User;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.exception.UnauthorizedException;
import com.ouchin.ourikat.mapper.CommentMapper;
import com.ouchin.ourikat.repository.CommentRepository;
import com.ouchin.ourikat.repository.PostRepository;
import com.ouchin.ourikat.repository.UserRepository;
import com.ouchin.ourikat.service.CommentService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto addComment(Long postId, Long userId, CommentRequestDto requestDto) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Comment comment = commentMapper.toEntity(requestDto, user, post);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponseDto(savedComment);
    }

    @Override
    public List<CommentResponseDto> getPostComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        return commentMapper.toResponseDtoList(comments);
    }

    @Override
    public CommentResponseDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        return commentMapper.toResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Long commentId, Long userId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }

        comment.setContent(requestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toResponseDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId) &&
                !comment.getPost().getGuide().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public int getPostCommentCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        return commentRepository.countByPost(post);
    }
}
