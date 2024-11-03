package com.namanmoo.kotlinboard.domain.entity

import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.BaseEntity
import com.namanmoo.kotlinboard.service.dto.UserDto
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @Column(unique = true, nullable = false)
    var userName: String,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @Column(nullable = false)
    var password: String,
): BaseEntity() {

    fun updateUser(userRequest: UserDto.Request) {
        this.nickname = userRequest.nickname
        this.password = userRequest.password
    }
}
//
//@Entity
//class UserRole(
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    val id: Long? = null,
//
//    @Column(nullable = false, length = 30)
//    @Enumerated(EnumType.STRING)
//    val role: ROLE,
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(foreignKey = ForeignKey(name = "fk_user_role_user_id"))
//    val user: User
//)