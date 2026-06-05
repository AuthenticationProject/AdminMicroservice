package com.dilillo.adminMicroservice.businessLayer.boundaries

/**
 * Random password generator interface
 */
interface PasswordGeneratorLogic {

    fun generatePassword(numChar: Int): String

}