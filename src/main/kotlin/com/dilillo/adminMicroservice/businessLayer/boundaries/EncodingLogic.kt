package com.dilillo.adminMicroservice.businessLayer.boundaries

/**
 * Encoding logic for passwords
 */
interface EncodingLogic {

    fun getHashedPassword(clearPassword: String): Result<String>

    fun checkMatch(passwordToCheck: String, encodedPassword: String): Boolean

}