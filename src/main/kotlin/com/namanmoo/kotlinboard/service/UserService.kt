package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.authority.JwtTokenProvider
import com.namanmoo.kotlinboard.common.authority.TokenInfo
import com.namanmoo.kotlinboard.common.exception.custom.InvalidInputException
import com.namanmoo.kotlinboard.common.service.AuthorizeUserService
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import com.namanmoo.kotlinboard.service.dto.UserDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException


@Service
class UserService(
    private val userRepository: UserRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authorizeUserService: AuthorizeUserService
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
        val user = findById(authorizeUserService.getCurrentUsername())
        return UserDto.Response.toResponse(user)
    }

    fun signUp(userRequest: UserDto.Request): String {
        if (userRepository.existsById(userRequest.userName)) {
            throw InvalidInputException(message = "중복된 username 입니다.")
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
        val user = findById(authorizeUserService.getCurrentUsername())
        user.updateUser(userRequest)
        return UserDto.Response.toResponse(user)
    }

    fun deleteUser(): String {
        userRepository.deleteById(authorizeUserService.getCurrentUsername())
        return "회원 정보 삭제 성공"
    }
}