package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.CategoryRequestDto;
import com.ouchin.ourikat.dto.response.CategoryResponseDto;
import com.ouchin.ourikat.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryRequestDto requestDto);

    CategoryResponseDto toResponseDto(Category category);
}