package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import com.namanmoo.kotlinboard.repository.ArticleRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class ArticleService(
    private val articleRepository: ArticleRepository
) {

    fun createArticle(articleRequest: ArticleDto.Request): ArticleDto.Response {
        val article = articleRequest.toArticle()
        return ArticleDto.Response.toResponse(articleRepository.save(article))
    }

    fun findAllArticles(): List<ArticleDto.Response> {
        val articleList = articleRepository.findAll(Sort.by("createdAt").descending())
        return articleList.map{ ArticleDto.Response.toResponse(it)}
    }

    fun findById(id: Long): Article {
        return articleRepository.findById(id)
            .orElseThrow{NoSuchElementException("게시글(id: $id)을 찾을 수 없습니다.")}
    }

    fun findArticle(id: Long): ArticleDto.Response {
        return ArticleDto.Response.toResponse(findById(id))
    }

    @Transactional
    fun updateArticle(articleRequest: ArticleDto.Request, articleId: Long): ArticleDto.Response {
        val article = findById(articleId)
        article.updateArticle(articleRequest)
        return ArticleDto.Response.toResponse(article)
    }

    fun deleteArticle(articleId: Long): String {
        val article = findById(articleId)
        articleRepository.deleteById(articleId)
        return "삭제 성공"
    }
}
