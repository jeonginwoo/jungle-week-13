package com.namanmoo.kotlinboard.common.exception.custom

class UserNotAuthorizedException(
    message: String = "Not authorized",
): RuntimeException(message)