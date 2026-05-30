package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.ChangeUsernameRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val businessLogic: BusinessLogic
) {
    @PostMapping("/changeUsername")
    fun changeUsername(@RequestBody request: ChangeUsernameRequest, @AuthenticationPrincipal emailDalToken: UserDetails): ResponseEntity<String> {
        println("L'utente che sta facendo la richiesta è: ${emailDalToken.username}")
        if(this.businessLogic.changeUsername(request.email, request.newUsername))
            //TODO Add email notification
            return ResponseEntity.ok("Username changed: " + request.newUsername)
        else
            return ResponseEntity.internalServerError().build()
    }
}