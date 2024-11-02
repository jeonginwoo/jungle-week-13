package com.namanmoo.kotlinboard.config

import com.namanmoo.kotlinboard.common.autority.JwtAuthenticationFilter
import com.namanmoo.kotlinboard.common.autority.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity      // Spring Security 활성화
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean   // Configuration에 @Component가 있는데 왜 사용? -> 메서드의 반환값을 스프링 컨텍스트에 Bean으로 등록
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }     // 기본 인증 방식 비활성화 (JWT 사용을 위해)
            .csrf { it.disable() }          // CSRF 보호 비활성화 (토큰 인증으로 대체)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)} // 세션을 사용하지 않도록 설정 (JWT로 인증하기 때문)
            .authorizeHttpRequests {        // 요청 URL별 권한 설정
                it
                    .requestMatchers("/api/user/sign-up", "/api/user/login").anonymous() // 회원가입 요청은 인증 없이 접근 허용
                    .requestMatchers("/api/user/**").hasRole("MEMBER")
                    .anyRequest().permitAll()   // 그 외의 모든 요청은 인증 없이 접근 허용
            }
            .addFilterBefore(               // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()     // 설정 완료 후 SecurityFilterChain 반환
    }

    // 비밀번호 인코더를 생성하는 Bean, 다양한 암호화 방식 지원
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}