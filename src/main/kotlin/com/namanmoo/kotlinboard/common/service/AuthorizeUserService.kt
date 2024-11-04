package com.namanmoo.kotlinboard.common.service

import com.namanmoo.kotlinboard.common.dto.CustomUser
import com.namanmoo.kotlinboard.common.exception.custom.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.BaseEntity
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class AuthorizeUserService(
    private val userRepository: UserRepository
) {

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
        val userName = getCurrentUsername()
        return userRepository.findById(getCurrentUsername()).orElseThrow{ NoSuchElementException("사용자($userName)를 찾을 수 없습니다.") }
    }

    fun <T : BaseEntity> validateUser(entity: T) {
        val user = getCurrentUser()
        if (user.role != ROLE.ADMIN && user.userName != entity.createdBy) {
            throw UserNotAuthorizedException("작성자만 삭제/수정할 수 있습니다.")
        }
    }
}