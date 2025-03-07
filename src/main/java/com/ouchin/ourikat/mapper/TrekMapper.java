package com.ouchin.ourikat.mapper;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.InclusionItemResponse;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.entity.TrekInclusion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {HighlightMapper.class, InclusionItemMapper.class})
public interface TrekMapper {
    TrekMapper INSTANCE = Mappers.getMapper(TrekMapper.class);

    @Mapping(target = "category", ignore = true)
    Trek toEntity(TrekRequest trekRequest);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "trekInclusions", target = "inclusionItems")
    TrekResponse toResponse(Trek trek);

    default Set<InclusionItemResponse> mapTrekInclusionsToInclusionItems(Set<TrekInclusion> trekInclusions) {
        if (trekInclusions == null) {
            return null;
        }
        return trekInclusions.stream()
                .map(trekInclusion -> {
                    InclusionItemResponse inclusionItemResponse = new InclusionItemResponse();
                    inclusionItemResponse.setId(trekInclusion.getInclusionItem().getId());
                    inclusionItemResponse.setContent(trekInclusion.getInclusionItem().getContent());
                    return inclusionItemResponse;
                })
                .collect(Collectors.toSet());
    }
}