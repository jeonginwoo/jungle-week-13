package com.namanmoo.kotlinboard.service.dto

import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.service.dto.CommentDto.ResponseWithComments
import java.io.Serializable
import java.time.LocalDateTime

class ArticleDto {

    data class Request(
        val title: String,
        val content: String
    ): Serializable {
        fun toArticle() = Article(
            title = title,
            content = content
        )
    }

    data class Response(
        val id: Long,
        val title: String,
        val content: String,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime
    ): Serializable {
        companion object {
            fun toResponse(article: Article): Response {
                return Response(
                    article.id,
                    article.title,
                    article.content,
                    article.createdBy,
                    article.createdAt,
                    article.modifiedAt)
            }
        }
    }

    data class ResponseWithComments(
        val id: Long,
        val title: String,
        val content: String,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime,
        val comments: List<CommentDto.ResponseWithComments>
    ): Serializable {
        companion object {
            fun toResponse(article: Article, comments: List<CommentDto.ResponseWithComments>): ResponseWithComments {
                return ResponseWithComments(
                    id = article.id,
                    title = article.title,
                    content = article.content,
                    createdBy = article.createdBy,
                    createdAt = article.createdAt,
                    modifiedAt = article.modifiedAt,
                    comments = comments
                )
            }
        }
    }
}