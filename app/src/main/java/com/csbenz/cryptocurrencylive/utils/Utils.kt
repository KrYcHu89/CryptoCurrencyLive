package com.csbenz.cryptocurrencylive.utils

import org.json.JSONArray

object Utils {

    fun jsonArrayToList(jsonArray: JSONArray): ArrayList<String> {
        val l: ArrayList<String> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            val e: String = jsonArray[i].toString()
            l.add(e)
        }

        return l
    }

    fun createBookRequest(pairName: String): String {
        val request = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"book\",\n" +
                "   \"pair\":\"" + pairName + "\",\n" +
                "   \"prec\":\"P0\",\n" +
                "   \n" +
                "   \"freq\":\"F0\",\n" +
                "   \"length\":\"25\"\n" +
                "}"
        return request
    }

    fun createTradesRequest(pairName: String): String {
        val request = "{\n" +
                "  \"event\": \"subscribe\",\n" +
                "  \"channel\": \"trades\",\n" +
                "  \"pair\": \"" + pairName + "\"\n" +
                "}"

        return request
    }
}