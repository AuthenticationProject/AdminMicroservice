package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * Set temporary password request when user forgets the password
 */
data class SetTemporaryPasswordRequest(val email: String)