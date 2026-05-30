package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto

data class UserDto(
    var id: Int,
    var name: String,
    var email: String,
    var hashedPassword: String,
    var role: String,
    var hasTemporaryPassword: Boolean
)
