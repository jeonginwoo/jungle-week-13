package com.namanmoo.kotlinboard.domain.entity

import com.namanmoo.kotlinboard.domain.BaseEntity
import com.namanmoo.kotlinboard.service.dto.CommentDto
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false, length = 2000)
    var content: String,

    @Column(nullable = false)
    var articleId: Long,

    @Column
    var parentCommentId: Long? = null,

    @CreatedBy
    @Column(nullable = false)
    var createdBy: String = ""
): BaseEntity() {

    fun updateComment(commentRequest: CommentDto.Request) {
        this.content = commentRequest.content
    }
}