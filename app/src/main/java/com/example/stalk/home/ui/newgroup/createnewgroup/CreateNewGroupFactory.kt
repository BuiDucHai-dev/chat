package com.example.stalk.home.ui.newgroup.createnewgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateNewGroupFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateNewGroupViewModel::class.java)){
            return CreateNewGroupViewModel(
                repository = CreateNewGroupRepository(
                    dataSource = CreateNewGroupDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}