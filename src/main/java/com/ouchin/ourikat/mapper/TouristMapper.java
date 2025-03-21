package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.TouristRegistrationRequestDto;
import com.ouchin.ourikat.dto.response.TouristResponseDto;
import com.ouchin.ourikat.entity.Tourist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface TouristMapper {

    @Mapping(target = "role", constant = "TOURIST")
    Tourist toEntity(TouristRegistrationRequestDto requestDto);

    @Mapping(source = "verified", target = "verified")
    TouristResponseDto toResponseDto(Tourist tourist);
}