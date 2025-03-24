package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.service.CommentService;
import com.ouchin.ourikat.service.LikeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, GuideMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "status", constant = "ACCEPTED")
    @Mapping(target = "guide", source = "guide")
    Post toEntity(PostRequestDto requestDto, Guide guide);

    @Mapping(target = "guide", source = "post.guide")
    @Mapping(target = "guideId", source = "post.guide.id")
    @Mapping(target = "likeCount", expression = "java(likeCount)")
    @Mapping(target = "commentCount", expression = "java(commentCount)")
    @Mapping(target = "comments", source = "comments")
    PostResponseDto toResponseDto(Post post, int likeCount, int commentCount, List<CommentResponseDto> comments);

    default List<PostResponseDto> toResponseDtoList(List<Post> posts, LikeService likeService, CommentService commentService) {
        return posts.stream()
                .map(post -> {
                    int likeCount = likeService.getPostLikeCount(post.getId());
                    int commentCount = commentService.getPostCommentCount(post.getId());
                    List<CommentResponseDto> comments = commentService.getPostComments(post.getId());
                    return toResponseDto(post, likeCount, commentCount, comments);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "guide", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);

    PostResponseDto toResponseDto(Post savedPost);
}