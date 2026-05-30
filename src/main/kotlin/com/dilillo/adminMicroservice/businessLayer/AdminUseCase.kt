package com.dilillo.adminMicroservice.businessLayer

import com.dilillo.adminMicroservice.businessLayer.adapter.LoginRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.LoginResponse
import com.dilillo.adminMicroservice.businessLayer.adapter.RegisterRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.EncodingLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.PasswordGeneratorLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.UserRepositoryGateway
import com.dilillo.adminMicroservice.businessLayer.conversion.toUserModel
import com.dilillo.adminMicroservice.businessLayer.exception.IncorrectCredentialsException
import com.dilillo.adminMicroservice.businessLayer.exception.PasswordNotValidForRegistrationException
import com.dilillo.adminMicroservice.businessLayer.exception.UserAlreadyExistException
import com.dilillo.adminMicroservice.businessLayer.exception.UserNotFoundException
import com.dilillo.adminMicroservice.domainLayer.Role
import com.dilillo.adminMicroservice.domainLayer.User
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.UserEntity

class AdminUseCase(
    val userRepository: UserRepositoryGateway,
    val encodingLogic: EncodingLogic,
    val authenticationLogic: AuthenticationLogic,
    val passwordGeneratorLogic: PasswordGeneratorLogic
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
        val hashedPassword = this.hashPassword(registerRequest.password).getOrNull() ?: return Result.failure(
            PasswordNotValidForRegistrationException())
        val userEntity = this.userRepository.saveUser(UserEntity().build(
            registerRequest.username,
            registerRequest.email,
            hashedPassword,
            role.toString()
        ))
        return Result.success(userEntity.toUserModel())
    }

    override fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        val userRetrieved: Result<User> = this.getUserInfo(loginRequest.email)
        if(userRetrieved.isSuccess) {
            val passwordRetrieved = userRetrieved.getOrThrow().password
            val result = this.encodingLogic.checkMatch(
                loginRequest.password,
                passwordRetrieved
            )
            if(result) {
                val hasTempPassword = this.userRepository.hasTemporaryPassword(loginRequest.email)!!
                val token: String = authenticationLogic.generateToken(loginRequest.email)
                return Result.success(LoginResponse(token, hasTempPassword))
            } else {
                return Result.failure(IncorrectCredentialsException())
            }
        } else {
            return Result.failure(UserNotFoundException())
        }

    }

    override fun getUserInfo(email: String): Result<User> {
        val entity: UserEntity =
            userRepository.findByEmail(email).getOrNull() ?: return Result.failure(NoSuchElementException())

        return Result.success(User(
            entity.name!!,
            entity.hashedPassword!!,
            entity.email!!
        ))
    }

    override fun setTemporaryPassword(email: String): String {
        val tempPassword = this.passwordGeneratorLogic.generatePassword(5)
        val hashedPassword = this.hashPassword(tempPassword).getOrThrow()
        this.userRepository.setTemporaryPassword(email, hashedPassword)
        return tempPassword
    }

    override fun changePassword(email: String, newPassword: String): Boolean {
        val hashedPassword = this.hashPassword(newPassword).getOrThrow()
        return this.userRepository.changePassword(email, hashedPassword)
    }

    private fun hashPassword(password: String): Result<String> {
        val hashedPassword = this.encodingLogic.getHashedPassword(password).getOrNull() ?: return Result.failure(
                PasswordNotValidForRegistrationException()
            )
        return Result.success(hashedPassword)
    }
}