package com.android.dsly.rxhttp.test

import android.app.Application
import com.blankj.utilcode.util.Utils
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.ssl.HttpsUtils
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)



        val builder = OkHttpClient.Builder();

        //不使用代理，防止抓包
        builder.proxy(Proxy.NO_PROXY)

        //全局的读取超时时间
        builder.readTimeout(10, TimeUnit.SECONDS)
        //全局的写入超时时间
        builder.writeTimeout(10, TimeUnit.SECONDS)
        //全局的连接超时时间
        builder.connectTimeout(10, TimeUnit.SECONDS)

        //1、信任所有证书,不安全有风险（默认信任所有证书）
        val sslParams = HttpsUtils.getSslSocketFactory()
        //2、使用预埋证书，校验服务端证书（自签名证书）
//        var inputStream: InputStream? = null
//        try {
//            inputStream = assets.open("srca.cer")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val sslParams = HttpsUtils.getSslSocketFactory(arrayOf(inputStream), null, null)
        //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        val sslParams = HttpsUtils.getSslSocketFactory(cerInputStream, bksInputStream, "123456")
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)

        //设置Hostname校验规则，默认实现返回true，需要时候传入相应校验规则即可
//        builder.hostnameVerifier({ hostname, session -> true })

        //cookie
//        val file = File(externalCacheDir, "cookie")
//        builder.cookieJar(CookieStore(file))

        val cacheFile = File(externalCacheDir, "cache")
        RxHttpPlugins.init(builder.build())
            .setDebug(BuildConfig.DEBUG, true)
            .setCache(cacheFile, Constants.maxSize, CacheMode.ONLY_NETWORK)
//            .setOnParamAssembly { param ->
//                val method = param.method
//                if (method.isGet) {
//                    param.add("method", "get")
//                } else if (method.isPost) { //Post请求
//                    param.add("method", "post")
//                }
//                param.add("versionName", "1.0.0") //添加公共参数
//                    .add("time", System.currentTimeMillis())
//                    .addHeader("deviceType", "android") //添加公共请求头
//            }
    }
}