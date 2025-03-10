package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.ServiceRequest;
import com.ouchin.ourikat.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse getServiceById(Long id);
    List<ServiceResponse> getAllServices();
    ServiceResponse updateService(Long id, ServiceRequest request);
    void deleteService(Long id);
    ServiceResponse addTrekToService(Long serviceId, Long trekId);
    ServiceResponse removeTrekFromService(Long serviceId, Long trekId);
}

