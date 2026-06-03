package com.dilillo.adminMicroservice.businessLayer.adapter

data class LoginResponse(val token: String, val role: String, val hasTemporaryPassword: Boolean)