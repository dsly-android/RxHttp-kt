package com.android.dsly.rxhttp.test

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
data class BaseResponse<T>(var code: Int, var msg: String, var data: T) {

    companion object {
        //成功
        val SUCCESS = 200

        //失败，不用提示
        val ERROR_NO_ALERT = 301

        //失败，要提示
        val ERROR_ALERT = 302

        //token失效
        val ERROR_TOKEN_OUTDATED = 401
    }
}