package com.dilillo.adminMicroservice.businessLayer

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.domainLayer.Role
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserEntity
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.UserRepository

class AdminUseCase(val userRepository: UserRepository): BusinessLogic {
    override fun registerUser(registerRequest: RegisterRequest): Result<Unit> {
        this.userRepository.save(UserEntity().build(
            registerRequest.username,
            registerRequest.email,
            registerRequest.password,
            Role.USER.toString()
        ))
        return Result.success(Unit)
    }

    override fun registerAdmin(registerRequest: RegisterRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun checkUserRegistered(): Result<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun checkAdminRegistered(): Result<LoginResponse> {
        TODO("Not yet implemented")
    }
}