package com.ouchin.ourikat.mapper;


import com.ouchin.ourikat.dto.request.ServiceRequest;
import com.ouchin.ourikat.dto.response.ServiceResponse;
import com.ouchin.ourikat.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treks", ignore = true)
    Service toEntity(ServiceRequest serviceRequest);

    ServiceResponse toResponse(Service service);
}