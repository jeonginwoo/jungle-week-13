package com.namanmoo.kotlinboard.common.service

import com.namanmoo.kotlinboard.common.dto.CustomUser
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findById(username)
            .map { createUserDetails(it) }
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }

    private fun createUserDetails(user: User): UserDetails =
        CustomUser(
            user.userName,
            passwordEncoder.encode(user.password),
            listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
        )
}