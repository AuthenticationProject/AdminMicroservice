package com.dilillo.adminMicroservice.businessLayer.boundaries

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.domainLayer.User

interface BusinessLogic {

    fun registerUser(registerRequest: RegisterRequest): Result<User>

    fun registerAdmin(registerRequest: RegisterRequest): Result<User>

    fun login(loginRequest: LoginRequest): Result<LoginResponse>

    fun getUserInfo(email: String): Result<User>

    fun changePassword(email: String, newPassword: String): Boolean

    fun setTemporaryPassword(email: String): String

    fun changeUsername(email: String, username: String): Boolean
}