package com.example.timetrekerforandroid.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class WpsModelFactory(private val wpsModel: WpsModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WpsViewModel::class.java)) {
            return WpsViewModel(wpsModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}