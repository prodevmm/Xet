package com.bllsht.xet3.dto

data class Response(val sdUrl: String, private val _hdUrl: String?, private val _hlsUrl: String?) {
    val hdUrl get() = _hdUrl ?: ""
    val hlsUrl get() = _hlsUrl ?: ""

    fun isHDAvailable(): Boolean {
        return _hdUrl != null
    }

    fun isHlsAvailable(): Boolean {
        return _hlsUrl != null
    }
}