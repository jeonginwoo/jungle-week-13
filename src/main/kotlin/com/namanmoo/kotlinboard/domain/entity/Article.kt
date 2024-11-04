package com.namanmoo.kotlinboard.domain.entity

import com.namanmoo.kotlinboard.domain.BaseEntity
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
@Table(
    name = "article",
    indexes = [
        Index(columnList = "title"),
        Index(columnList = "createdBy"),
        Index(columnList = "createdAt")
    ]
)
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, length = 10000)
    var content: String
) : BaseEntity() {

    fun updateArticle(articleRequest: ArticleDto.Request) {
        this.title = articleRequest.title
        this.content = articleRequest.content
    }
}
