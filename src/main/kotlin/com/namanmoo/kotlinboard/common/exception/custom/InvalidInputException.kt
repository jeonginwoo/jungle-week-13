package com.namanmoo.kotlinboard.common.exception.custom

class InvalidInputException (
    val fieldName: String = "",
    message: String = "Invalid input"
): RuntimeException(message)