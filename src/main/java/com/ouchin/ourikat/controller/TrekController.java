package com.ouchin.ourikat.controller;


import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.ApiResponse;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.service.TrekService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treks")
public class TrekController {

    @Autowired
    private TrekService trekService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrekResponse>> createTrek(@Valid @RequestBody TrekRequest trekRequest) {
        TrekResponse responseDto = trekService.createTrek(trekRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Trek created successfully", responseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrekResponse> getTrekById(@PathVariable Long id) {
        TrekResponse trekResponse = trekService.getTrekById(id);
        return ResponseEntity.ok(trekResponse);
    }

    @GetMapping
    public ResponseEntity<List<TrekResponse>> getAllTreks() {
        List<TrekResponse> trekResponses = trekService.getAllTreks();
        return ResponseEntity.ok(trekResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrekResponse> updateTrek(@PathVariable Long id, @RequestBody TrekRequest trekRequest) {
        TrekResponse trekResponse = trekService.updateTrek(id, trekRequest);
        return ResponseEntity.ok(trekResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrek(@PathVariable Long id) {
        trekService.deleteTrek(id);
        return ResponseEntity.noContent().build();
    }
}