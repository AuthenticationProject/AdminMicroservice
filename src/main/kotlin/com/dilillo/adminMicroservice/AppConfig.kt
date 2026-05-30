package com.dilillo.adminMicroservice

import com.dilillo.adminMicroservice.businessLayer.AdminUseCase
import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.EncodingLogic
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.UserRepository
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.UserRepositoryGatewayImpl
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.utils.SimplePasswordGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(val encodingLogic: EncodingLogic) {

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val authenticationLogic: AuthenticationLogic? = null

    @Bean
    fun businessLogic(): BusinessLogic {
        if(this.userRepository == null)
            throw IllegalArgumentException("userRepository is null")
        if(this.authenticationLogic == null)
            throw IllegalArgumentException("authenticationLogic is null")
        return AdminUseCase(UserRepositoryGatewayImpl(userRepository), encodingLogic, authenticationLogic, SimplePasswordGenerator())
    }

}