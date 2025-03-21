package com.ouchin.ourikat.service.impl;

import com.ouchin.ourikat.dto.request.ReservationRequestDto;
import com.ouchin.ourikat.dto.request.AssignGuideRequestDto;
import com.ouchin.ourikat.dto.response.ReservationResponseDto;
import com.ouchin.ourikat.dto.response.ReservationStatisticsResponseDto;
import com.ouchin.ourikat.entity.*;
import com.ouchin.ourikat.enums.ReservationStatus;
import com.ouchin.ourikat.exception.ResourceNotFoundException;
import com.ouchin.ourikat.mapper.ReservationMapper;
import com.ouchin.ourikat.repository.*;
import com.ouchin.ourikat.service.EmailService;
import com.ouchin.ourikat.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TouristRepository touristRepository;
    private final TrekRepository trekRepository;
    private final GuideRepository guideRepository;
    private final ReservationMapper reservationMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public ReservationResponseDto createReservation(Long touristId, ReservationRequestDto request) {
        Tourist tourist = touristRepository.findById(touristId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist not found"));

        Trek trek = trekRepository.findById(request.getTrekId())
                .orElseThrow(() -> new ResourceNotFoundException("Trek not found"));

        Reservation reservation = new Reservation();
        reservation.setTourist(tourist);
        reservation.setTrek(trek);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStartDate(request.getStartDate());
        reservation.setChildCount(request.getChildCount());
        reservation.setAdultCount(request.getAdultCount());
        reservation.setEndDate(request.getEndDate());
        reservation.setTotalPrice(trek.getPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);

        // Send email to tourist
        //emailService.sendReservationConfirmationEmail(tourist.getEmail(), savedReservation);

        return reservationMapper.toResponseDto(savedReservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        emailService.sendReservationCancellationEmail(reservation.getTourist().getEmail(), reservation);
    }

    @Override
    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ReservationResponseDto assignGuideToReservation(Long reservationId, AssignGuideRequestDto request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Guide guide = guideRepository.findById(request.getGuideId())
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        reservation.setGuide(guide);
        reservation.setStatus(ReservationStatus.APPROVED); // Set status to APPROVED
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Send email to guide
        emailService.sendGuideAssignmentEmail(guide.getEmail(), updatedReservation);

        return reservationMapper.toResponseDto(updatedReservation);
    }

    @Override
    @Transactional
    public ReservationResponseDto approveReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus(ReservationStatus.APPROVED);
        Reservation updatedReservation = reservationRepository.save(reservation);

        emailService.sendReservationApprovalEmail(reservation.getTourist().getEmail(), updatedReservation);

        return reservationMapper.toResponseDto(updatedReservation);
    }

    @Override
    @Transactional
    public ReservationResponseDto cancelReservationByAdmin(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);

        emailService.sendReservationCancellationEmail(reservation.getTourist().getEmail(), updatedReservation);

        return reservationMapper.toResponseDto(updatedReservation);
    }

    @Override
    public long getTotalReservations() {
        return reservationRepository.count();
    }


    @Override
    public ReservationStatisticsResponseDto getReservationStatistics() {
        long totalReservations = reservationRepository.count();
        long totalPendingReservations = reservationRepository.countByStatus(ReservationStatus.PENDING);

        log.info("Total Reservations: {}", totalReservations);
        log.info("Total Pending Reservations: {}", totalPendingReservations);

        return new ReservationStatisticsResponseDto(totalReservations, totalPendingReservations);
    }

    public ReservationResponseDto notifyGuideAboutLatestReservation(Long guideId) {
        Optional<Reservation> lastReservation = reservationRepository.findFirstByGuideIdOrderByReservationDateDesc(guideId);

        if (lastReservation.isPresent()) {
            Reservation reservation = lastReservation.get();
            return reservationMapper.toResponseDto(reservation);
        } else {
            throw new RuntimeException("No reservations found for guide " + guideId);
        }
    }

    @Override
    public List<ReservationResponseDto> getAllByTouristId(Long touristId) {
        List<Reservation> reservations = reservationRepository.getByTouristId(touristId);

        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found for tourist ID: " + touristId);
        }

        return reservations.stream()
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}