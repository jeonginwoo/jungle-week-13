package com.namanmoo.kotlinboard.common.exception.custom

class JwtTokenException(
    message: String = "Invalid token",
): RuntimeException(message)