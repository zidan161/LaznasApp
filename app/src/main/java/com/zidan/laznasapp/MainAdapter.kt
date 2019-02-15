package com.zidan.laznasapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*

class MainAdapter(private val context: Context, private val items: List<Action>, private val listener: (Action) -> Unit) :
    RecyclerView.Adapter<MainAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int): MainHolder =
        MainHolder(LayoutInflater.from(context).inflate(R.layout.content_view, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class MainHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bindItem(item: Action, listener: (Action) -> Unit){

        }
    }
}