package com.namanmoo.kotlinboard.repository

import com.namanmoo.kotlinboard.domain.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<Article>
    fun findAllByCreatedByOrderByCreatedAtDesc(userName: String): List<Article>
    fun findAllByCreatedBy(userName: String): List<Article>
    fun deleteAllByCreatedBy(username: String)
}