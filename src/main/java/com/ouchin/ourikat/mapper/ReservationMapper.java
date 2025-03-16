package com.ouchin.ourikat.mapper;

import com.ouchin.ourikat.dto.response.ReservationResponseDto;
import com.ouchin.ourikat.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationResponseDto toResponseDto(Reservation reservation);
}