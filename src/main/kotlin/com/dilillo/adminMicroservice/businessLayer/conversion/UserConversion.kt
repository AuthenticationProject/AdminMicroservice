package com.dilillo.adminMicroservice.businessLayer.conversion

import com.dilillo.adminMicroservice.domainLayer.User
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserEntity

fun UserEntity.toUserModel(): User {
    return User(
        this.name!!,
        this.hashedPassword!!,
        this.email!!
    )
}