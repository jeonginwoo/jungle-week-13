package com.namanmoo.kotlinboard.controller

import com.namanmoo.kotlinboard.common.autority.TokenInfo
import com.namanmoo.kotlinboard.common.dto.BaseResponse
import com.namanmoo.kotlinboard.service.ArticleService
import com.namanmoo.kotlinboard.service.CommentService
import com.namanmoo.kotlinboard.service.UserService
import com.namanmoo.kotlinboard.service.dto.ArticleDto
import com.namanmoo.kotlinboard.service.dto.CommentDto
import com.namanmoo.kotlinboard.service.dto.UserDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/user")
@RestController
class UserController(
    private val userService: UserService,
    private val articleService: ArticleService,
    private val commentService: CommentService
) {

    @GetMapping("/all-user")
    fun allUser(): ResponseEntity<List<UserDto.Response>> {
        val response = userService.findAllUser()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my-info")
    fun myInfo(): ResponseEntity<UserDto.Response> {
        val response = userService.findUser()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/articles")
    fun userArticles(): ResponseEntity<List<ArticleDto.Response>> {
        val response = articleService.findArticlesInUser()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/comments")
    fun userComments(): ResponseEntity<List<CommentDto.Response>> {
        val response = commentService.findCommentsInUser()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun userLogin(
        @RequestBody @Valid userRequest: UserDto.Login
    ): BaseResponse<TokenInfo> {
        val response = userService.login(userRequest)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), data = response)
    }

    @PostMapping("/sign-up")
    fun userSignUp(
        @RequestBody @Valid userRequest: UserDto.Request
    ): BaseResponse<String> {
        val response = userService.signUp(userRequest)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), message = response)
    }

    @PutMapping("")
    fun updateUser(
        @RequestBody @Valid userRequest: UserDto.Request,
    ): BaseResponse<UserDto.Response> {
        val response = userService.updateUser(userRequest)
        return BaseResponse(statusCode = HttpStatus.OK.toString(), data = response)
    }

    @DeleteMapping("/withdrawal")
    fun deleteUser(): BaseResponse<String> {
        val response = userService.deleteUser()
        return BaseResponse(statusCode = HttpStatus.OK.toString(), message = response)
    }
}