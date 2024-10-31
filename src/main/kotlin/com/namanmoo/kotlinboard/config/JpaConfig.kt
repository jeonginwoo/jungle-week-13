package com.namanmoo.kotlinboard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("jiw413") } // TODO: 스프링 사큐리티로 인증 기능을 붙이게 될 때 수정
    }
}