package com.namanmoo.kotlinboard.controller

import com.namanmoo.kotlinboard.service.UserService
import com.namanmoo.kotlinboard.service.dto.UserDto
import org.springframework.http.ResponseEntity
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

    @PostMapping("/login")
    fun userLogin(
        @RequestBody loginInfo: UserDto.Login
    ): ResponseEntity<Map<String, Any>> {
        val response = userService.userLogin(loginInfo)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/sign-up")
    fun userSignUp(
        @RequestBody signUpInfo: UserDto.SignUp
    ): ResponseEntity<UserDto.Response> {
        val response = userService.userSignUp(signUpInfo)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{user-name}")
    fun updateUser(
        @RequestBody userRequest: UserDto.Request,
        @PathVariable("user-name") userName: String
    ): ResponseEntity<UserDto.Response> {
        val response = userService.updateUser(userRequest, userName)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{user-name}")
    fun deleteUser(
        @PathVariable("user-name") userName: String
    ): ResponseEntity<Map<String, Any>> {
        val response = userService.deleteUser(userName)
        return ResponseEntity.ok(response)
    }
}