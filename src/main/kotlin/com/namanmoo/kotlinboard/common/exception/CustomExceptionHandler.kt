package com.namanmoo.kotlinboard.common.exception

import com.namanmoo.kotlinboard.common.dto.BaseResponse
import com.namanmoo.kotlinboard.common.exception.custom.InvalidInputException
import com.namanmoo.kotlinboard.common.exception.custom.JwtTokenException
import com.namanmoo.kotlinboard.common.exception.custom.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.status.ResultCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage?: "Not Exception Message"
        }

        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(JwtTokenException::class)
    protected fun jwtTokenException(ex: JwtTokenException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("잘못된 토큰" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotAuthorizedException::class)
    protected fun userNotAuthorizedException(ex: UserNotAuthorizedException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("유저 권한 없음" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun badCredentialsException(ex: BadCredentialsException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("로그인 실패" to "회원을 찾을 수 없습니다.")
        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("미처리 에러" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(HttpStatus.BAD_REQUEST.toString(), ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }
}