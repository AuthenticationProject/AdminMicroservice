package com.dilillo.adminMicroservice.businessLayer.exception

class UserNotFoundException : Exception() {
    override val message: String
        get() = "User not found"
}