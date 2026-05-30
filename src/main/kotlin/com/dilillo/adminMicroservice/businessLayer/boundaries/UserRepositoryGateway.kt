package com.dilillo.adminMicroservice.businessLayer.boundaries

import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.UserEntity

interface UserRepositoryGateway {
    fun findByEmail(email: String): Result<UserEntity>
    fun setTemporaryPassword(email: String, password: String): Boolean
    fun changePassword(email: String, password: String): Boolean
    fun saveUser(user: UserEntity): UserEntity
    fun hasTemporaryPassword(email: String): Boolean?
    fun changeUsername(email: String, newUsername: String): Boolean
}