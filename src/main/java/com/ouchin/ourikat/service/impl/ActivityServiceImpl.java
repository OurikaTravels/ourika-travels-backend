package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.ActivityRequest;
import com.ouchin.ourikat.dto.response.ActivityResponse;
import com.ouchin.ourikat.entity.Activity;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.ActivityMapper;
import com.ouchin.ourikat.repository.ActivityRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import com.ouchin.ourikat.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final TrekRepository trekRepository;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional
    public ActivityResponse createActivity(Long trekId, ActivityRequest activityRequest) {
        log.debug("Creating activity for trek with ID: {}", trekId);

        Trek trek = trekRepository.findById(trekId)
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found with id: " + trekId));

        Activity activity = activityMapper.toEntity(activityRequest);
        activity.setTrek(trek);

        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity created successfully with ID: {}", savedActivity.getId());

        return activityMapper.toResponse(savedActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(Long id) {
        log.debug("Fetching activity with ID: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));

        return activityMapper.toResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivitiesByTrekId(Long trekId) {
        log.debug("Fetching activities for trek with ID: {}", trekId);

        List<Activity> activities = activityRepository.findByTrekIdOrderByActivityOrder(trekId);
        return activities.stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ActivityResponse updateActivity(Long id, ActivityRequest activityRequest) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));

        activity.setTitle(activityRequest.getTitle());
        activity.setType(activityRequest.getType());
        activity.setDescription(activityRequest.getDescription());
        activity.setIsOptional(activityRequest.getIsOptional());
        activity.setTransportType(activityRequest.getTransportType());
        activity.setTransportDuration(activityRequest.getTransportDuration());
        activity.setActivityOrder(activityRequest.getActivityOrder());

        Activity updatedActivity = activityRepository.save(activity);
        return activityMapper.toResponse(updatedActivity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        log.debug("Deleting activity with ID: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));

        activityRepository.delete(activity);
        log.info("Activity deleted successfully with ID: {}", id);
    }
}