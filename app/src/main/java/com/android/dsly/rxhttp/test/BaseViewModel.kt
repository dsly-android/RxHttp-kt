package com.android.dsly.rxhttp.test

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.CoroutineScope

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val showDialogLiveData = MutableLiveData<Boolean>()

    fun setShowDialog(showDialog: Boolean) {
        showDialogLiveData.value = showDialog
    }

    fun launch(
        showToast: Boolean = false,
        showDialog: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ) {
        rxLifeScope.launch({
            block()
        }, {
            if (showToast) {
                ToastUtils.showLong(it.errorMsg)
            }
        }, {
            if (showDialog) {
                setShowDialog(true)
            }
        }, {
            if (showDialog) {
                setShowDialog(false)
            }
        })
    }
}