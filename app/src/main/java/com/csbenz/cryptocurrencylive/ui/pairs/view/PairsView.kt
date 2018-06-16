package com.csbenz.cryptocurrencylive.ui.pairs.view

interface PairsView {

    fun openDetailsActivity(pairName: String)

    fun displayNoNetworkSnackbar()

    fun showPairs(pairs: List<String>)
}