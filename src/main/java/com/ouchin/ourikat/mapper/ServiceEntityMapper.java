package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.request.ServiceEntityRequest;
import com.ouchin.ourikat.dto.response.ServiceEntityResponse;
import com.ouchin.ourikat.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceEntityMapper {
    ServiceEntityMapper INSTANCE = Mappers.getMapper(ServiceEntityMapper.class);

    ServiceEntity toEntity(ServiceEntityRequest serviceEntityRequest);
    ServiceEntityResponse toResponse(ServiceEntity serviceEntity);
}
