package com.csbenz.cryptocurrencylive.ui.details

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.network.NetworkUtils
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL


class DetailsActivity : AppCompatActivity() {

    val SUMMARY_LOW__KEY = "low"
    val SUMMARY_HIGH_KEY = "high"
    val SUMMARY_VOLUME_LEY = "volume"
    val SUMMARY_LAST_PRICE_KEY = "last_price"

    private lateinit var pairName: String
    private lateinit var noNetworkSnackbar: Snackbar

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
        if (NetworkUtils.isNetworkAvailable(this)) {
            doAsync {
                val result = URL(createSummaryUrl(pairName)).readText()
                uiThread {
                    displaySummary(result)
                }
            }
        } else {
            noNetworkSnackbar = Snackbar.make(details_root_layout, getString(R.string.network_unavailable), Snackbar.LENGTH_INDEFINITE)
            noNetworkSnackbar.setAction(getString(R.string.retry_network), {
                noNetworkSnackbar.dismiss()
                fetchSummary()
            })
            noNetworkSnackbar.show()

        }
    }

    private fun displaySummary(summary: String) {
        tv_summary.text = unJsonSummary(summary)
    }

    private fun unJsonSummary(summary: String): String {
        val jsonObject = JSONObject(summary)

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