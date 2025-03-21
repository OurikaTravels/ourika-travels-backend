package com.ouchin.ourikat.service;

import com.ouchin.ourikat.dto.request.ActivityRequest;
import com.ouchin.ourikat.dto.response.ActivityResponse;
import com.ouchin.ourikat.entity.Activity;

import java.util.List;

public interface ActivityService {
    ActivityResponse createActivity(Long trekId, ActivityRequest activityRequest);
    ActivityResponse getActivityById(Long id);
    List<ActivityResponse> getActivitiesByTrekId(Long trekId);
    ActivityResponse updateActivity(Long id, ActivityRequest activityRequest);
    void deleteActivity(Long id);
}