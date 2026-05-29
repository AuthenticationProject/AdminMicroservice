package com.dilillo.adminMicroservice.businessLayer.boundaries

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest

interface BusinessLogic {

    fun registerUser(registerRequest: RegisterRequest): Result<Unit>

    fun registerAdmin(registerRequest: RegisterRequest): Result<Unit>

    fun login(loginRequest: LoginRequest): Result<LoginResponse>

    fun checkUserRegistered(): Result<LoginResponse>

    fun checkAdminRegistered(): Result<LoginResponse>



}