package com.dilillo.adminMicroservice.businessLayer.adapter

data class LoginResponse(val token: String, val hasTemporaryPassword: Boolean)