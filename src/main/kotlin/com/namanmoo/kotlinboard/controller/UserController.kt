package com.namanmoo.kotlinboard.controller

import com.namanmoo.kotlinboard.common.dto.BaseResponse
import com.namanmoo.kotlinboard.common.autority.TokenInfo
import com.namanmoo.kotlinboard.common.dto.CustomUser
import com.namanmoo.kotlinboard.service.UserService
import com.namanmoo.kotlinboard.service.dto.UserDto
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/user")
@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/all-user")
    fun allUser(): ResponseEntity<List<UserDto.Response>> {
        val response = userService.findAllUser()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my-info")
    fun myInfo(): ResponseEntity<UserDto.Response> {
        val userName = userService.getCurrentUsername()
        val response = userService.findUser(userName)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun userLogin(
        @RequestBody @Valid userRequest: UserDto.Login
    ): BaseResponse<TokenInfo> {
        val response = userService.login(userRequest)
        return BaseResponse(data=response)
    }

    @PostMapping("/sign-up")
    fun userSignUp(
        @RequestBody @Valid userRequest: UserDto.Request
    ): BaseResponse<String> {
        val response = userService.signUp(userRequest)
        return BaseResponse(message=response)
    }

    @PutMapping("/{user-name}")
    fun updateUser(
        @RequestBody @Valid userRequest: UserDto.Request,
        @PathVariable("user-name") userName: String
    ): BaseResponse<UserDto.Response> {
        val response = userService.updateUser(userRequest, userName)
        return BaseResponse(data=response)
    }

    @DeleteMapping("/{user-name}")
    fun deleteUser(
        @PathVariable("user-name") userName: String
    ): BaseResponse<Map<String, Any>> {
        val response = userService.deleteUser(userName)
        return BaseResponse(data=response)
    }
}