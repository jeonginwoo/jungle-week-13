package com.namanmoo.kotlinboard.repository

import com.namanmoo.kotlinboard.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
    fun findAllByArticleId(articleId: Long): List<Comment>
    fun findAllByCreatedBy(username: String): List<Comment>
}