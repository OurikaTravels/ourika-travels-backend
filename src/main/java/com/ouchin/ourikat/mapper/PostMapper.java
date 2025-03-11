package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "guide", source = "guide")
    Post toEntity(PostRequestDto requestDto, Guide guide);

    // New method with additional parameters
    @Mapping(target = "guideId", source = "post.guide.id")
    @Mapping(target = "likeCount", expression = "java(likeCount)")
    @Mapping(target = "commentCount", expression = "java(commentCount)")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "isLikedByCurrentUser", expression = "java(isLiked)")
    PostResponseDto toResponseDto(Post post, int likeCount, int commentCount, List<CommentResponseDto> comments, boolean isLiked);

    // Original method - updated to include default values for new fields
    @Mapping(target = "guideId", source = "guide.id")
    @Mapping(target = "likeCount", constant = "0")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "comments", expression = "java(new ArrayList<>())")
    @Mapping(target = "isLikedByCurrentUser", constant = "false")
    PostResponseDto toResponseDto(Post post);

    // Method to map a list of posts
    List<PostResponseDto> toResponseDtoList(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "guide", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);
}