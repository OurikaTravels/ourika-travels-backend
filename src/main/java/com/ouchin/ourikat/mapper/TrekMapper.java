package com.ouchin.ourikat.mapper;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.entity.Trek;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {HighlightMapper.class, ServiceEntityMapper.class, ActivityMapper.class, TrekImageMapper.class})
public interface TrekMapper {
    TrekMapper INSTANCE = Mappers.getMapper(TrekMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "highlights", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "images", ignore = true)
    Trek toEntity(TrekRequest trekRequest);

    @Mapping(source = "category.id", target = "categoryId")
    TrekResponse toResponse(Trek trek);
}