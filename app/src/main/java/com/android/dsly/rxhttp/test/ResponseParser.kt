package com.android.dsly.rxhttp.test

import android.app.AlertDialog
import com.blankj.utilcode.util.*
import okhttp3.Response
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type

/**
 * 输入T,输出T,并对code统一判断
 * 如果使用协程发送请求，wrappers属性可不设置，设置了也无效
 */
@Parser(name = "Response")
open class ResponseParser<T> : TypeParser<T> {
    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     *
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     *
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected constructor() : super()

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     *
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse<Student>()
     */
    protected constructor(vararg types: Type) : super(*types)

    @Throws(IOException::class)
    override fun onParse(response: Response): T {
        val data: BaseResponse<T> = response.convertTo(BaseResponse::class, *types)
        if (data.code != BaseResponse.SUCCESS) { //code不等于0，说明数据不正确，抛出异常
            if (data.code == BaseResponse.ERROR_TOKEN_OUTDATED){
                RxHttpPlugins.getCache().removeAll()
//                LiveEventBus.get(EventBusTag.EVENT_TOKEN_INVALIDATION).post(TokenInvalidationEvent())
            }
            throw ParseException(data.code.toString(), data.msg, response)
        }
        return data.data
    }
}