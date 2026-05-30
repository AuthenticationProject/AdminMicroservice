package com.dilillo.adminMicroservice.businessLayer

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.EncodingLogic
import com.dilillo.adminMicroservice.businessLayer.conversion.toUserModel
import com.dilillo.adminMicroservice.businessLayer.exception.IncorrectCredentialsException
import com.dilillo.adminMicroservice.businessLayer.exception.PasswordNotValidForRegistrationException
import com.dilillo.adminMicroservice.businessLayer.exception.UserAlreadyExistException
import com.dilillo.adminMicroservice.businessLayer.exception.UserNotFoundException
import com.dilillo.adminMicroservice.domainLayer.Role
import com.dilillo.adminMicroservice.domainLayer.User
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.dto.UserEntity
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository.UserRepository

class AdminUseCase(
    val userRepository: UserRepository,
    val encodingLogic: EncodingLogic,
    val authenticationLogic: AuthenticationLogic
): BusinessLogic {
    override fun registerUser(registerRequest: RegisterRequest): Result<User> {
        return this.registration(registerRequest, Role.USER)
    }

    override fun registerAdmin(registerRequest: RegisterRequest): Result<User> {
        return this.registration(registerRequest, Role.ADMIN)
    }

    private fun registration(registerRequest: RegisterRequest, role: Role): Result<User> {

        if(this.getUserInfo(registerRequest.email).isSuccess) {
            return Result.failure(UserAlreadyExistException())
        }
        val hashedPassword =
            this.encodingLogic.getHashedPassword(registerRequest.password).getOrNull() ?: return Result.failure(
                PasswordNotValidForRegistrationException()
            )
        val userEntity = this.userRepository.save(UserEntity().build(
            registerRequest.username,
            registerRequest.email,
            hashedPassword,
            role.toString()
        ))
        return Result.success(userEntity.toUserModel())
    }

    override fun login(loginRequest: LoginRequest): Result<String> {
        val userRetrieved: Result<User> = this.getUserInfo(loginRequest.email)
        if(userRetrieved.isSuccess) {
            val passwordRetrieved = userRetrieved.getOrThrow().password
            val result = this.encodingLogic.checkMatch(
                loginRequest.password,
                passwordRetrieved
            )
            if(result) {
                return Result.success(authenticationLogic.generateToken(loginRequest.email))
            } else {
                return Result.failure(IncorrectCredentialsException())
            }
        } else {
            return Result.failure(UserNotFoundException())
        }

    }

    override fun getUserInfo(email: String): Result<User> {
        val entity: UserEntity? = userRepository.findByEmail(email)
            ?.orElseGet { null }

        if(entity == null)
            return Result.failure(NoSuchElementException())
        return Result.success(User(
            entity.name!!,
            entity.hashedPassword!!,
            entity.email!!
        ))
    }
}