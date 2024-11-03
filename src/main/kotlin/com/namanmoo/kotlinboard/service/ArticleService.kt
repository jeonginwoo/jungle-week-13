package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.exception.custom.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import com.namanmoo.kotlinboard.repository.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userService: UserService,
    private val commentService: CommentService
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
        val user = userService.getCurrentUser()
        val articleList = articleRepository.findAllByCreatedByOrderByCreatedAtDesc(user.userName)
        return articleList.map{ ArticleDto.Response.toResponse(it)}
    }

    fun findById(id: Long): Article {
        return articleRepository.findById(id)
            .orElseThrow{ NoSuchElementException("게시글(id: $id)을 찾을 수 없습니다.") }
    }

    fun findArticle(id: Long): ArticleDto.Response {
        return ArticleDto.Response.toResponse(findById(id))
    }

    // 게시글과 그 게시글의 댓글 및 대댓글을 포함한 정보 반환
    fun findArticleWithComments(id: Long): ArticleDto.ResponseWithComments {
        val article = findById(id)
        val comments = commentService.findAllCommentsInArticle(id)
        return ArticleDto.ResponseWithComments.toResponse(article, comments)
    }

    @Transactional
    fun updateArticle(articleRequest: ArticleDto.Request, articleId: Long): ArticleDto.Response {
        val article = findById(articleId)
        validateUser(article)
        article.updateArticle(articleRequest)
        return ArticleDto.Response.toResponse(article)
    }

    fun deleteArticle(articleId: Long): String {
        val article = findById(articleId)
        validateUser(article)
        articleRepository.deleteById(articleId)
        return "삭제 성공"
    }

    fun validateUser(article: Article) {
        val user = userService.getCurrentUser()
        if (user.role != ROLE.ADMIN && user.userName != article.createdBy) {
            throw UserNotAuthorizedException("작성자만 삭제/수정할 수 있습니다.")
        }
    }
}
