package com.namanmoo.kotlinboard.repository

import com.namanmoo.kotlinboard.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {
    fun findAllByArticleIdOrderByCreatedAtDesc(articleId: Long): List<Comment>
    fun findAllByArticleIdAndParentCommentIdIsNullOrderByCreatedAtDesc(articleId: Long): List<Comment>
    fun findAllByParentCommentIdOrderByCreatedAtDesc(parentCommentId: Long): List<Comment>
    fun findAllByCreatedByOrderByCreatedAtDesc(username: String): List<Comment>
    fun deleteAllByCreatedBy(username: String)
    fun deleteAllByArticleId(articleId: Long)
    fun deleteAllByParentCommentId(parentCommentId: Long)
}