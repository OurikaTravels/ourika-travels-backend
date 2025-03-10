package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.ActivityRequest;
import com.ouchin.ourikat.dto.response.ActivityResponse;
import com.ouchin.ourikat.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {
    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trek", ignore = true)
    Activity toEntity(ActivityRequest activityRequest);

    @Mapping(source = "trek.id", target = "trekId")
    ActivityResponse toResponse(Activity activity);
}