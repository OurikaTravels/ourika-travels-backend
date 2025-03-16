package com.ouchin.ourikat.service;

import com.ouchin.ourikat.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a verification email to the user with a link to verify their email address.
     *
     * @param to    The recipient's email address.
     * @param token The verification token.
     */
    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify Your Email");
        message.setText("Click the link to verify your email: http://localhost:8080/api/auth/verify?token=" + token);
        mailSender.send(message);
    }

    /**
     * Sends a password reset email to the user with a link to reset their password.
     *
     * @param to    The recipient's email address.
     * @param token The password reset token.
     */
    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Your Password");
        message.setText("Click the link to reset your password: http://localhost:8080/api/auth/reset-password?token=" + token);
        mailSender.send(message);
    }


    public void sendReservationConfirmationEmail(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Confirmation");
        message.setText("Your reservation for " + reservation.getTrek().getTitle() + " has been confirmed.");
        mailSender.send(message);
    }

    public void sendReservationCancellationEmail(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Cancellation");
        message.setText("Your reservation for " + reservation.getTrek().getTitle() + " has been cancelled.");
        mailSender.send(message);
    }

    public void sendGuideAssignmentEmail(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Reservation Assignment");
        message.setText("You have been assigned to guide the trek: " + reservation.getTrek().getTitle());
        mailSender.send(message);
    }

    public void sendReservationApprovalEmail(String to, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Approved");
        message.setText("Your reservation for " + reservation.getTrek().getTitle() + " has been approved.");
        mailSender.send(message);
    }


}