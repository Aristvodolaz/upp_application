package com.example.timetrekerforandroid.adapter.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.util.SPHelper

class PakingAdapter(
    private val context: Context,
    private var artikulData: List<ArticlesResponse.Articuls>,
    private val onClickItem: OnClickItem
) : RecyclerView.Adapter<PakingAdapter.LabelHolder>() {

    interface OnClickItem {
        fun onClick(item: ArticlesResponse.Articuls)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.artikul_tasks_item, parent, false)
        return LabelHolder(view)
    }

    override fun getItemCount(): Int = artikulData.size

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val supportTask = artikulData[position]
        holder.bind(supportTask)

        holder.itemView.isClickable = shouldItemBeClickable(supportTask)
        holder.itemView.setOnClickListener {
            if (holder.itemView.isClickable) {
                onClickItem.onClick(supportTask)
            }
        }
    }

    private fun shouldItemBeClickable(item: ArticlesResponse.Articuls): Boolean {
        // Можете оставить логику проверки кликабельности, если необходимо
        return item.status == 3 // Делаем кликабельными только статус 3
    }

    // Фильтрация данных по статусу (только 0 и 1)
    fun setFilterData(data: List<ArticlesResponse.Articuls?>) {
        artikulData = data.filterNotNull().filter { it.status == 3 } // Фильтруем только статусы 0 и 1
        notifyDataSetChanged() // Обновляем данные в адаптере
    }

    inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val status: TextView = itemView.findViewById(R.id.status)
        private val artikul: TextView = itemView.findViewById(R.id.artikul)
        private val shk: TextView = itemView.findViewById(R.id.shk)

        fun bind(data: ArticlesResponse.Articuls) {
            name.text = data.nazvanieTovara
            status.text = "Упаковка" // Поскольку отображаем только статус 3, статуса будет один
            artikul.text = data.artikul?.toString()
            shk.text = data.shk?.toString()
        }
    }
}
