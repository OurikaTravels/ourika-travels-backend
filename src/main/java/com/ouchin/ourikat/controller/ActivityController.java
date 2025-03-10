package com.ouchin.ourikat.controller;

import com.ouchin.ourikat.dto.request.ActivityRequest;
import com.ouchin.ourikat.dto.response.ActivityResponse;
import com.ouchin.ourikat.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/treks/{trekId}/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> createActivity(
            @PathVariable Long trekId,
            @Valid @RequestBody ActivityRequest activityRequest
    ) {
        return new ResponseEntity<>(activityService.createActivity(trekId, activityRequest), HttpStatus.CREATED);
    }

    @GetMapping("/activities/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @GetMapping("/treks/{trekId}/activities")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByTrekId(@PathVariable Long trekId) {
        return ResponseEntity.ok(activityService.getActivitiesByTrekId(trekId));
    }

    @PutMapping("/activities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequest activityRequest
    ) {
        return ResponseEntity.ok(activityService.updateActivity(id, activityRequest));
    }

    @DeleteMapping("/activities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}