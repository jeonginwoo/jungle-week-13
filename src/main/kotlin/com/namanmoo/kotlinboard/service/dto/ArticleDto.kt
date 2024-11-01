package com.namanmoo.kotlinboard.service.dto

import com.namanmoo.kotlinboard.domain.entity.Article
import java.io.Serializable
import java.time.LocalDateTime

class ArticleDto {

    data class Request(
        val title: String,
        val content: String,
        val password: String
    ): Serializable {
        fun toArticle(): Article {
            return Article(title=title, content=content, password=password)
        }
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
                return Response(article.id, article.title, article.content, article.createdBy, article.createdAt, article.modifiedAt)
            }
        }
    }

    data class Password(
        val password: String
    ): Serializable
}