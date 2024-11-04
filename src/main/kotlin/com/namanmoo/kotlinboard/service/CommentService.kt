package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.exception.custom.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.service.AuthorizeUserService
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.entity.Comment
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.ArticleRepository
import com.namanmoo.kotlinboard.repository.CommentRepository
import com.namanmoo.kotlinboard.service.dto.CommentDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val authorizeUserService: AuthorizeUserService
) {

    fun createComment(articleId: Long, commentRequest: CommentDto.Request): CommentDto.Response {
        val article = articleRepository.findById(articleId).orElseThrow{ NoSuchElementException("게시글(id: $articleId)을 찾을 수 없습니다.") }
        val comment = commentRepository.save(commentRequest.toComment(article))
        return CommentDto.Response.toResponse(comment)
    }

    fun findById(id: Long): Comment{
        return commentRepository.findById(id)
            .orElseThrow{ NoSuchElementException("게시글(id: $id)을 찾을 수 없습니다.") }
    }

    fun findCommentsInArticle(articleId: Long): List<CommentDto.Response> {
        val commentList = commentRepository.findAllByArticleId(articleId)
        return commentList.map { CommentDto.Response.toResponse(it) }
    }

    fun findCommentsInParentComment(commentId: Long): List<CommentDto.Response> {
        val commentList = commentRepository.findAllByParentCommentId(commentId)
        return commentList.map { CommentDto.Response.toResponse(it) }
    }

    fun findCommentsInUser(): List<CommentDto.Response> {
        val user = authorizeUserService.getCurrentUser()
        val commentList = commentRepository.findAllByCreatedBy(user.userName)
        return commentList.map { CommentDto.Response.toResponse(it) }
    }

    @Transactional
    fun updateComment(commentId: Long, commentRequest: CommentDto.Request): CommentDto.Response {
        val comment = findById(commentId)
        comment.updateComment(commentRequest)
        return CommentDto.Response.toResponse(comment)
    }

    fun deleteComment(commentId: Long): String {
        val comment = findById(commentId)
        authorizeUserService.validateUser(comment)
        commentRepository.delete(comment)
        return "댓글 삭제 성공"
    }
}