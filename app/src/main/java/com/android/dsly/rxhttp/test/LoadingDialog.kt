package com.android.dsly.rxhttp.test

import android.app.Dialog
import android.content.Context
import android.os.Bundle

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
class LoadingDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}