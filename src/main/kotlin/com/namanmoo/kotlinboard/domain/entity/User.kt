package com.namanmoo.kotlinboard.domain.entity

import com.namanmoo.kotlinboard.service.dto.UserDto
import jakarta.persistence.*

@Entity
@Table(
    name = "user",
    indexes = [
        Index(columnList = "userName")
    ]
)
class User(
    @Id
    @Column(unique = true, nullable = false)
    var userName: String,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false)
    var password: String,
): BaseEntity() {

    fun checkPassword(password: String) = this.password == password

    fun updateUser(userRequest: UserDto.Request) {
        this.nickname = userRequest.nickname
        this.password = userRequest.password
    }
}