package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * Body for login response
 */
data class LoginResponse(val token: String, val role: String, val hasTemporaryPassword: Boolean)