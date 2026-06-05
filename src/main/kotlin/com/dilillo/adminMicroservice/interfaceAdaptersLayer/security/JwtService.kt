package com.dilillo.adminMicroservice.interfaceAdaptersLayer.security

import com.dilillo.adminMicroservice.businessLayer.boundaries.AuthenticationLogic
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

/**
 * Jwt service for token generation, verification and validation
 */
@Service
class JwtService: AuthenticationLogic {

    // secret key generated with openssl rand -hex 32 command in bash prompt
    private val secretString = "b3f841b9cde790258db4d5d0ea7391c117bab968ed853b6cc70e4b43355ceb5a"
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray())
    private val expirationTimeInMs = 3600000 // 1 ora

    override fun generateToken(username: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationTimeInMs)

        return Jwts.builder()
            .subject(username)
            .claim("role", role)
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

    override fun extractRole(token: String): String {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            claims["role"].toString()
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