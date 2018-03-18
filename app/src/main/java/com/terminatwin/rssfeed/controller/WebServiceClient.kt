package com.terminatwin.rssfeed.controller

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.MySSLSocketFactory
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import com.terminatwin.rssfeed.model.ActionListener
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.security.KeyStore


open class WebServiceClient {

    protected fun sendRequest(type: RequestType, url: String, params: RequestParams, token: String, listener: ActionListener) {
        when (type) {
            WebServiceClient.RequestType.POST -> sendPost(url, params, token, listener)
            WebServiceClient.RequestType.GET -> sendGet(url, token, listener)
        }
    }

    private fun sendPost(url: String, params: RequestParams, token: String, listener: ActionListener) {
        val client = getAsyncHttpClient()
        client.post(url, params, getResponseHandler(listener))
    }

    private fun sendGet(url: String, token: String, listener: ActionListener) {
        val client = getAsyncHttpClient()
        client.get(url, getResponseHandler(listener))
    }

    private fun getResponseHandler(listener: ActionListener?): TextHttpResponseHandler {
        return object : TextHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseString: String, throwable: Throwable) {
                val error = getError(responseString)
                listener?.onFail(error ?: "")
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseString: String) {
                listener?.onSuccess(responseString)
            }
        }
    }

    private fun getError(responseString: String?): String? {
        if (responseString == null)
            return null

        var error: String = responseString
        try {
            val json = JSONObject(responseString)
            error = json.getString("error")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return error
    }

    protected fun sendPicture(url: String, params: RequestParams?, name: String, image: File, listener: ActionListener?) {
        var params = params
        val httpClient = getAsyncHttpClient()
        httpClient.setMaxRetriesAndTimeout(8, 1000)
        httpClient.setTimeout(60000)

        if (params == null)
            params = RequestParams()

        try {
            params.put(name, image, "multipart/form-data")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        httpClient.post(url, params, object : TextHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseString: String, throwable: Throwable) {
                val error = getError(responseString)
                listener?.onFail(error ?: "")
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseString: String) {
                listener?.onSuccess(responseString)
            }
        })
    }

    private fun getAsyncHttpClient(): AsyncHttpClient {
        val httpClient = AsyncHttpClient()

        var sf: SSLSocketFactory? = null
        try {
            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            trustStore.load(null, null)
            sf = MySSLSocketFactory(trustStore)
            sf.hostnameVerifier = MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        } catch (ignored: Exception) {
        }

        if (sf != null)
            httpClient.setSSLSocketFactory(sf)

        return httpClient
    }

    protected enum class RequestType {
        POST,
        GET
    }
}
