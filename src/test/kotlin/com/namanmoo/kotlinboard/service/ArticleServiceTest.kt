package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.repository.ArticleRepository
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.data.domain.Sort
import java.util.*

class ArticleServiceTest {
    private lateinit var articleService: ArticleService
    private lateinit var articleRepository: ArticleRepository

    @BeforeEach
    fun setUp() {
        articleRepository = mock()
        articleService = ArticleService(articleRepository)
    }

    @Test
    fun `게시글 생성 성공 테스트`() {
        // given
        val articleRequest = ArticleDto.Request(
            title = "Test Title",
            content = "Test Content",
            password = "1234",
        )

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234",
        )

        `when`(articleRepository.save(any(Article::class.java))).thenReturn(article)

        // when
        val resultResponse = articleService.createArticle(articleRequest)

        // then
        assertEquals(resultResponse.title, articleRequest.title)
        assertEquals(resultResponse.content, articleRequest.content)
    }

    @Test
    fun `단일 게시글 조회 성공 테스트`() {
        // given
        val id = 1L

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234"
        )

        `when`(articleRepository.findById(id)).thenReturn(Optional.of(article))

        // when
        val response = articleService.findArticle(id)

        // then
        assertEquals(response.title, article.title)
        assertEquals(response.content, article.content)
    }

    @Test
    fun `전체 게시글 조회 성공 테스트`() {
        // given
        val articleList = mutableListOf<Article>()
        for (i in 1..3) {
            articleList.add(
                Article.of(
                    title = "Test${i} Title",
                    content = "Test${i} Content",
                    password = "1234"
                )
            )
        }


        `when`(articleRepository.findAll(Sort.by("createdAt").descending())).thenReturn(articleList)

        // when
        val response = articleService.findAllArticles()

        // then
        for (i in articleList.indices) {
            assertEquals(response[i].title, articleList[i].title)
            assertEquals(response[i].content, articleList[i].content)
        }
    }

    @Test
    fun `게시글 수정 성공 테스트`() {
        // given
        val articleRequest = ArticleDto.Request(
            title = "Update Title",
            content = "Update Content",
            password = "1234"
        )
        val articleId = 1L

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234"
        )

        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // when
        val response = articleService.updateArticle(articleRequest, articleId)

        // then
        verify(articleRepository).findById(articleId)
        assertEquals(response.title, articleRequest.title)
        assertEquals(response.content, articleRequest.content)
    }

    @Test
    fun `게시글 수정 실패 테스트`() {
        // given
        val articleRequest = ArticleDto.Request(
            title = "Update Title",
            content = "Update Content",
            password = "1111"
        )
        val articleId = 1L

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234"
        )

        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // when, then
        assertThrows<IllegalArgumentException> {
            articleService.updateArticle(articleRequest, articleId)
        }
    }

    @Test
    fun `게시글 삭제 성공 테스트`() {
        // given
        val id = 1L
        val password = ArticleDto.Password("1234")

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234"
        )

        `when`(articleRepository.findById(id)).thenReturn(Optional.of(article))

        //when
        articleService.deleteArticle(id, password)

        //then
        verify(articleRepository).deleteById(id)
    }

    @Test
    fun `게시글 삭제 실패 테스트`() {
        // given
        val id = 1L
        val password = ArticleDto.Password("1111")

        val article = Article.of(
            title = "Test Title",
            content = "Test Content",
            password = "1234"
        )

        `when`(articleRepository.findById(id)).thenReturn(Optional.of(article))

        // when, then
        assertThrows<IllegalArgumentException> {
            articleService.deleteArticle(id, password)
        }
    }
}