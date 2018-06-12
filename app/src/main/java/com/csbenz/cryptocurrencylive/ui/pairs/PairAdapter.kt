package com.csbenz.cryptocurrencylive.ui.pairs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.R
import kotlinx.android.synthetic.main.pair_item.view.*

class PairAdapter(val items: ArrayList<String>, val context: Context, val listener: (String) -> Unit) : RecyclerView.Adapter<PairViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        return PairViewHolder(LayoutInflater.from(context).inflate(R.layout.pair_item, parent, false))
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        holder.bindItems(items[position], listener)
    }

    fun setData(newItems: ArrayList<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class PairViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindItems(pair: String, listener: (String) -> Unit) = with(view) {
        tv_pair_name.text = pair
        setOnClickListener { listener(pair) }
    }
}