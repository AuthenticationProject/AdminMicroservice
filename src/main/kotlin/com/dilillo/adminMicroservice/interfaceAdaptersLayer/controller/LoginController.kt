package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.security.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val jwtService: JwtService,
    private val businessLogic: BusinessLogic
) {

    @PostMapping("/hello")
    fun hello(@RequestBody request: LoginRequest): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO :D " + request.username)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return if (request.username == "admin" && request.password == "password") {
            val token = jwtService.generateToken(request.username)
            ResponseEntity.ok(LoginResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        return if (request.username != "" && request.email != "" && request.password != "") {
            this.businessLogic.registerUser(request)
            ResponseEntity.ok("User registered")
        } else {
            ResponseEntity.status(401).build()
        }
    }

}