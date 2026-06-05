package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * Change username request
 */
data class ChangeUsernameRequest(val email: String, val newUsername: String)
