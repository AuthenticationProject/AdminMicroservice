package com.dilillo.adminMicroservice.businessLayer.exception

class IncorrectCredentialsException : Exception() {
    override val message: String
        get() = "Incorrect credentials"
}