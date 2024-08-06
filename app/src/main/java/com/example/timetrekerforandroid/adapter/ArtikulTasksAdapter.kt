package com.example.timetrekerforandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.util.SPHelper

class ArtikulTasksAdapter(private val context: Context, private var artikulData: List<ArticlesResponse.Articuls>,
                          private val onClickItem: OnClickItem): RecyclerView.Adapter<ArtikulTasksAdapter.LabelHolder>() {

    interface OnClickItem{
        fun onClick(item: ArticlesResponse.Articuls)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.artikul_tasks_item, parent,false)
        return LabelHolder(view)
    }

    override fun getItemCount(): Int {
        return artikulData.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val supportTasks = artikulData[position]
        holder.bind(supportTasks)
        holder.itemView.setOnClickListener {
            onClickItem.onClick(artikulData[position])
        }
        holder.itemView.isClickable = shouldItemBeClickable(supportTasks)
        holder.itemView.setOnClickListener {
            if (holder.itemView.isClickable) { onClickItem.onClick(supportTasks) }
        }
    }
    private fun shouldItemBeClickable(item: ArticlesResponse.Articuls): Boolean {
        val isStatusValid = item.status != 1 && item.status != 2
        val isAssignedToCurrentUser = item.status == 1 && item.ispolnitel == SPHelper.getNameEmployer()
        return isStatusValid || isAssignedToCurrentUser
    }

    fun setFilterData(data: List<ArticlesResponse.Articuls?>) {
        artikulData = data.filterNotNull()
        notifyDataSetChanged()
    }

    inner class LabelHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val name: TextView = itemView.findViewById(R.id.name)
        private val status: TextView = itemView.findViewById(R.id.status)
        private val artikul: TextView = itemView.findViewById(R.id.artikul)
        private val shk: TextView = itemView.findViewById(R.id.shk)

        fun bind(data: ArticlesResponse.Articuls){
            name.text = data.nazvanieTovara
            when (data.status) {
                0 -> status.text = "Ожидает"
                1 -> status.text = "В работе"
                2 -> status.text = "Завершено"
            }
            artikul.text = data.articul.toString()
            shk.text = data.shk.toString()
        }

    }
}