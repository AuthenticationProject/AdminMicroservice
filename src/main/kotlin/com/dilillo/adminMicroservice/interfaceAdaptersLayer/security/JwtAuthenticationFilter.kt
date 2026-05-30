package com.dilillo.adminMicroservice.interfaceAdaptersLayer.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter


class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val username = jwtService.extractUsername(token)

            if (SecurityContextHolder.getContext().authentication == null) {
                if (jwtService.validateToken(token, username)) {
                    val userDetails = User.withUsername(username).password("").roles("ADMIN").build()

                    val role: String? = jwtService.extractRole(token)

                    val authority: ArrayList<SimpleGrantedAuthority> = ArrayList()
                    authority.add(SimpleGrantedAuthority("ROLE_$role"))

                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, authority
                    )

                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}