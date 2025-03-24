package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.response.TrekImageResponse;
import com.ouchin.ourikat.dto.request.TrekImageRequest;
import com.ouchin.ourikat.entity.TrekImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrekImageMapper {
    TrekImageMapper INSTANCE = Mappers.getMapper(TrekImageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "trek", ignore = true)
    TrekImage toEntity(TrekImageRequest trekImageRequest);

    TrekImageResponse toResponse(TrekImage trekImage);

    List<TrekImageResponse> toResponseList(List<TrekImage> images);
}
