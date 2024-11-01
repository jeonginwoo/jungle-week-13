package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import com.namanmoo.kotlinboard.service.dto.UserDto
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun findAllUser(): List<UserDto.Response> {
        val userList = userRepository.findAll()
        return userList.map{ UserDto.Response.toResponse(it)}
    }

    fun findUser(userName: String): User {
        return userRepository.findById(userName)
            .orElseThrow{ NoSuchElementException("사용자($userName)를 찾을 수 없습니다.") }
    }

    fun userLogin(loginInfo: UserDto.Login): Map<String, Any> {
        val user = findUser(loginInfo.userName)

        if (!user.checkPassword(loginInfo.password)) {
            throw IllegalArgumentException("패스워드가 일치하지 않습니다.")
        }

        return mapOf(
            "message" to "로그인 성공",
            "userName" to loginInfo.userName
        )
    }

    fun userSignUp(signUpInfo: UserDto.SignUp): Map<String, Any> {

        if (userRepository.existsById(signUpInfo.userName)) {
            throw IllegalArgumentException("(${signUpInfo.userName}) 이미 존재하는 사용자 이름입니다.")
        }

        val user = signUpInfo.toUser()
        userRepository.save(user)
        return mapOf(
            "message" to "회원가입 성공",
            "userName" to signUpInfo.userName
        )
    }
}