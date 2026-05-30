package com.dilillo.adminMicroservice.businessLayer.boundaries

interface AuthenticationLogic {
    fun generateToken(username: String): String
    fun extractUsername(token: String): String
    fun validateToken(token: String, username: String): Boolean
}