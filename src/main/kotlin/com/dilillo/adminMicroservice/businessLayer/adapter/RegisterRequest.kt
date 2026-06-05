package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * Body for registration request
 */
data class RegisterRequest(val username: String, val email: String, val password: String)
