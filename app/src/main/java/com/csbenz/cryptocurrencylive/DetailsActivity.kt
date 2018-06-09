package com.csbenz.cryptocurrencylive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.csbenz.cryptocurrencylive.orderbook.OrderBook
import kotlinx.android.synthetic.main.activity_details.*
import okhttp3.WebSocket
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject


class DetailsActivity : AppCompatActivity() {

    val SUMMARY_LOW__KEY = "low"
    val SUMMARY_HIGH_KEY = "high"
    val SUMMARY_VOLUME_LEY = "volume"
    val SUMMARY_LAST_PRICE_KEY = "last_price"


    private lateinit var websocket: WebSocket
    private lateinit var pairName: String

    private val orderBook = OrderBook()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        pairName = intent.getStringExtra(Constants.PAIR_NAME_BUNDLE_ID)
        (this as AppCompatActivity).supportActionBar?.title = pairName

        val fragmentAdapter = DetailsPagerAdapter(supportFragmentManager, pairName)
        vp_details.adapter = fragmentAdapter

        tabs_details.setupWithViewPager(vp_details)

    }

    private fun fetchSummary() {
        doAsync {
            val result = NetworkUtils.getRequest(createSummaryUrl(pairName))
            uiThread {
                displaySummary(result)
            }
        }
    }

    private fun displaySummary(summary: String) {
        tv_summary.text = unJsonSummary(summary)
    }

    private fun unJsonSummary(summary: String): String {
        val jsonObject: JSONObject = JSONObject(summary)

        val lastPrice = jsonObject.get(SUMMARY_LAST_PRICE_KEY)
        val high = jsonObject.get(SUMMARY_HIGH_KEY)
        val low = jsonObject.get(SUMMARY_LOW__KEY)
        val volume = jsonObject.get(SUMMARY_VOLUME_LEY)

        return "k$lastPrice\nVOL: $volume\nLOW: $low\tHIGH: $high"
    }

    private fun createSummaryUrl(pairName: String): String {
        return Constants.BITFINEX_PAIR_SUMMARY_URL_PREFIX + pairName
    }

    override fun onStart() {
        super.onStart()

        fetchSummary()
    }
}