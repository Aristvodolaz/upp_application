package com.example.timetrekerforandroid.adapter.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.DataWBResponse

class EditAdapterForWb (
    private val context: Context,
    private var artikulData: List<DataWBResponse>,
    private val onClickItem: OnClickItem
) : RecyclerView.Adapter<EditAdapterForWb.LabelHolder>() {

    interface OnClickItem {
        fun onClick(item: DataWBResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.artikul_tasks_item, parent, false)
        return LabelHolder(view)
    }

    fun setFilterData(data: List<DataWBResponse>) {
        artikulData = data.filterNotNull()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = artikulData.size

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val supportTask = artikulData[position]
        holder.bind(supportTask)

        holder.itemView.setOnClickListener {
            if (holder.itemView.isClickable) {
                onClickItem.onClick(supportTask)
            }
        }
    }




    inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val artikul: TextView = itemView.findViewById(R.id.artikul)
        private val shk: TextView = itemView.findViewById(R.id.shk)

        @SuppressLint("SetTextI18n")
        fun bind(data: DataWBResponse) {
            name.text = "Артикул: ${data.artikul}"
            artikul.text = data?.shk?.toString()
            shk.text ="Паллет:${data?.pallet}"
        }
    }
}