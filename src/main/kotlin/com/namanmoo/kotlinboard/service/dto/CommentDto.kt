package com.namanmoo.kotlinboard.service.dto

import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.domain.entity.Comment
import com.namanmoo.kotlinboard.domain.entity.User
import java.io.Serializable
import java.time.LocalDateTime

class CommentDto {

    data class Request(
        val content: String
    ): Serializable {
        fun toComment(article: Article): Comment {
            return Comment(content=this.content, articleId=article.id)
        }
    }

    data class Response(
        val commentId: Long,
        val content: String,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime,
    ): Serializable {
        companion object {
            fun toResponse(comment: Comment): Response {
                return Response(
                    commentId = comment.id,
                    content = comment.content,
                    createdBy = comment.createdBy,
                    createdAt = comment.createdAt,
                    modifiedAt = comment.modifiedAt
                )
            }
        }
    }

    data class ResponseWithComments(
        val commentId: Long,
        val content: String,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val modifiedAt: LocalDateTime,
        val comments: List<ResponseWithComments>
    ): Serializable {
        companion object {
            fun toResponse(comment: Comment, comments: List<ResponseWithComments>): ResponseWithComments {
                return ResponseWithComments(
                    commentId = comment.id,
                    content = comment.content,
                    createdBy = comment.createdBy,
                    createdAt = comment.createdAt,
                    modifiedAt = comment.modifiedAt,
                    comments = comments
                )
            }
        }
    }
}