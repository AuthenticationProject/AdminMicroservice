package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.ChangePasswordRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.SetTemporaryPasswordRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.exception.IncorrectCredentialsException
import com.dilillo.adminMicroservice.businessLayer.exception.PasswordNotValidForRegistrationException
import com.dilillo.adminMicroservice.businessLayer.exception.UserAlreadyExistException
import com.dilillo.adminMicroservice.businessLayer.exception.UserNotFoundException
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.notifications.EmailNotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val businessLogic: BusinessLogic,
    private val emailNotificationService: EmailNotificationService,
) {

    @PostMapping("/hello")
    fun hello(@RequestBody request: LoginRequest): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO :D " + request.email)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val tokenResult = this.businessLogic.login(request)

        return if (tokenResult.isSuccess) {
            emailNotificationService.sendSimpleEmail(
                request.email,
                "Login request",
                "A new login of your account has been requested. If it weren't you contact us."
            )
            ResponseEntity.ok(tokenResult.getOrNull())
        } else {
            when (tokenResult.exceptionOrNull()) {
                is UserNotFoundException -> ResponseEntity.status(404).build()
                is IncorrectCredentialsException -> ResponseEntity.status(401).build()
                else -> ResponseEntity.internalServerError().build()
            }
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        return if (request.username != "" && request.email != "" && request.password != "") {
            val result = this.businessLogic.registerUser(request)
            return if (result.isSuccess) {
                emailNotificationService.sendSimpleEmail(
                    request.email,
                    "Registration confirmation",
                    "Congratulations!! You have been registered successfully!",
                )
                ResponseEntity.ok("User registered")
            } else {
                when (result.exceptionOrNull()) {
                    is UserAlreadyExistException -> ResponseEntity.status(401).build()
                    is PasswordNotValidForRegistrationException -> ResponseEntity.status(401).build()
                    else -> ResponseEntity.internalServerError().build()
                }
            }
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @PostMapping("/setTemporaryPassword")
    fun setTemporaryPassword(@RequestBody request: SetTemporaryPasswordRequest): ResponseEntity<String> {
        return if (request.email.isNotEmpty()) {
            val result = this.businessLogic.setTemporaryPassword(request.email)
            emailNotificationService.sendSimpleEmail(
                request.email,
                "New Temporary Password Set",
                "Congratulations!! We have generated a new temporary " +
                        "password for you, to be modified after your next login." +
                        "Use this password: " + result,
            )
            ResponseEntity.ok("Password changed")
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @PostMapping("/changePassword")
    fun changePassword(@RequestBody request: ChangePasswordRequest): ResponseEntity<String> {
        return if (request.email.isNotEmpty()) {
            val result = this.businessLogic.changePassword(request.email, request.newPassword)
            emailNotificationService.sendSimpleEmail(
                request.email,
                "Password changed",
                "Your password has been changed correctly, if it weren't you contact us.",
            )
            ResponseEntity.ok("Password changed")
        } else {
            ResponseEntity.status(401).build()
        }
    }

}