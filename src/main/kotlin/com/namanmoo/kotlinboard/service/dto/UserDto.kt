package com.namanmoo.kotlinboard.service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.entity.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.io.Serializable
import java.time.LocalDateTime

class UserDto {

    data class Request(
        @JsonProperty("userName")
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}\$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)"
        )
        private val _userName: String?,

        @JsonProperty("nickname")
        @field:Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{1,20}\$",
            message = "최소 1자 이상, 20자 이하이며 한글(가~힣), 알파벳 대소문자(a~z, A~Z), 숫자(0~9)"
        )
        private val _nickname: String?,

        @JsonProperty("password")
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])[a-zA-Z0-9[^a-zA-Z0-9]]{8,15}\$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자"
        )
        private val _password: String?
    ): Serializable {
        val userName: String
            get() = _userName!!
        val nickname: String
            get() = _nickname!!
        val password: String
            get() = _password!!

        fun toUser() = User(
            userName = userName,
            nickname = nickname,
            role = ROLE.USER,
            password = password
        )
    }

    data class Response(
        val userName: String,
        val nickname: String,
        val userRole: String,
//        val password: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime
    ): Serializable {
        companion object {
            fun toResponse(user: User) = Response(
                userName = user.userName,
                nickname = user.nickname,
                userRole = user.role.toString(),
//                password = user.password,
                createdAt = user.createdAt,
                modifiedAt = user.modifiedAt
            )
        }
    }

    data class Login(
        @field:NotBlank
        @JsonProperty("userName")
        private val _userName: String?,

        @field:NotBlank
        @JsonProperty("password")
        private val _password: String?
    ): Serializable {
        val userName: String
            get() = _userName!!
        val password: String
            get() = _password!!
    }

//    data class SignUp(
//        val userName: String,
//        val nickname: String,
//        val password: String
//    ): Serializable {
//        fun toUser() = User(userName=userName, nickname=nickname, password=password)
//    }
}