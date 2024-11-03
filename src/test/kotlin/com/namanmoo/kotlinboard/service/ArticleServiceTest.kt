package com.namanmoo.kotlinboard.service

import com.namanmoo.kotlinboard.common.exception.UserNotAuthorizedException
import com.namanmoo.kotlinboard.common.status.ROLE
import com.namanmoo.kotlinboard.domain.entity.Article
import com.namanmoo.kotlinboard.domain.entity.User
import com.namanmoo.kotlinboard.repository.ArticleRepository
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class ArticleServiceTest {
    private lateinit var articleService: ArticleService
    private lateinit var articleRepository: ArticleRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        articleRepository = mock(ArticleRepository::class.java)
        userService = mock(UserService::class.java)
        articleService = ArticleService(articleRepository, userService)
    }

    @Test
    fun `게시글 생성 성공 테스트`() {
        // given
        val articleRequest = ArticleDto.Request(
            title = "Test Title",
            content = "Test Content"
        )

        val article = Article(
            title = "Test Title",
            content = "Test Content"
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

        val article = Article(
            title = "Test Title",
            content = "Test Content"
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
        val articleList = listOf(
            Article(title = "Test1 Title", content = "Test1 Content"),
            Article(title = "Test2 Title", content = "Test2 Content")
        )

        `when`(articleRepository.findAllByOrderByCreatedAtDesc()).thenReturn(articleList)

        // when
        val response = articleService.findAllArticles()

        // then
        assertEquals(response.size, articleList.size)
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
            content = "Update Content"
        )
        val articleId = 1L

        val article = Article(
            title = "Test Title",
            content = "Test Content",
            createdBy = "user1"
        )

        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))
        `when`(userService.getCurrentUser()).thenReturn(User(userName = "user1", nickname = "nickname", role = ROLE.USER, password = "password"))

        // when
        val response = articleService.updateArticle(articleRequest, articleId)

        // then
        verify(articleRepository).findById(articleId)
        assertEquals(response.title, articleRequest.title)
        assertEquals(response.content, articleRequest.content)
    }

    @Test
    fun `게시글 수정 실패 테스트 - 권한 없음`() {
        // given
        val articleRequest = ArticleDto.Request(
            title = "Update Title",
            content = "Update Content"
        )
        val articleId = 1L

        val article = Article(
            title = "Test Title",
            content = "Test Content",
            createdBy = "user1"
        )

        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))
        `when`(userService.getCurrentUser()).thenReturn(User(userName = "user2", nickname = "nickname", role = ROLE.USER, password = "password"))

        // when, then
        assertThrows<UserNotAuthorizedException> {
            articleService.updateArticle(articleRequest, articleId)
        }
    }

    @Test
    fun `게시글 삭제 성공 테스트`() {
        // given
        val id = 1L

        val article = Article(
            title = "Test Title",
            content = "Test Content",
            createdBy = "user1"
        )

        `when`(articleRepository.findById(id)).thenReturn(Optional.of(article))
        `when`(userService.getCurrentUser()).thenReturn(User(userName = "user1", nickname = "nickname", role = ROLE.USER, password = "password"))

        // when
        articleService.deleteArticle(id)

        // then
        verify(articleRepository).deleteById(id)
    }

    @Test
    fun `게시글 삭제 실패 테스트 - 권한 없음`() {
        // given
        val id = 1L

        val article = Article(
            title = "Test Title",
            content = "Test Content",
            createdBy = "user1"
        )

        `when`(articleRepository.findById(id)).thenReturn(Optional.of(article))
        `when`(userService.getCurrentUser()).thenReturn(User(userName = "user2", nickname = "nickname", role = ROLE.USER, password = "password"))

        // when, then
        assertThrows<UserNotAuthorizedException> {
            articleService.deleteArticle(id)
        }
    }
}
