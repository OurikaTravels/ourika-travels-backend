package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

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


    @Mapping(target = "guide", source = "guide")
    @Mapping(target = "guideId", source = "guide.id")
    @Mapping(target = "likeCount", constant = "0")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "comments", expression = "java(new ArrayList<>())")
    @Mapping(target = "isLikedByCurrentUser", constant = "false")
    PostResponseDto toResponseDto(Post post);


    List<PostResponseDto> toResponseDtoList(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "guide", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);
}