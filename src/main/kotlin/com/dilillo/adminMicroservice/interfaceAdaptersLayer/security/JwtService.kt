package com.dilillo.adminMicroservice.interfaceAdaptersLayer.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secretString = "laTuaChiaveSegretaMoltoLungaEComplessaCheSoddisfiIRequisitiDiBitDiHmac"
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray())
    private val expirationTimeInMs = 3600000 // 1 ora

    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTimeInMs)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            claims.subject
        } catch (e: Exception) {
            null
        }
    }

    fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return (extractedUsername == username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims.expiration.before(Date())
    }
}