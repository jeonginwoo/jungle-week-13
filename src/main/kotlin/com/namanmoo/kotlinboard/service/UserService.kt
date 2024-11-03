package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.autority.JwtTokenProvider
import com.namanmoo.kotlinboard.common.autority.TokenInfo
import com.namanmoo.kotlinboard.common.dto.CustomUser
import com.namanmoo.kotlinboard.common.exception.InvalidInputException
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import com.namanmoo.kotlinboard.service.dto.UserDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun findAllUser(): List<UserDto.Response> {
        val userList = userRepository.findAll()
        return userList.map{ UserDto.Response.toResponse(it)}
    }

    fun findById(userName: String): User {
        return userRepository.findById(userName)
            .orElseThrow{ NoSuchElementException("사용자($userName)를 찾을 수 없습니다.") }
    }

    fun findUser(): UserDto.Response {
        val user = findById(getCurrentUsername())
        return UserDto.Response.toResponse(user)
    }

    fun signUp(userRequest: UserDto.Request): String {
        if (userRepository.existsById(userRequest.userName)) {
            throw InvalidInputException("(${userRequest.userName}) 이미 존재하는 사용자 이름입니다.")
        }

        val user = userRequest.toUser()
        userRepository.save(user)

        return "회원 가입 성공"
    }

    fun login(userRequest: UserDto.Login): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(userRequest.userName, userRequest.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    @Transactional
    fun updateUser(userRequest: UserDto.Request): UserDto.Response {
        val user = findById(getCurrentUsername())
        user.updateUser(userRequest)
        return UserDto.Response.toResponse(user)
    }

    fun deleteUser(): String {
        userRepository.deleteById(getCurrentUsername())
        return "회원 정보 삭제 성공"
    }

//    fun getCurrentUsername(): String {
//        val userName = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
//        return userName
//    }

    fun getCurrentUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal
        return when (principal) {
            is CustomUser -> principal.username
            is String -> principal // principal이 String일 경우 그대로 반환
            else -> throw RuntimeException("User not authenticated or principal is not a recognized type")
        }
    }

    fun getCurrentUser(): User {
        return findById(getCurrentUsername())
    }
}