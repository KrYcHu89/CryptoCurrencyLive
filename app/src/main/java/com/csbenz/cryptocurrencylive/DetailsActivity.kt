package com.csbenz.cryptocurrencylive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val pairName: String = intent.getStringExtra(Constants.PAIR_NAME_BUNDLE_ID)

        (this as AppCompatActivity).supportActionBar?.title = pairName

    }
}