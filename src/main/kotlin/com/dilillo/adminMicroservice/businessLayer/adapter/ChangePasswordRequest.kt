package com.dilillo.adminMicroservice.businessLayer.adapter

data class ChangePasswordRequest(val email: String, val newPassword: String)
