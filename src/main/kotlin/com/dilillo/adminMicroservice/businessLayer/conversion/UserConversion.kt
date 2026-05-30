package com.dilillo.adminMicroservice.businessLayer.conversion

import com.dilillo.adminMicroservice.domainLayer.Role
import com.dilillo.adminMicroservice.domainLayer.User
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserDto
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.UserEntity

fun UserEntity.toUserModel(): User {
    return User(
        this.name!!,
        this.hashedPassword!!,
        this.email!!,
        Role.valueOf(this.role!!)
    )
}
