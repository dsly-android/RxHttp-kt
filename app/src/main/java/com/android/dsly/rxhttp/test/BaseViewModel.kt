package com.android.dsly.rxhttp.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val showDialogLiveData = MutableLiveData<Boolean>()

    fun setShowDialog(showDialog: Boolean) {
        showDialogLiveData.value = showDialog
    }
}