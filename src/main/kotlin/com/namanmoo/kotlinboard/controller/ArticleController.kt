package com.namanmoo.kotlinboard.controller

import com.namanmoo.kotlinboard.service.dto.ArticleDto
import com.namanmoo.kotlinboard.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/articles")
@RestController
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping("")
    fun articleList(): ResponseEntity<List<ArticleDto.Response>> {
        val response = articleService.findAllArticles()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/new-article")
    fun createArticle(
        @RequestBody articleRequest: ArticleDto.Request
    ): ResponseEntity<ArticleDto.Response> {
        val response = articleService.createArticle(articleRequest)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{article-id}")
    fun getArticle(
        @PathVariable("article-id") articleId: Long
    ): ResponseEntity<ArticleDto.Response> {
        val response = articleService.findArticle(articleId)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{article-id}")
    fun updateArticle(
        @RequestBody articleRequest: ArticleDto.Request,
        @PathVariable("article-id") articleId: Long
    ): ResponseEntity<ArticleDto.Response> {
        val response = articleService.updateArticle(articleRequest, articleId)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{article-id}")
    fun deleteArticle(
        @RequestBody password: ArticleDto.Password,
        @PathVariable("article-id") articleId: Long
    ): ResponseEntity<Map<String, Any>> {
        val response = articleService.deleteArticle(articleId, password)
        return ResponseEntity.ok(response)
    }
}