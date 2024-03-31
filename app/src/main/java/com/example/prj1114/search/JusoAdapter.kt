package com.example.prj1114.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.prj1114.data.*
import com.example.prj1114.R
import kotlin.collections.ArrayList

class JusoAdapter(
    var itemList: ArrayList<Juso>,
    private val listener: OnAdapterItemClickListener
):RecyclerView.Adapter<JusoAdapter.ViewHolder>(){
    private var strBuilder = StringBuilder()
    private var adapterItemClickListener:OnAdapterItemClickListener = listener

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):JusoAdapter.ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.juso_item, parent, false)
        return ViewHolder(view, listener)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder:JusoAdapter.ViewHolder, position:Int){
        holder.bdNm.text = itemList[position].bdNm
        strBuilder = StringBuilder()
            .append(itemList[position].siNm).append(" ")
            .append(itemList[position].sggNm).append(" ")
            .append(itemList[position].emdNm)
        holder.addr.text = strBuilder
    }
    inner class ViewHolder(itemView:View, private val listener:OnAdapterItemClickListener):RecyclerView.ViewHolder(itemView){
        val card:CardView = itemView.findViewById(R.id.juso_card_view)
        val bdNm:TextView = itemView.findViewById(R.id.bdNm)
        val addr:TextView = itemView.findViewById(R.id.addr)

        init {
            itemView.setOnClickListener(object:View.OnClickListener{
                override fun onClick(v: View?) {
                    val position = adapterPosition
                    listener.onAdapterItemClickListener(position)
                }
            })
        }
    }
}