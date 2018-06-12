package com.csbenz.cryptocurrencylive.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object NetworkUtils {

    fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun getRequest(url: String): String {

        val okHttpClient = OkHttpClient.Builder()

                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url(url)
                .build()

        val response = okHttpClient.newCall(request).execute()

        return response.body()!!.string()
    }
}