package com.namanmoo.kotlinboard.common.exception

class UserNotAuthorizedException(
    message: String = "Not Authorized",
): RuntimeException(message)