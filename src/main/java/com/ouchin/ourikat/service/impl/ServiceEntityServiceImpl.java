package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.ServiceEntityRequest;
import com.ouchin.ourikat.dto.response.ServiceEntityResponse;
import com.ouchin.ourikat.entity.ServiceEntity;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.ServiceEntityMapper;
import com.ouchin.ourikat.repository.ServiceEntityRepository;
import com.ouchin.ourikat.service.ServiceEntityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceEntityServiceImpl implements ServiceEntityService {

    private final ServiceEntityRepository serviceRepository;
    private final ServiceEntityMapper serviceMapper;

    @Autowired
    public ServiceEntityServiceImpl(ServiceEntityRepository serviceRepository,
                                    ServiceEntityMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    @Transactional
    public ServiceEntityResponse createService(ServiceEntityRequest request) {
        if (serviceRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Service with name '" + request.getName() + "' already exists");
        }

        ServiceEntity service = serviceMapper.toEntity(request);
        ServiceEntity savedService = serviceRepository.save(service);
        return serviceMapper.toResponse(savedService);
    }

    @Override
    @Transactional
    public ServiceEntityResponse getServiceById(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return serviceMapper.toResponse(service);
    }

    @Override
    @Transactional
    public List<ServiceEntityResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServiceEntityResponse updateService(Long id, ServiceEntityRequest request) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        if (!service.getName().equalsIgnoreCase(request.getName())
                && serviceRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Service with name '" + request.getName() + "' already exists");
        }

        service.setName(request.getName());
        ServiceEntity updatedService = serviceRepository.save(service);
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
    public List<ServiceEntityResponse> searchServicesByName(String name) {
        return serviceRepository.findByNameContainingIgnoreCase(name).stream()
                .map(serviceMapper::toResponse)
                .collect(Collectors.toList());
    }

}
