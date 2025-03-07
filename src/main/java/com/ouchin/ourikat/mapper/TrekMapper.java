package com.ouchin.ourikat.mapper;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.entity.Trek;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {HighlightMapper.class})
public interface TrekMapper {
    TrekMapper INSTANCE = Mappers.getMapper(TrekMapper.class);

    Trek toEntity(TrekRequest trekRequest);
    TrekResponse toResponse(Trek trek);
}