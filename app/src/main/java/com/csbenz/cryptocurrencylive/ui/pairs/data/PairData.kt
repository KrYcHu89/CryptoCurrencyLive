package com.csbenz.cryptocurrencylive.ui.pairs.data

import android.content.Context
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.network.NetworkUtils
import com.csbenz.cryptocurrencylive.utils.Utils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import java.net.URL

class PairData(private val context: Context) {

    fun fetchPairs(dataLoaderListener: DataLoaderListener) {
        if (NetworkUtils.isNetworkAvailable(context)) {

            doAsync {
                val response = URL(Constants.BITFINEX_PAIR_LIST_URL).readText()
                context.runOnUiThread {
                    val pairList = Utils.jsonArrayToList(JSONArray(response))

                    dataLoaderListener.loadingPairsSuccess(pairList)
                }
            }
        } else {
            dataLoaderListener.loadingPairsFailure()
        }
    }

    interface DataLoaderListener {
        fun loadingPairsSuccess(pairs: ArrayList<String>)
        fun loadingPairsFailure()
    }
}