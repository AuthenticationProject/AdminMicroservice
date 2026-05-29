package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Integer> {
}