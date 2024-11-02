package com.namanmoo.kotlinboard.common.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    userName: String,
    password: String,
    authorities: Collection<GrantedAuthority>
): User(userName, password, authorities) {
}