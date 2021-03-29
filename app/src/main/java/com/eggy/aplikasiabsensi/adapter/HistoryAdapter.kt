package com.eggy.aplikasiabsensi.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eggy.aplikasiabsensi.R
import com.eggy.aplikasiabsensi.model.HistoryItem

class HistoryAdapter(val data:List<HistoryItem?>?) :RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggal = itemView.findViewById<TextView>(R.id.tanggal)
        val status = itemView.findViewById<TextView>(R.id.status)

        fun bind(item:HistoryItem){
            val date = item.waktu
            val divide = date?.split(" ", )
            val dateDivide = divide?.get(0)?.split("-")

            val dateString = "${dateDivide?.get(2)}/${dateDivide?.get(1)}/${dateDivide?.get(0)}"


            status.text = item.status
            tanggal.text = "pada $dateString"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data?.get(position)!!)
    }

    override fun getItemCount(): Int = data?.size ?: 0
}