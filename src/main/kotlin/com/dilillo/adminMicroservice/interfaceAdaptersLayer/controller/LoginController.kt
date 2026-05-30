package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.exception.IncorrectCredentialsException
import com.dilillo.adminMicroservice.businessLayer.exception.PasswordNotValidForRegistrationException
import com.dilillo.adminMicroservice.businessLayer.exception.UserAlreadyExistException
import com.dilillo.adminMicroservice.businessLayer.exception.UserNotFoundException
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val businessLogic: BusinessLogic
) {

    @PostMapping("/hello")
    fun hello(@RequestBody request: LoginRequest): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO :D " + request.email)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val tokenResult = this.businessLogic.login(request)

        return if (tokenResult.isSuccess) {
            ResponseEntity.ok(LoginResponse(tokenResult.getOrThrow()))
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

}