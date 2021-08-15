package com.android.dsly.rxhttp.test

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.Dispatchers
import okhttp3.internal.wait
import rxhttp.*
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.param.RxApiopenHttp
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toResponse
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * @author 陈志鹏
 * @date 2021/8/14
 */
class MainActivity : AppCompatActivity() {

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val viewModel: BaseViewModel by lazy {
        ViewModelProviders.of(this).get(BaseViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.showDialogLiveData.observe(this, {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.hide()
            }
        })
    }

    fun click1(view: View) {
        rxLifeScope.launch({
            val result = RxHttp.get("api/data/Android/10/1")
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click2(view: View) {
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click3(view: View) {
        rxLifeScope.launch({
            val result = RxHttp.postForm("http://t.xinhuo.com/index.php/Api/Pic/uploadPic")
                .addFile("aaa", "/storage/emulated/0/configmanager.json")
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click5(view: View) {
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .setCacheMode(CacheMode.ONLY_NETWORK)
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click6(view: View) {
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE)
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click7(view: View) {
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .setCacheMode(CacheMode.READ_CACHE_FAILED_REQUEST_NETWORK)
                .toStr().retry(2).await()
            LogUtils.i(result)
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click8(view: View) {
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .setCacheMode(CacheMode.ONLY_CACHE)
                .toStr().retry(2).await()
            LogUtils.i("click81")
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
        rxLifeScope.launch({
            val result = RxApiopenHttp.get("getJoke")
                .setCacheMode(CacheMode.NETWORK_SUCCESS_WRITE_CACHE)
                .toStr().retry(2).await()
            LogUtils.i("click82")
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click10(view: View) {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "aaa.apk")
        rxLifeScope.launch({
            RxHttp.get("https://34463b61b3ace6b08aa8d549200b179c.dlied1.cdntips.net/imtt.dd.qq.com/16891/apk/CD08ABAE3EEB07AE136108D5EA708E31.apk?mkey=5fdc461b1b9af0bb&f=1ea1&fsname=com.xiachufang_7.6.8_606.apk&csr=1bbd&cip=27.154.214.78&proto=https")
                .toDownload(file.toString(), Dispatchers.Main) {
                    ToastUtils.showLong(it.progress.toString())
                }
                .await()
        }, {
            ToastUtils.showLong(it.errorMsg)
        }, {
            viewModel.setShowDialog(true)
        }, {
            viewModel.setShowDialog(false)
        })
    }

    fun click11(view: View) {
        rxLifeScope.launch({
            val result = RxHttp.get("http://192.168.0.8:3001/test/log")
                .setCacheMode(CacheMode.READ_CACHE_FAILED_REQUEST_NETWORK)
                .toResponse<Any>().await()
            LogUtils.i("click11:" + result)
        }, {
            if (it is ParseException) {
                ToastUtils.showLong(it.errorCode + "  " + it.errorMsg)
            }
        })
    }

    fun click12(view: View) {
        var i : Int = 1
        rxLifeScope.launch {
            val result = RxHttp.get("api/data/Android/10/1")
                .toStr().repeat(10, 1000) {
                    //最后一次请求不会走这里
                    LogUtils.i("click12:" + (i++))
                    false
                }.await()
            //最后一次请求会走这里
            LogUtils.i("click12:" + (i++))
        }
    }
}