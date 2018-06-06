package com.csbenz.cryptocurrencylive

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var pairAdapter: PairAdapter

    private var asyncTask: AsyncTask<Void, Void, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pairAdapter = PairAdapter(ArrayList(), this, { pair -> launchDetailsActivity(pair) })

        rv_pairs.layoutManager = LinearLayoutManager(this)
        rv_pairs.adapter = pairAdapter

        fetchAndUpdatePairs()
    }

    override fun onStop() {
        super.onStop()

        asyncTask?.cancel(true)
    }

    /**
     * Fetch currency pairs from Bitfinex and update the UI. Done asynchronously.
     */
    private fun fetchAndUpdatePairs() {
        asyncTask?.cancel(true)

        asyncTask = GetPairsAsyncTask(this, pairAdapter, Constants.BITFINEX_PAIR_LIST_URL).execute()
    }

    class GetPairsAsyncTask() : AsyncTask<Void, Void, String>() {

        private lateinit var adapter: PairAdapter
        private lateinit var contextRef: WeakReference<Context>
        private lateinit var url: String

        constructor(context: Context, adapter: PairAdapter, url: String) : this() {
            this.contextRef = WeakReference(context)
            this.adapter = adapter
            this.url = url
        }


        override fun onPreExecute() {
            super.onPreExecute()

            if (!isCancelled) {
                // stop processing
            }

            // TODO add dialog, spinning icon, network connectivity logic

            if (!NetworkUtils.isNetworkAvailable(contextRef.get())) {

                AlertDialog.Builder(contextRef.get()!!) // TODO double excl
                        .setTitle("Connectivity")
                        .setMessage("Please check your internet connection")
                        .setCancelable(false)
                        .setNegativeButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Try again") { _, _ ->
                            GetPairsAsyncTask(contextRef.get()!!, adapter, url).execute()
                        }
                        .show()

                this.cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void?): String {
            var response = ""

            if (!isCancelled) {
                response = NetworkUtils.getRequest(url)
            }

            return response
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val pairList = Utils.jsonPairToList(JSONArray(result))

            adapter.setData(pairList)
        }
    }

    private fun launchDetailsActivity(pair: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.PAIR_NAME_BUNDLE_ID, pair)
        startActivity(intent)
    }

}
