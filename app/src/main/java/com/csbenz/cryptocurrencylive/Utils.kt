package com.csbenz.cryptocurrencylive

import org.json.JSONArray

object Utils {

    fun jsonPairToList(jsonArray: JSONArray): ArrayList<String> {
        val pairList: ArrayList<String> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            val pair: String = jsonArray[i].toString()
            pairList.add(pair)
        }

        return pairList
    }
}