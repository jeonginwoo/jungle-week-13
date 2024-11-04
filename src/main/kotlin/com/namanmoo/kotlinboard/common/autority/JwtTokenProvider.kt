package com.namanmoo.kotlinboard.common.autority

import com.namanmoo.kotlinboard.common.dto.CustomUser
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30

@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    /**
     * Token 생성
     */
    fun createToken(authentication: Authentication): TokenInfo {
        val authorities: String = authentication
            .authorities
            .joinToString(",") { it.authority }

        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

        // Access Token 생성
        val accessToken = Jwts
            .builder()
            /* payload */
            .setSubject(authentication.name)            // 토큰 소유자 설정
            .claim("auth", authorities)                 // 권한 정보 추가
            .claim("userName", (authentication.principal as CustomUser).username)
            .setIssuedAt(now)                           // 토큰 발행 시간 설정
            .setExpiration(accessExpiration)            // 토큰 만료 시간 설정
            /* header, signature */
            .signWith(key, SignatureAlgorithm.HS256)    // 서명 알고리즘 및 키 설정
            .compact()                                  // 토큰 생성 및 문자열로 변환

        return TokenInfo("Bearer", accessToken)
    }

    /**
     *  Token에서 Authentication 정보 추출
     */
    fun getAuthentication(token: String): Authentication {
        // 토큰의 클레임(Claims)을 가져옴
        val claims: Claims = getClaims(token)

        // 클레임에서 권한 정보 추출, 없으면 예외 발생
        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val userName = claims["userName"] ?: throw RuntimeException("잘못된 토큰입니다.")

        // 권한 문자열을 SimpleGrantedAuthority로 변환하여 Collection으로 생성
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map{ SimpleGrantedAuthority(it) }

        // 사용자 정보 생성
        val principal: UserDetails = CustomUser(claims.subject, "", authorities)

        // UsernamePasswordAuthenticationToken 생성 및 반환
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    /**
     * Token 검증
     */
    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: SecurityException) {
            println("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            println("Invalid JWT token.")
        } catch (e: ExpiredJwtException) {
            println("Expired JWT token.")
        } catch (e: UnsupportedJwtException) {
            println("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            println("JWT claims string is empty.")
        } catch (e: Exception) {
            println("JWT claims string is wrong.")
        }
        return false
    }

    private fun getClaims(token: String): Claims =
        Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}