package com.dilillo.adminMicroservice.interfaceAdaptersLayer.utils

import com.dilillo.adminMicroservice.businessLayer.boundaries.PasswordGeneratorLogic

class SimplePasswordGenerator: PasswordGeneratorLogic {
    override fun generatePassword(numChar: Int): String {
        val possibleChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..numChar)
            .map { possibleChars.random() }
            .joinToString("")

        return randomString
    }
}