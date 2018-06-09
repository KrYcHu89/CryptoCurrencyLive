package com.csbenz.cryptocurrencylive

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.orderbook.OrderAdapter
import com.csbenz.cryptocurrencylive.orderbook.OrderBook
import kotlinx.android.synthetic.main.fragment_orderbook.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONTokener

class OrderBookFragment : Fragment() {

    private val ORDERS_TO_DISPLAY = 15

    private lateinit var bidOrderAdapter: OrderAdapter
    private lateinit var askOrderAdapter: OrderAdapter

    private lateinit var mContext: Context
    private lateinit var websocket: WebSocket

    private lateinit var pairName: String
    private val orderBook = OrderBook()


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        pairName = arguments!!.getString(Constants.PAIR_NAME_BUNDLE_ID)

        return inflater.inflate(R.layout.fragment_orderbook, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bidOrderAdapter = OrderAdapter(mContext, true)
        rv_orders_bid.layoutManager = LinearLayoutManager(activity)
        rv_orders_bid.adapter = bidOrderAdapter

        askOrderAdapter = OrderAdapter(mContext, false)
        rv_orders_ask.layoutManager = LinearLayoutManager(activity)
        rv_orders_ask.adapter = askOrderAdapter
    }

    override fun onStart() {
        super.onStart()

        startWebsocket()
    }

    override fun onStop() {
        super.onStop()

        stopWebsocket()
    }

    private fun startWebsocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url(Constants.BITFINEX_WEBSOCKET_URL).build()
        val listener = BitfinexWebSocketListener()

        websocket = client.newWebSocket(request, listener)

        client.dispatcher().executorService().shutdown()
    }

    private fun stopWebsocket() {
        websocket.cancel()
    }

    private var lastTimestamp: Long = 0

    fun parseNewUpdate(jsonArray: JSONArray) {

        when (JSONTokener(jsonArray[1].toString()).nextValue()) {
            is JSONArray -> {
                val orders: JSONArray = jsonArray[1] as JSONArray
                when (JSONTokener(orders[0].toString()).nextValue()) {
                    is JSONArray -> {
                        for (i in 0 until orders.length()) {
                            val e: JSONArray = orders[i] as JSONArray
                            orderBook.processNewOrder(e.getDouble(0), e.getInt(1), e.getDouble(2))
                        }
                    }
                    is Double -> {
                        orderBook.processNewOrder(orders.getDouble(0), orders.getInt(1), orders.getDouble(2))
                    }
                    else -> {
                        Log.v("cc", "Something even more complicated: " + orders[0].toString())
                    }
                }

            }
            is String -> {
                // "hb" value, ignore
            }
        }

        // Do not update the adapters too often
        val now = System.currentTimeMillis()
        if (now - lastTimestamp > 100) {
            bidOrderAdapter.setData(orderBook.getBidOrders().reversed().take(ORDERS_TO_DISPLAY))
            askOrderAdapter.setData(orderBook.getAskOrders().take(ORDERS_TO_DISPLAY))

            lastTimestamp = System.currentTimeMillis()
        }
    }


    private inner class BitfinexWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send(Utils.createBookRequest(pairName))
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            when (JSONTokener(text).nextValue()) {
                is JSONArray -> {
                    parseNewUpdate(JSONArray(text))
                }
                else -> {
                    // something else, ignore
                }
            }
        }

        /*
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
            webSocket.close(1000, null)
        }
        */

    }
}