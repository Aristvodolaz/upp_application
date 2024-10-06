package com.example.timetrekerforandroid.adapter.navigation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.util.InfoForCheckBox


class ChooseOpAdapter(
    private val context: Context,
    private val names: Array<String>,
    private val valuesMap: Map<String, Boolean>
) : RecyclerView.Adapter<ChooseOpAdapter.LabelHolder>() {

    private val selectedItems = mutableSetOf<String>() // Store selected items
    private var areSwitchesEnabled: Boolean = false

    fun getData(): List<String> {
        return selectedItems.toList()
    }

    fun setData(data: Array<String>, valuesMap: Map<String, Boolean>) {
        selectedItems.clear()
        selectedItems.addAll(data)
        notifyDataSetChanged()
    }

    fun setSwitchesEnabled(enabled: Boolean) {
        areSwitchesEnabled = enabled
        notifyDataSetChanged() // Update display
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.choose_op_item, parent, false)
        return LabelHolder(view)
    }

    override fun getItemCount(): Int {
        // Return count of filtered items
        return names.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems.contains(names[position])
        holder.checkBox.isEnabled = areSwitchesEnabled

        holder.text.text = names[position]

    }

    inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.name)
        val checkBox: CheckBox = itemView.findViewById(R.id.check)

        fun bind(nameFilesData: String) {
            text.text = nameFilesData
        }
    }

}
