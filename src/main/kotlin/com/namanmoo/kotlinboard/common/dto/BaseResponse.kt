package com.namanmoo.kotlinboard.common.dto

import com.namanmoo.kotlinboard.common.status.ResultCode

data class BaseResponse<T> (
    val statusCode: String = "",
    val resultCode: String = ResultCode.SUCCESS.name,
    val data: T? = null,
    val message: String = ResultCode.SUCCESS.msg,
)
