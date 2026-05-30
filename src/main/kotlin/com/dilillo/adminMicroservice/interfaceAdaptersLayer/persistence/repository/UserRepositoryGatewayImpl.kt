package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.businessLayer.boundaries.UserRepositoryGateway
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.UserEntity

class UserRepositoryGatewayImpl(val userRepository: UserRepository): UserRepositoryGateway {
    override fun findByEmail(email: String): Result<UserEntity> {
        val result = userRepository.findByEmail(email) ?: return Result.failure(Throwable("User not found!"))
        if(result.isEmpty) return Result.failure(Throwable("User not found!"))

        return Result.success(result.get())
    }

    override fun setTemporaryPassword(email: String, password: String): Boolean {
        val user = this.findByEmail(email).map { user ->
            user.apply {
                hasTemporaryPassword = true
                hashedPassword = password
            }
            this.userRepository.save(user)
        }

        return user.isSuccess
    }

    override fun changePassword(email: String, password: String): Boolean {
        val user = this.findByEmail(email).map { user ->
            user.apply {
                hasTemporaryPassword = false
                hashedPassword = password
            }
            this.userRepository.save(user)
        }

        return user.isSuccess
    }

    override fun saveUser(user: UserEntity): UserEntity {
        return this.userRepository.save(user)
    }

    override fun hasTemporaryPassword(email: String): Boolean? {
        return this.findByEmail(email).getOrNull()?.hasTemporaryPassword
    }
}