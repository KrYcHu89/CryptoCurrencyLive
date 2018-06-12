package com.csbenz.cryptocurrencylive.ui.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject


class DetailsActivity : AppCompatActivity() {

    val SUMMARY_LOW__KEY = "low"
    val SUMMARY_HIGH_KEY = "high"
    val SUMMARY_VOLUME_LEY = "volume"
    val SUMMARY_LAST_PRICE_KEY = "last_price"

    private lateinit var pairName: String

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

        return "$lastPrice\nVOL: $volume\nLOW: $low   HIGH: $high"
    }

    private fun createSummaryUrl(pairName: String): String {
        return Constants.BITFINEX_PAIR_SUMMARY_URL_PREFIX + pairName
    }

    override fun onStart() {
        super.onStart()

        fetchSummary()
    }
}