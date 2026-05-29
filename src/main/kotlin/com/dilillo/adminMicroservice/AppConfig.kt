package com.dilillo.adminMicroservice

import com.dilillo.adminMicroservice.businessLayer.AdminUseCase
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Autowired
    private val userRepository: UserRepository? = null

    @Bean
    fun businessLogic(): BusinessLogic {
        if(this.userRepository == null)
            throw IllegalArgumentException("userRepository is null")
        return AdminUseCase(userRepository)
    }

}