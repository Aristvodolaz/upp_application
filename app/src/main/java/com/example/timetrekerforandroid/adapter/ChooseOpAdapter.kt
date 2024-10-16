package com.example.timetrekerforandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.util.InfoForCheckBox
import com.example.timetrekerforandroid.util.InfoForCheckBox.infoBoxToDB
class ChooseOpAdapter(
    private val context: Context,
    private val names: Array<String>,
    private val valuesMap: Map<String, Boolean>  // Карта начальных значений для галочек
) : RecyclerView.Adapter<ChooseOpAdapter.LabelHolder>() {

    private val selectedItems = mutableSetOf<String>()
    var onClickItem: OnClickItem? = null

    init {
        // Инициализируем выбранные элементы на основе valuesMap
        selectedItems.addAll(valuesMap.filterValues { it }.keys)
    }

    fun getData(): List<String> {
        return selectedItems.toList()
    }

    fun setData(data: List<String>) {
        selectedItems.clear()
        selectedItems.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.choose_op_item, parent, false)
        return LabelHolder(view)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        val nameFiles = names[position]
        val nameFilesData = InfoForCheckBox.infoBoxToDB[position]
        holder.bind(nameFiles)

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems.contains(nameFilesData) || valuesMap[nameFilesData] == true

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(nameFilesData)
            } else {
                selectedItems.remove(nameFilesData)
            }
            onClickItem?.onItemClick(selectedItems)
        }
    }

    inner class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.name)
        val checkBox: CheckBox = itemView.findViewById(R.id.check)

        fun bind(nameFilesData: String) {
            text.text = nameFilesData
        }
    }

    interface OnClickItem {
        fun onItemClick(names: MutableSet<String>)
    }

    // Функция для получения данных для отправки на сервер
    fun getValuesForServer(): Map<String, String?> {
        val result = mutableMapOf<String, String?>()
        InfoForCheckBox.infoBoxToDB.forEach { key ->
            result[key] = if (selectedItems.contains(key)) "V" else null
        }
        return result
    }
}
