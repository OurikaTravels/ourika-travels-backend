package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.PostRequestDto;
import com.ouchin.ourikat.dto.request.PostUpdateRequestDto;
import com.ouchin.ourikat.dto.response.PostResponseDto;
import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "guide", source = "guide")
    Post toEntity(PostRequestDto requestDto, Guide guide);

    PostResponseDto toResponseDto(Post post);

    List<PostResponseDto> toResponseDtoList(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "guide", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);
}