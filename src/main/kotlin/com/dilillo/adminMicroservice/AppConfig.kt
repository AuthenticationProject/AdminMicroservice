package com.dilillo.adminMicroservice

import com.dilillo.adminMicroservice.businessLayer.ShopUseCase
import com.dilillo.adminMicroservice.businessLayer.UserUseCase
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.EncodingLogic
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.ProductRepository
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.ProductRepositoryGatewayImpl
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
    private val productRepository: ProductRepository? = null

    @Autowired
    private val authenticationLogic: AuthenticationLogic? = null

    @Bean
    fun businessLogic(): BusinessLogic {
        if(this.userRepository == null)
            throw IllegalArgumentException("userRepository is null")
        if(this.authenticationLogic == null)
            throw IllegalArgumentException("authenticationLogic is null")
        return UserUseCase(UserRepositoryGatewayImpl(userRepository), encodingLogic, authenticationLogic, SimplePasswordGenerator())
    }

    @Bean
    fun adminBusinessLogic(): ShopBusinessLogic {
        if(this.productRepository == null)
            throw IllegalArgumentException("productRepository is null")
        return ShopUseCase(ProductRepositoryGatewayImpl(productRepository))
    }

}