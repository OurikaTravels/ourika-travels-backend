package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.InclusionItemRequest;
import com.ouchin.ourikat.dto.response.InclusionItemResponse;
import com.ouchin.ourikat.entity.InclusionItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InclusionItemMapper {
    InclusionItemMapper INSTANCE = Mappers.getMapper(InclusionItemMapper.class);

    InclusionItem toEntity(InclusionItemRequest inclusionItemRequest);
    InclusionItemResponse toResponse(InclusionItem inclusionItem);
}