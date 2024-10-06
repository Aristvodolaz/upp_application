package com.example.timetrekerforandroid.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.timetrekerforandroid.model.NetworkModel
import com.example.timetrekerforandroid.viewModel.InformationViewModel

class InformationModelFactory(private val networkModel: NetworkModel) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InformationViewModel::class.java)) {
            return InformationViewModel(networkModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}