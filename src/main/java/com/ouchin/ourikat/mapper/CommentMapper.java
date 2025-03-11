package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.CommentRequestDto;
import com.ouchin.ourikat.dto.response.CommentResponseDto;
import com.ouchin.ourikat.entity.Comment;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentRequestDto requestDto, User user, Post post);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFirstName", source = "user.firstName")
    @Mapping(target = "userLastName", source = "user.lastName")
    CommentResponseDto toResponseDto(Comment comment);

    List<CommentResponseDto> toResponseDtoList(List<Comment> comments);
}