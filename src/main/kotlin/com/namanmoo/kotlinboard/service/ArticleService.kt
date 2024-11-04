package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.service.AuthorizeUserService
import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.domain.entity.Comment
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import com.namanmoo.kotlinboard.repository.ArticleRepository
import com.namanmoo.kotlinboard.repository.CommentRepository
import com.namanmoo.kotlinboard.service.dto.CommentDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val authorizeUserService: AuthorizeUserService
) {

    fun createArticle(articleRequest: ArticleDto.Request): ArticleDto.Response {
        val article = articleRequest.toArticle()
        return ArticleDto.Response.toResponse(articleRepository.save(article))
    }

    fun findAllArticles(): List<ArticleDto.Response> {
        val articleList = articleRepository.findAllByOrderByCreatedAtDesc()
        return articleList.map{ ArticleDto.Response.toResponse(it)}
    }

    fun findArticlesInUser(): List<ArticleDto.Response> {
        val user = authorizeUserService.getCurrentUser()
        val articleList = articleRepository.findAllByCreatedByOrderByCreatedAtDesc(user.userName)
        return articleList.map{ ArticleDto.Response.toResponse(it)}
    }

    fun findById(id: Long): Article {
        return articleRepository.findById(id).orElseThrow{ NoSuchElementException("게시글(id: $id)을 찾을 수 없습니다.") }
    }

    fun findArticle(id: Long): ArticleDto.Response {
        return ArticleDto.Response.toResponse(findById(id))
    }

    // 게시글과 그 게시글의 댓글 및 대댓글을 포함한 정보 반환
    fun findArticleWithComments(id: Long): ArticleDto.ResponseWithComments {
        val article = findById(id)
        val comments = findAllCommentsInArticle(id)
        return ArticleDto.ResponseWithComments.toResponse(article, comments)

    } private fun findAllCommentsInArticle(articleId: Long): List<CommentDto.ResponseWithComments> {
        val topLevelComments = commentRepository.findAllByArticleIdAndParentCommentIdIsNullOrderByCreatedAtDesc(articleId)
        return topLevelComments.map { buildCommentWithReplies(it) }

    } private fun buildCommentWithReplies(comment: Comment): CommentDto.ResponseWithComments {
        val replies = commentRepository.findAllByParentCommentIdOrderByCreatedAtDesc(comment.id)
        val replyDtos = replies.map { buildCommentWithReplies(it) }
        return CommentDto.ResponseWithComments.toResponse(comment, replyDtos)
    }

    @Transactional
    fun updateArticle(articleRequest: ArticleDto.Request, articleId: Long): ArticleDto.Response {
        val article = findById(articleId)
        authorizeUserService.validateUser(article)
        article.updateArticle(articleRequest)
        return ArticleDto.Response.toResponse(article)
    }

    @Transactional
    fun deleteArticle(articleId: Long): String {
        val article = findById(articleId)
        authorizeUserService.validateUser(article)
        commentRepository.deleteAllByArticleId(articleId)
        articleRepository.deleteById(articleId)
        return "삭제 성공"
    }
}
