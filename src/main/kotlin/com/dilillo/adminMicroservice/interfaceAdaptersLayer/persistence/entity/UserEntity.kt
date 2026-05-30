package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    var name: String? = null

    var email: String? = null

    var hashedPassword: String? = null

    var role: String? = null

    var hasTemporaryPassword = false

    fun build(name: String, email: String, password: String, role: String): UserEntity {
        this.name = name
        this.email = email
        this.role = role
        this.hashedPassword = password
        return this
    }

}