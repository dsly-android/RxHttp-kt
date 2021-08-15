package com.android.dsly.rxhttp.test

import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.TimeoutCancellationException
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * @author 陈志鹏
 * @date 2021/8/15
 */
val Throwable.errorCode: Int
    get() {
        val errorCode = when (this) {
            is HttpStatusCodeException -> this.statusCode //Http状态码异常
            is ParseException -> this.errorCode     //业务code异常
            else -> "-1"
        }
        return try {
            errorCode.toInt()
        } catch (e: Exception) {
            -1
        }
    }

val Throwable.errorMsg: String
    get() {
        return if (this is UnknownHostException) { //网络异常
            if (!NetworkUtils.isConnected())
                "当前无网络，请检查你的网络设置"
            else
                "网络连接不可用，请稍后重试！"
        } else if (
            this is SocketTimeoutException  //okhttp全局设置超时
            || this is TimeoutException     //rxjava中的timeout方法超时
            || this is TimeoutCancellationException  //协程超时
        ) {
            "连接超时,请稍后再试"
        } else if (this is ConnectException) {
            "网络不给力，请稍候重试！"
        } else if (this is HttpStatusCodeException) { //请求失败异常
            convertStatusCode(this)
        } else if (this is JsonSyntaxException) {  //请求成功，但Json语法异常,导致解析失败
            "数据解析失败,请检查数据是否正确"
        } else if (this is SSLHandshakeException) {
            "证书验证失败"
        } else if (this is ParseException) {       // ParseException异常表明请求成功，但是数据不正确
            this.message ?: errorCode   //msg为空，显示code
        } else {
            message ?: this.toString()
        }
    }

private fun convertStatusCode(httpException: HttpStatusCodeException): String {
    if (httpException.statusCode == "500") {
        return "服务器发生错误"
    } else if (httpException.statusCode == "404") {
        return "请求地址不存在"
    } else if (httpException.statusCode == "403") {
        return "请求被服务器拒绝"
    } else if (httpException.statusCode == "307") {
        return "请求被重定向到其他页面"
    } else {
        return httpException.message ?: ""
    }
}