package com.namanmoo.kotlinboard.config

import com.namanmoo.kotlinboard.common.service.AuthorizeUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val authorizeUserService: AuthorizeUserService,
) {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware {
            Optional.of(authorizeUserService.getCurrentUsername())
        }
    }
}