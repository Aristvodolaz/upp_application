package com.example.timetrekerforandroid.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timetrekerforandroid.model.TaskWpsModel
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.viewModel.TaskWpsViewModel
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class TaskWpsModelFactory (private val wpsModel: TaskWpsModel) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskWpsViewModel::class.java)) {
                return TaskWpsViewModel(wpsModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}