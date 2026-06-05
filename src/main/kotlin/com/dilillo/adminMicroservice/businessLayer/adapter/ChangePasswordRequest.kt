package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * Data for change password request
 */
data class ChangePasswordRequest(val email: String, val newPassword: String)
