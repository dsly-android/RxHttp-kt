package com.android.dsly.rxhttp.test

import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.annotation.Domain

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
object Constants {
    //设置为默认域名
    @JvmField
    @DefaultDomain
    var baseUrl = "https://gank.io/"

    //另一个域名
    @JvmField
    @Domain(name = "Apiopen", className = "Apiopen")
    var apiopen = "https://api.apiopen.top/"

    //rxhttp缓存最大大小(100M)
    val maxSize: Long = 100 * 1024 * 1024
}