package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.exception.InvalidInputException
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import com.namanmoo.kotlinboard.service.dto.UserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    fun userLogin(userRequest: UserDto.Request): Map<String, Any> {
        val user = findUser(userRequest.userName)
        if (!user.checkPassword(userRequest.password)) {
            throw InvalidInputException("패스워드가 일치하지 않습니다.")
        }
        return mapOf(
            "message" to "로그인 성공",
            "userName" to userRequest.userName
        )
    }

    fun userSignUp(userRequest: UserDto.Request): UserDto.Response {
        if (userRepository.existsById(userRequest.userName)) {
            throw InvalidInputException("(${userRequest.userName}) 이미 존재하는 사용자 이름입니다.")
        }
        val user = userRequest.toUser()
        return UserDto.Response.toResponse(userRepository.save(user))
    }

    @Transactional
    fun updateUser(userRequest: UserDto.Request, userName: String): UserDto.Response {
        val user = findUser(userName)
        user.updateUser(userRequest)
        return UserDto.Response.toResponse(user)
    }

    fun deleteUser(userName: String): Map<String, String> {
        userRepository.deleteById(userName)
        return mapOf(
            "message" to "회원 정보 삭제 성공",
            "userName" to userName
        )
    }
}