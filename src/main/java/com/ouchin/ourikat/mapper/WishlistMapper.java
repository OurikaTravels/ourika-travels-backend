package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.response.WishlistResponseDto;
import com.ouchin.ourikat.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TrekMapper.class})
public interface WishlistMapper {
    @Mapping(target = "touristId", source = "tourist.id")
    @Mapping(target = "trek", source = "trek")
    WishlistResponseDto toResponseDto(Wishlist wishlist);
}