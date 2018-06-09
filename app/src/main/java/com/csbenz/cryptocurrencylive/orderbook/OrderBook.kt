package com.csbenz.cryptocurrencylive.orderbook

import android.util.Log
import android.util.Pair
import java.util.*

class OrderBook(val book: TreeMap<Double, Pair<Double, Int>> = TreeMap()) {
    // Key is price, value is pair of amout-count


    fun processNewOrder(price: Double, count: Int, amount: Double) {
        if (count == 0) {
            removeOrder(price)
        } else {
            addOrder(price, count, amount)
        }
    }

    fun addOrder(price: Double, count: Int, amount: Double) {
        Log.v("cryptok", "Adding order: $count  $price  $amount")
        if (book.containsKey(price) && book[price] != null) {
            book[price] = Pair(book[price]!!.first, book[price]!!.second)
        } else {
            book[price] = Pair(amount, count)
        }
    }

    fun removeOrder(price: Double) {
        Log.v("cryptok", "Removing order: $price")

        book.remove(price)
    }

    fun getAllOrders(): List<Triple<Double, Double, Int>> {
        return toList()
    }


    fun getBidOrders(): List<Triple<Double, Double, Int>> {
        return toList().filter { o -> o.second > 0 }
    }

    fun getAskOrders(): List<Triple<Double, Double, Int>> {
        return toList().filter { o -> o.second < 0 }
    }

    private fun toList(): ArrayList<Triple<Double, Double, Int>> {
        val l: ArrayList<Triple<Double, Double, Int>> = ArrayList()

        for ((price, vs) in book) {
            l.add(Triple(price, vs.first, vs.second))
        }

        return l
    }
}