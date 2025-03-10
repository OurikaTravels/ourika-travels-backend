package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.ServiceRequest;
import com.ouchin.ourikat.dto.response.ServiceResponse;
import com.ouchin.ourikat.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        return new ResponseEntity<>(serviceService.createService(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long id, @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(serviceService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{serviceId}/treks/{trekId}")
    public ResponseEntity<ServiceResponse> addTrekToService(@PathVariable Long serviceId, @PathVariable Long trekId) {
        return ResponseEntity.ok(serviceService.addTrekToService(serviceId, trekId));
    }

    @DeleteMapping("/{serviceId}/treks/{trekId}")
    public ResponseEntity<ServiceResponse> removeTrekFromService(@PathVariable Long serviceId, @PathVariable Long trekId) {
        return ResponseEntity.ok(serviceService.removeTrekFromService(serviceId, trekId));
    }
}