package com.namanmoo.kotlinboard.domain.entity

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
    var content: String,

    @Column(nullable = false)
    var password: String,

    @CreatedBy
    @Column(nullable = false)
    var createdBy: String = ""
) : BaseEntity() {

    companion object {
        fun of(title: String, content: String, password: String): Article = Article(title=title, content=content, password=password)
    }

    protected constructor() : this(title="", content="", password="") // JPA 기본 생성자

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Article) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + (id.hashCode() ?: 0)
        return result
    }

    fun checkPassword(password: String): Boolean = this.password == password

    fun updateArticle(articleRequest: ArticleDto.Request) {
        this.title = articleRequest.title
        this.content = articleRequest.content
    }
}
