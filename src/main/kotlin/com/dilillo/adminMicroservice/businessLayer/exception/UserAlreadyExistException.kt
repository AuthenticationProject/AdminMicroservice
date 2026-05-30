package com.dilillo.adminMicroservice.businessLayer.exception

class UserAlreadyExistException : Exception() {
    override val message: String
        get() = "User already exists"
}