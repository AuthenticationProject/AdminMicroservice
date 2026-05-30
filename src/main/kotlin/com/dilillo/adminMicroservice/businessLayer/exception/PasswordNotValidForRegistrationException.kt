package com.dilillo.adminMicroservice.businessLayer.exception

class PasswordNotValidForRegistrationException  : Exception() {
    override val message: String
        get() = "Password is not valid for registration"
}