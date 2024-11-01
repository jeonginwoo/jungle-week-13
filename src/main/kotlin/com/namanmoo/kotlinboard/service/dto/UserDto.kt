package com.namanmoo.kotlinboard.service.dto

import com.namanmoo.kotlinboard.domain.entity.User
import java.io.Serializable
import java.time.LocalDateTime

class UserDto {

    data class Request(
        val nickname: String,
        val password: String
    ): Serializable

    data class Response(
        val userName: String,
        val nickname: String,
        val password: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime
    ): Serializable {
        companion object {
            fun toResponse(user: User) = Response(
                userName=user.userName,
                nickname=user.nickname,
                password=user.password,
                createdAt=user.createdAt,
                modifiedAt=user.modifiedAt
            )
        }
    }

    data class Login(
        val userName: String,
        val password: String
    ): Serializable

    data class SignUp(
        val userName: String,
        val nickname: String,
        val password: String
    ): Serializable {
        fun toUser() = User(userName=userName, nickname=nickname, password=password)
    }
}