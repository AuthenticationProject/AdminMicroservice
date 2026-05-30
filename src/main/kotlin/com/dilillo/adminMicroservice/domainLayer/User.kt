package com.dilillo.adminMicroservice.domainLayer

data class User(
    val username: String,
    val password: String,
    val email: String,
    val role: Role
)
