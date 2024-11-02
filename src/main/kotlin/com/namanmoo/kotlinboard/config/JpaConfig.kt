package com.namanmoo.kotlinboard.config

import com.namanmoo.kotlinboard.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val userService: UserService,
) {
//    @Bean
//    fun auditorAware(): AuditorAware<String> {
//        return AuditorAware {
//            Optional.of(userService.getCurrentUsername())
//        }
//    }
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware {
            val username = runCatching { userService.getCurrentUsername() }.getOrNull()
            Optional.ofNullable(username) // 인증되지 않은 경우 null 반환
        }
    }
}