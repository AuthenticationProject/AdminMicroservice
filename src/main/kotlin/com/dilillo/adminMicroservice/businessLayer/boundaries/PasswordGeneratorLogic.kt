package com.dilillo.adminMicroservice.businessLayer.boundaries

interface PasswordGeneratorLogic {

    fun generatePassword(numChar: Int): String

}