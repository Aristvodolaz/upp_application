package com.example.timetrekerforandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.network.response.WpsResponse
import com.example.timetrekerforandroid.network.response.Zapis
import com.example.timetrekerforandroid.network.response.ZapisResponse

class WpsAdapter(
    private val context: Context,
    private var data: List<Zapis>
) : RecyclerView.Adapter<WpsAdapter.LabelHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_zapis, parent, false)
        return LabelHolder(itemView)
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val zapis = data[position]
        holder.bind(zapis)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<Zapis>) {
        Log.d("WpsAdapter", "Обновляем данные: ${newData.size}")
        this.data = newData
        notifyDataSetChanged()
    }


    inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(zapis: Zapis) {
            titleTextView.text = "Вложенность: ${zapis.kolvoTovarov}"
            descriptionTextView.text = zapis.shkWps
        }
    }
}

