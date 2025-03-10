package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.ServiceEntityRequest;
import com.ouchin.ourikat.dto.response.ServiceEntityResponse;
import com.ouchin.ourikat.service.ServiceEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceEntityController {

    private final ServiceEntityService serviceService;

    @Autowired
    public ServiceEntityController(ServiceEntityService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<ServiceEntityResponse> createService(@Valid @RequestBody ServiceEntityRequest request) {
        return new ResponseEntity<>(serviceService.createService(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntityResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceEntityResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntityResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceEntityRequest request) {
        return ResponseEntity.ok(serviceService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServiceEntityResponse>> searchServicesByName(@RequestParam String name) {
        return ResponseEntity.ok(serviceService.searchServicesByName(name));
    }
}