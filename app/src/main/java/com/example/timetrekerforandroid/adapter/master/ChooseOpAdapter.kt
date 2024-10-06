package com.example.timetrekerforandroid.adapter.master

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

    private val selectedItems = mutableSetOf<String>()
    var onClickItem: OnClickItem? = null
    private var areSwitchesEnabled: Boolean = false

    fun getValuesForServer(): Map<String, String?> {
        return InfoForCheckBox.infoBoxToDB.associateWith { key ->
            if (selectedItems.contains(key)) {
                "V" // Вернуть "V" для отмеченных элементов
            } else {
                null // Вернуть null для снятых элементов, чтобы исключить их
            }
        }
    }

    init {
        // Перенос значений из valuesMap в selectedItems
        valuesMap.forEach { (key, value) ->
            if (value) {
                selectedItems.add(key) // Добавляем ключ, если значение true
            }
        }
    }


    // Function to get the current selected items
    fun getData(): List<String> {
        return selectedItems.toList()
    }

    // Set selected items
    fun setData(data: List<String>) {
        selectedItems.clear()
        selectedItems.addAll(data)
        notifyDataSetChanged()
    }

    // Enable or disable switches
    fun setSwitchesEnabled(enabled: Boolean) {
        areSwitchesEnabled = enabled
        notifyDataSetChanged() // Refresh display
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

        holder.checkBox.setOnCheckedChangeListener(null) // Очистка слушателя перед установкой состояния
        holder.checkBox.isChecked = selectedItems.contains(nameFilesData) // Устанавливаем состояние чекбокса
        holder.checkBox.isEnabled = areSwitchesEnabled

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(nameFilesData) // Добавляем в выбранные при установке чекбокса
                Log.d("ChooseOpAdapter", "Added: $nameFilesData")
            } else {
                selectedItems.remove(nameFilesData) // Удаляем из выбранных при снятии чекбокса
                Log.d("ChooseOpAdapter", "Removed: $nameFilesData")
            }
            Log.d("ChooseOpAdapter", "Current selected items: $selectedItems")
            onClickItem?.onItemClick(selectedItems) // Сообщаем о изменении выбранных
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
}
