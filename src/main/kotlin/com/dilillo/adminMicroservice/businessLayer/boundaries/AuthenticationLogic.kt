package com.dilillo.adminMicroservice.businessLayer.boundaries

/**
 * Authentication logic for token generation and validation
 */
interface AuthenticationLogic {

    fun generateToken(username: String, role: String): String

    fun extractUsername(token: String): String

    fun validateToken(token: String, username: String): Boolean

    fun extractRole(token: String): String

}