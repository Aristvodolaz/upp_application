package com.example.timetrekerforandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.util.SPHelper

class NameFileAdapter(private val context: Context, private val namesList: List<String>,
    private val onClickItem: OnClickItem): RecyclerView.Adapter<NameFileAdapter.LabelHolder>() {
    interface OnClickItem{
        fun onClick(name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.name_file_item, parent, false)
        return LabelHolder(view)
    }

    override fun getItemCount(): Int {
        return namesList.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val nameFilesData = namesList[position]
        holder.bind(nameFilesData)
        holder.itemView.setOnClickListener {
            SPHelper.setNameTask(namesList[position])
            onClickItem.onClick(namesList[position])
        }

    }

    inner class LabelHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val text: TextView = itemView.findViewById(R.id.text)
        fun bind(nameFilesData: String){
            text.text = nameFilesData
        }
    }

}