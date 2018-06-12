package com.csbenz.cryptocurrencylive.ui.pairs

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.network.NetworkUtils
import com.csbenz.cryptocurrencylive.ui.details.DetailsActivity
import com.csbenz.cryptocurrencylive.utils.Utils
import kotlinx.android.synthetic.main.activity_pairs.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import java.net.URL

class PairsActivity : AppCompatActivity() {

    private lateinit var pairAdapter: PairAdapter
    private lateinit var noNetworkSnackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairs)

        pairAdapter = PairAdapter(ArrayList(), this, { pair -> launchDetailsActivity(pair) })

        rv_pairs.layoutManager = LinearLayoutManager(this)
        rv_pairs.adapter = pairAdapter

    }


    override fun onResume() {
        super.onResume()

        fetchAndUpdatePairs()
    }

    /**
     * Fetch currency pairs from Bitfinex and update the UI. Done asynchronously.
     */
    private fun fetchAndUpdatePairs() {
        if (NetworkUtils.isNetworkAvailable(this)) {

            doAsync {
                val response = URL(Constants.BITFINEX_PAIR_LIST_URL).readText()  //NetworkUtils.getRequest(Constants.BITFINEX_PAIR_LIST_URL)
                runOnUiThread {
                    val pairList = Utils.jsonArrayToList(JSONArray(response))

                    pairAdapter.setData(pairList)
                }
            }

        } else {
            noNetworkSnackbar = Snackbar.make(pairs_root_layout, getString(R.string.network_unavailable), Snackbar.LENGTH_INDEFINITE)
            noNetworkSnackbar.setAction(getString(R.string.retry_network), {
                noNetworkSnackbar.dismiss()
                fetchAndUpdatePairs()
            })
            noNetworkSnackbar.show()
        }
    }

    private fun launchDetailsActivity(pair: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.PAIR_NAME_BUNDLE_ID, pair)
        startActivity(intent)
    }

}
