package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.HighlightRequest;
import com.ouchin.ourikat.dto.response.HighlightResponse;
import com.ouchin.ourikat.entity.Highlight;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HighlightMapper {
    HighlightMapper INSTANCE = Mappers.getMapper(HighlightMapper.class);

    Highlight toEntity(HighlightRequest highlightRequest);
    HighlightResponse toResponse(Highlight highlight);
}