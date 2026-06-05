package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

    @Operation(
        summary = "Login request",
        description = "User performs a login request",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = LoginRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Logged succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginResponse::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = [Content()]
            ), ApiResponse(
                responseCode = "401",
                description = "Not authorized to perform this request",
                content = [Content()]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
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

    @Operation(
        summary = "Registration request",
        description = "User performs a registration request",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = RegisterRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Registered succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "403",
                description = "Resource already exists",
                content = [Content()]
            ), ApiResponse(
                responseCode = "401",
                description = "Not authorized to perform this request",
                content = [Content()]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
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
                    is UserAlreadyExistException -> ResponseEntity.status(403).build()
                    is PasswordNotValidForRegistrationException -> ResponseEntity.status(401).build()
                    else -> ResponseEntity.internalServerError().build()
                }
            }
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @Operation(
        summary = "Temporary password request",
        description = "User requests a temporary password",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SetTemporaryPasswordRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Operation done succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "401",
                description = "Not authorized to perform this request",
                content = [Content()]
            )
        ]
    )
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

}