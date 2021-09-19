package com.bllsht.xet3.workers

import com.bllsht.xet3.dto.Response
import com.bllsht.xet3.exceptions.XetException
import org.jsoup.Jsoup

internal object UrlWorker {
    data class Result(val response: Response?, val exception: XetException? = null)

    private const val HTML5_PLAYER = "HTML5Player"
    private val REGEX_LOW_URL by lazy { "setVideoUrlLow\\('(.*)'\\)".toRegex() }
    private val REGEX_HIGH_URL by lazy { "setVideoUrlHigh\\('(.*)'\\)".toRegex() }
    private val REGEX_HLS_URL by lazy { "setVideoHLS\\('(.*)'\\)".toRegex() }

    private const val MSG_SCRIPT_NOT_FOUND = "Required script that contains stream url not found."
    private const val MSG_REGEX_LOW_NOT_MATCH = "Cannot find direct link using regex."

    fun get(url: String): Result {
        try {
            val document = Jsoup.connect(url).get()
            val scriptElements = document.select("script")
            var selectedScript: String? = null

            for (scriptElement in scriptElements) {
                val script = scriptElement.toString()
                if (script.contains(HTML5_PLAYER)) {
                    selectedScript = script
                    break
                }
            }

            if (selectedScript != null) {
                val lowUrlResult = REGEX_LOW_URL.find(selectedScript)
                if (lowUrlResult != null && lowUrlResult.groupValues.isNotEmpty()) {
                    val sdUrl = lowUrlResult.groupValues[1]
                    val hdUrlResult = REGEX_HIGH_URL.find(selectedScript)
                    val hlsUrlResult = REGEX_HLS_URL.find(selectedScript)

                    val hdUrl =
                        if (hdUrlResult != null && hdUrlResult.groupValues.isNotEmpty()) hdUrlResult.groupValues[1] else null
                    val hlsUrl =
                        if (hlsUrlResult != null && hlsUrlResult.groupValues.isNotEmpty()) hlsUrlResult.groupValues[1] else null

                    return Result(Response(sdUrl, hdUrl, hlsUrl))
                } else return throwsException(MSG_REGEX_LOW_NOT_MATCH)
            } else return throwsException(MSG_SCRIPT_NOT_FOUND)

        } catch (e: Exception) {
            return throwsException(e.message)
        }
    }

    private fun throwsException(message: String?): Result {
        return Result(null, XetException(message))
    }
}