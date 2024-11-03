package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.exception.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.entity.Comment
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.CommentRepository
import com.namanmoo.kotlinboard.service.dto.CommentDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val articleService: ArticleService,
) {

    fun createComment(articleId: Long, commentRequest: CommentDto.Request): CommentDto.Response {
        val user = userService.getCurrentUser()
        val article = articleService.findById(articleId)
        val comment = commentRepository.save(commentRequest.toComment(article))
        return CommentDto.Response.toResponse(comment, user)
    }

    fun findById(id: Long): Comment{
        return commentRepository.findById(id)
            .orElseThrow{ NoSuchElementException("게시글(id: $id)을 찾을 수 없습니다.") }
    }

    fun findCommentsInArticle(articleId: Long): List<CommentDto.Response> {
        val user = userService.getCurrentUser()
        val commentList = commentRepository.findAllByArticleId(articleId)
        return commentList.map { CommentDto.Response.toResponse(it, user) }
    }

    fun findCommentsInUser(): List<CommentDto.Response> {
        val user = userService.getCurrentUser()
        val commentList = commentRepository.findAllByCreatedBy(user.userName)
        return commentList.map { CommentDto.Response.toResponse(it, user) }
    }

    @Transactional
    fun updateComment(commentId: Long, commentRequest: CommentDto.Request): CommentDto.Response {
        val comment = findById(commentId)
        val user = validateAndGetUser(comment)
        comment.updateComment(commentRequest)
        return CommentDto.Response.toResponse(comment, user)
    }

    fun deleteComment(commentId: Long): String {
        val comment = findById(commentId)
        validateAndGetUser(comment)
        commentRepository.delete(comment)
        return "댓글 삭제 성공"
    }

    fun validateAndGetUser(comment: Comment): User {
        val user = userService.getCurrentUser()
        if (user.role != ROLE.ADMIN && user.userName != comment.createdBy) {
            throw UserNotAuthorizedException("사용자가 이 댓글을 수정하거나 삭제할 권한이 없습니다.")
        }
        return user
    }
}