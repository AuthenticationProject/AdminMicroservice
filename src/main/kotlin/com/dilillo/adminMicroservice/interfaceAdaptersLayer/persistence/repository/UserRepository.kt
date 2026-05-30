package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<UserEntity, Integer> {
    fun findByEmail(email: String?): Optional<UserEntity?>?
}