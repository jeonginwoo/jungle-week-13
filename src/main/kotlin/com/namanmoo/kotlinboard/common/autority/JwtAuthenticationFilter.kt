package com.namanmoo.kotlinboard.common.autority

import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils

class JwtAuthenticationFilter (
    private val jwtTokenProvider: JwtTokenProvider  // 토큰 검증을 위한 JwtTokenProvider 주입
): GenericFilter() {
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        // HttpServletRequest로 변환 후, 토큰 추출
        val token = resolveToken(p0 as HttpServletRequest)

        // 토큰이 유효한 경우, 인증 정보 설정
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)          // 토큰에서 인증 정보 추출
            SecurityContextHolder.getContext().authentication = authentication      // 인증 정보를 SecurityContext에 저장
        }

        // 다음 필터로 요청 전달
        p2?.doFilter(p0, p1)
    }

    // 요청 헤더에서 토큰을 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        // Authorization 헤더에서 토큰 가져오기
        val bearerToken = request.getHeader("Authorization")

        // 토큰이 "Bearer"로 시작하는지 확인 후 "Bearer " 부분 제거하여 반환
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}