package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.TrekRequest;
import com.ouchin.ourikat.dto.response.TrekResponse;
import com.ouchin.ourikat.entity.Category;
import com.ouchin.ourikat.entity.Trek;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.TrekMapper;
import com.ouchin.ourikat.repository.CategoryRepository;
import com.ouchin.ourikat.repository.TrekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrekServiceImplTest {

    @Mock
    private TrekRepository trekRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TrekMapper trekMapper;

    @InjectMocks
    private TrekServiceImpl trekService;

    private TrekRequest trekRequest;
    private Category category;
    private Trek trek;
    private TrekResponse trekResponse;

    @BeforeEach
    void setUp() {
        trekRequest = new TrekRequest();
        trekRequest.setTitle("Test Trek");
        trekRequest.setDescription("Test Description");
        trekRequest.setCategoryId(1L);

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        trek = new Trek();
        trek.setId(1L);
        trek.setTitle("Test Trek");
        trek.setDescription("Test Description");
        trek.setCategory(category);

        trekResponse = new TrekResponse();
        trekResponse.setId(1L);
        trekResponse.setTitle("Test Trek");
        trekResponse.setDescription("Test Description");
        trekResponse.setCategoryId(1L);
    }

    @Test
    void createTrek_SuccessfulCreation() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(trekMapper.toEntity(trekRequest)).thenReturn(trek);
        when(trekRepository.save(trek)).thenReturn(trek);
        when(trekMapper.toResponse(trek)).thenReturn(trekResponse);


        TrekResponse result = trekService.createTrek(trekRequest);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Trek", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(1L, result.getCategoryId());

        verify(categoryRepository, times(1)).findById(1L);
        verify(trekMapper, times(1)).toEntity(trekRequest);
        verify(trekRepository, times(1)).save(trek);
        verify(trekMapper, times(1)).toResponse(trek);
    }

    @Test
    void createTrek_InvalidCategoryId_ThrowsResourceNotFoundException() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            trekService.createTrek(trekRequest);
        });

        assertEquals("Category not found with id: 1", exception.getMessage());

        verify(categoryRepository, times(1)).findById(1L);
        verify(trekMapper, never()).toEntity(any());
        verify(trekRepository, never()).save(any());
        verify(trekMapper, never()).toResponse(any());
    }

    @Test
    void createTrek_MissingCategoryId_ThrowsIllegalArgumentException() {

        trekRequest.setCategoryId(null);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trekService.createTrek(trekRequest);
        });

        assertEquals("Category ID is required.", exception.getMessage());

        verify(categoryRepository, never()).findById(any());
        verify(trekMapper, never()).toEntity(any());
        verify(trekRepository, never()).save(any());
        verify(trekMapper, never()).toResponse(any());
    }
}