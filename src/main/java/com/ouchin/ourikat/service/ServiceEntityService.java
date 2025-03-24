package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.ServiceEntityRequest;
import com.ouchin.ourikat.dto.response.ServiceEntityResponse;

import java.util.List;
import java.util.Set;

public interface ServiceEntityService {
    ServiceEntityResponse createService(ServiceEntityRequest request);
    ServiceEntityResponse getServiceById(Long id);
    List<ServiceEntityResponse> getAllServices();
    ServiceEntityResponse updateService(Long id, ServiceEntityRequest request);
    void deleteService(Long id);
    List<ServiceEntityResponse> searchServicesByName(String name);
}