package com.example.timetrekerforandroid.adapter.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.network.response.ArticlesResponse

class EditAdapter (
    private val context: Context,
    private var artikulData: List<ArticlesResponse.Articuls>,
    private val onClickItem: OnClickItem
    ) : RecyclerView.Adapter<EditAdapter.LabelHolder>() {

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

            holder.itemView.isClickable = isItemClickable(supportTask)
            holder.itemView.setOnClickListener {
                if (holder.itemView.isClickable) {
                    onClickItem.onClick(supportTask)
                }
            }
        }

        private fun isItemClickable(item: ArticlesResponse.Articuls): Boolean {
            return item.status == 2
        }

        fun setFilterData(data: List<ArticlesResponse.Articuls?>) {
            artikulData = data.filterNotNull().filter { it.status == 2 }
            notifyDataSetChanged()
        }

        inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val name: TextView = itemView.findViewById(R.id.name)
            private val artikul: TextView = itemView.findViewById(R.id.artikul)
            private val shk: TextView = itemView.findViewById(R.id.shk)

            fun bind(data: ArticlesResponse.Articuls) {
                name.text = data.nazvanieTovara
                artikul.text = data.artikul.toString()
                shk.text = data?.shk?.toString()
            }
        }
    }