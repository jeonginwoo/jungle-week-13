package com.namanmoo.kotlinboard.domain

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Getter
@ToString
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
    @Setter
    @Column(nullable = false)
    var title: String,

    @Setter
    @Column(nullable = false, length = 10000)
    var content: String,

    @Setter
    @Column(nullable = false)
    var password: String,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    protected constructor() : this("", "", "") // JPA 기본 생성자

    companion object {
        fun of(title: String, content: String, password: String): Article = Article(title, content, password)
    }

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
}
