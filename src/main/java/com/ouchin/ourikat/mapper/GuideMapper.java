package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.GuideRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.GuideResponseDto;
import com.ouchin.ourikat.entity.Guide;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuideMapper {
    @Mapping(target = "role", constant = "GUIDE")
    Guide toEntity(GuideRegistrationRequestDto requestDto);

    GuideResponseDto toResponseDto(Guide guide);
}