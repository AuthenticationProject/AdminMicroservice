package com.dilillo.adminMicroservice.interfaceAdaptersLayer.security.encoding

import com.dilillo.adminMicroservice.businessLayer.boundaries.EncodingLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EncodingService: EncodingLogic {

    @Autowired
    private var passwordEncoder: PasswordEncoder? = null

    override fun getHashedPassword(clearPassword: String): Result<String> {
        val passwordHashata =
            passwordEncoder!!.encode(clearPassword) ?: return Result.failure(Exception("Error while hashing password"))
        return Result.success(passwordHashata)
    }

    override fun checkMatch(passwordToCheck: String, encodedPassword: String): Boolean {
        return passwordEncoder?.matches(passwordToCheck, encodedPassword) ?: false;
    }

}