package com.ouchin.ourikat.service.impl;


import com.ouchin.ourikat.dto.request.ServiceRequest;
import com.ouchin.ourikat.dto.response.ServiceResponse;
import com.ouchin.ourikat.entity.Service;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.exception.ResourceAlreadyExistsException;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.ServiceMapper;
import com.ouchin.ourikat.repository.ServiceRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final TrekRepository trekRepository;
    private final ServiceMapper serviceMapper;

    @Override
    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        if (serviceRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Service with name " + request.getName() + " already exists");
        }

        Service service = serviceMapper.toEntity(request);

        if (request.getTrekIds() != null && !request.getTrekIds().isEmpty()) {
            request.getTrekIds().forEach(trekId -> {
                Trek trek = trekRepository.findById(trekId)
                        .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));
                service.addTrek(trek);
            });
        }

        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponse(savedService);
    }

    @Override
    public ServiceResponse getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return serviceMapper.toResponse(service);
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        // Check if name already exists for another service
        if (!service.getName().equals(request.getName()) && serviceRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Service with name " + request.getName() + " already exists");
        }

        service.setName(request.getName());

        // Update trek associations if needed
        if (request.getTrekIds() != null) {
            service.getTreks().clear();
            request.getTrekIds().forEach(trekId -> {
                Trek trek = trekRepository.findById(trekId)
                        .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));
                service.addTrek(trek);
            });
        }

        Service updatedService = serviceRepository.save(service);
        return serviceMapper.toResponse(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service not found with id: " + id);
        }
        serviceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ServiceResponse addTrekToService(Long serviceId, Long trekId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        service.addTrek(trek);
        Service updatedService = serviceRepository.save(service);
        return serviceMapper.toResponse(updatedService);
    }

    @Override
    @Transactional
    public ServiceResponse removeTrekFromService(Long serviceId, Long trekId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        service.removeTrek(trek);
        Service updatedService = serviceRepository.save(service);
        return serviceMapper.toResponse(updatedService);
    }
}
