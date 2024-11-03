package com.namanmoo.kotlinboard.controller

import com.namanmoo.kotlinboard.common.dto.BaseResponse
import com.namanmoo.kotlinboard.service.CommentService
import com.namanmoo.kotlinboard.service.dto.CommentDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/articles/{article-id}/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/new-comment")
    fun createComment(
        @PathVariable("article-id") articleId: Long,
        @RequestBody commentRequest: CommentDto.Request
    ): BaseResponse<CommentDto.Response> {
        val response = commentService.createComment(articleId, commentRequest)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), data = response)
    }

    @GetMapping("")
    fun getComments(
        @PathVariable("article-id") articleId: Long
    ): BaseResponse<List<CommentDto.Response>> {
        val response = commentService.findCommentsInArticle(articleId)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), data = response)
    }

    @PutMapping("/{comment-id}")
    fun updateComment(
        @PathVariable("comment-id") commentId: Long,
        @RequestBody commentRequest: CommentDto.Request
    ): BaseResponse<CommentDto.Response> {
        val response = commentService.updateComment(commentId, commentRequest)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), data = response)
    }

    @DeleteMapping("/{comment-id}")
    fun deleteComment(
        @PathVariable("comment-id") commentId: Long
    ): BaseResponse<String> {
        val response = commentService.deleteComment(commentId)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), message = response)
    }
}