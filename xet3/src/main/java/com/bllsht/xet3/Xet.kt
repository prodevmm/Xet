package com.bllsht.xet3

import com.bllsht.xet3.dto.Response
import com.bllsht.xet3.exceptions.XetException
import com.bllsht.xet3.workers.UrlWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Xet {
    abstract class Callback {
        abstract fun onSuccess(response: Response)
        abstract fun onFailure(exception: XetException)
    }

    fun fetch(url: String, callback: Callback) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UrlWorker.get(url)
            CoroutineScope(Dispatchers.Main).launch {
                result.response?.let { callback.onSuccess(it) }
                result.exception?.let { callback.onFailure(it) }
            }
        }
    }

}