package com.csbenz.cryptocurrencylive

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pair_item.view.*

class PairAdapter(val items: ArrayList<String>, val context: Context, val listener: (String) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.pair_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position], listener)
    }

    fun setData(newItems: ArrayList<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindItems(pair: String, listener: (String) -> Unit) = with(view) {
        tv_pair_name.text = pair
        setOnClickListener { listener(pair) }
    }
}