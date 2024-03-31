package com.example.prj1114.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.prj1114.data.OnAdapterItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.prj1114.R
import com.example.prj1114.data.Team
import java.util.Date
import kotlin.collections.ArrayList

class ListAdapter(
    var itemList: ArrayList<Team>,
    private val listener: OnAdapterItemClickListener
):RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    private var strBuilder = StringBuilder()
    private var adapterItemClickListener:OnAdapterItemClickListener = listener

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ListAdapter.ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view, listener)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder:ListAdapter.ViewHolder, position:Int){
        if(itemList[position].teamId == "")
            holder.addr.text = "create new"
        else {
            val date = Date(itemList[position].time!!)
            holder.time.text = date.toString()
            strBuilder = StringBuilder()
                .append(itemList[position].end!!.sggNm).append(" ")
                .append(itemList[position].end!!.emdNm)
            holder.addr.text = strBuilder
        }
    }
    inner class ViewHolder(itemView:View, private val listener:OnAdapterItemClickListener):RecyclerView.ViewHolder(itemView){
        val card:CardView = itemView.findViewById(R.id.list_card_view)
        val time:TextView = itemView.findViewById(R.id.time)
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