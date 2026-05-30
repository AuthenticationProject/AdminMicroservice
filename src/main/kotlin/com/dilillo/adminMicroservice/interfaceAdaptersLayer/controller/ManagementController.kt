package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class ManagementController() {

    @GetMapping("/helloAdmin")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO ADMIN :D ")
    }

}