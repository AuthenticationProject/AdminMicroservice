package com.dilillo.adminMicroservice.interfaceAdaptersLayer.security

import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService: AuthenticationLogic {

    private val secretString = "b3f841b9cde790258db4d5d0ea7391c117bab968ed853b6cc70e4b43355ceb5a"
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray())
    private val expirationTimeInMs = 3600000 // 1 ora

    override fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTimeInMs)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    override fun extractUsername(token: String): String {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            claims.subject
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return (extractedUsername == username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims.expiration.before(Date())
    }
}