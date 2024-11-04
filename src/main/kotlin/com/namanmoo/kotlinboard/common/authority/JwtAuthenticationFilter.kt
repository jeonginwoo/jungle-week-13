package com.namanmoo.kotlinboard.common.authority

import com.namanmoo.kotlinboard.common.exception.custom.JwtTokenException
import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import java.io.IOException

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider  // 토큰 검증을 위한 JwtTokenProvider 주입
) : GenericFilter() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

//        try {
            // 토큰을 추출
            val token = resolveToken(httpServletRequest)

            // 토큰이 존재하고 유효한지 확인
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 인증 정보를 추출하여 SecurityContext에 설정
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
//            } else {
//                throw JwtTokenException("토큰이 유효하지 않습니다.")
//            }
//        } catch (ex: JwtTokenException) {
//            // JWT 토큰 관련 예외가 발생한 경우 처리
//            handleException(httpServletResponse, ex)
//            return  // 예외가 발생하면 체인 실행을 중단합니다.
//        }

        // 다음 필터로 요청을 전달
        chain?.doFilter(request, response)
    }

    // 요청 헤더에서 토큰을 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")

        // Bearer 토큰을 확인하고 'Bearer ' 부분을 제거한 후 반환
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }

    // 예외 처리 메서드: statusCode와 error 메시지를 JSON 형식으로 반환
    private fun handleException(response: HttpServletResponse, ex: JwtTokenException) {
        response.status = HttpServletResponse.SC_BAD_REQUEST  // 400 Bad Request 상태 코드 반환
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        try {
            // 상태 코드와 에러 메시지를 포함한 JSON 응답 작성
            val jsonResponse = """
                {
                    "statusCode": "${HttpStatus.BAD_REQUEST}",
                    "error": "${ex.message}"
                }
            """.trimIndent()

            response.writer.write(jsonResponse)  // JSON 응답 반환
        } catch (ioEx: IOException) {
            ioEx.printStackTrace()
        }
    }
}
