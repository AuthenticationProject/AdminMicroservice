package com.dilillo.adminMicroservice.businessLayer.boundaries

interface EncodingLogic {

    fun getHashedPassword(clearPassword: String): Result<String>

    fun checkMatch(passwordToCheck: String, encodedPassword: String): Boolean

}