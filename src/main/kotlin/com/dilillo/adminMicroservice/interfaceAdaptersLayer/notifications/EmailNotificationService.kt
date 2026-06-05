package com.dilillo.adminMicroservice.interfaceAdaptersLayer.notifications

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * Email spring service
 */
@Service
class EmailNotificationService(private val mailSender: JavaMailSender) {

    fun sendSimpleEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage().apply {
            setTo(to)
            setSubject(subject)
            setText(text)
        }
        mailSender.send(message)
    }
}